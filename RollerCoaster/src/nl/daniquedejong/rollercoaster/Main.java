/*
*  _______                .__                           .___            ____.
* \______ \ _____    ____ |__| ________ __   ____     __| _/____       |    | ____   ____    ____
*  |    |  \\__  \  /    \|  |/ ____/  |  \_/ __ \   / __ |/ __ \      |    |/  _ \ /    \  / ___\
*  |    `   \/ __ \|   |  \  < <_|  |  |  /\  ___/  / /_/ \  ___/  /\__|    (  <_> )   |  \/ /_/  >
* /_______  (____  /___|  /__|\__   |____/  \___  > \____ |\___  > \________|\____/|___|  /\___  /
*         \/     \/     \/       |__|           \/       \/    \/                       \//_____/
* Authors:  Daníque de Jong <danique at daniquedejong.nl>
*     		Bart Fijneman <bart at bartfijneman.com>
*
*********************
*
* Copyright (c) 2017  Daníque de Jong
* All Rights Reserved.
*
*/
package nl.daniquedejong.rollercoaster;

import java.io.*;
import java.util.HashMap;
import java.util.List;

import nl.daniquedejong.rollercoaster.file.Config;
import nl.daniquedejong.rollercoaster.file.ConfigManager;
import nl.daniquedejong.rollercoaster.listeners.Command_Listener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import nl.daniquedejong.rollercoaster.listeners.EntityClick_Listener;

public class Main extends JavaPlugin
{
	public static Main r;
	public Configuration c;
	public File config;
	public static ConfigManager manager;

	public HashMap<String, Rollercoaster> Attracties = new HashMap<String, Rollercoaster>();
		
	public void onEnable()
	{
		r = this;
		saveDefaultConfig();
		c = getConfig();
		config = new File(this.getDataFolder(), "config.yml");
		manager = new ConfigManager(this);
		try{
			if(!(new PluginStats().sendStats())){
				this.setEnabled(false);
				return;}} 
		catch (IOException e) {
			e.printStackTrace();
			this.setEnabled(false);
			return;
		}
		
		getAllAttracties();
		
		getCommand("rc").setExecutor(new Command_Listener());
		
		Bukkit.getPluginManager().registerEvents(new EntityClick_Listener(), this);
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.r, new Runnable()
		{
		    public void run()
		    {
		    	for(Rollercoaster att : Attracties.values()){
		    		if(att.allowLeave == false){
		    			HashMap<Player, ArmorStand> list = att.getPassagiers();
		    			for(Player wp : list.keySet()){
		    				if(list.get(wp).getPassenger() == null){
		    					list.get(wp).setPassenger(wp);
		    				}
		    			}
		    		}else{
		    			HashMap<Player, ArmorStand> list = att.getPassagiers();
		    			for(Player wp : list.keySet()){
		    				if(list.get(wp).getPassenger() == null){
		    					att.removePassagier(wp);
		    				}
		    			}
		    		}
		    	}
		    }
		}, 20L, 1L);
	}
	public void onDisable()
	{
		removeAllAttracties();
	}
	public void getAllAttracties()
	{
		for(String att : c.getStringList("attractions")){
			//Config conf = manager.getNewConfig(att + ".yml");
			manager.prepareFile(att+".yml", "rollercoaster.yml");
			Config conf = manager.getNewConfig(att+".yml");



			HashMap<String, List<String>> runs = new HashMap<String, List<String>>();
			List<String> allruns = (List<String>)conf.getList("runs");
			for(int i = 0; i < allruns.size(); i++){
				List<String> runsofattraction = (List<String>) conf.getList("run." + allruns.get(i).toString());
				runs.put(allruns.get(i).toString(), runsofattraction);
			}

			HashMap<String, List<String>> spawns = new HashMap<String, List<String>>();
			List<String> allspawns = (List<String>)conf.getList("spawns");
			for(int i = 0; i < allspawns.size(); i++){
				List<String> spawnsofattraction = (List<String>) conf.getList("spawn." + allspawns.get(i).toString());
				spawns.put(allspawns.get(i).toString(), spawnsofattraction);
			}
			
			Rollercoaster newRollercoaster = new Rollercoaster(
					att,
					conf.getString("run_command"),
					conf.getString("run_permissions"),
					conf.getString("spawn_command"),
					conf.getString("spawn_permissions"),
					spawns,
					runs
					);
			
			Attracties.put(att, newRollercoaster);
		}
	}
	public void removeAllAttracties()
	{
		for(Rollercoaster att : Attracties.values()){
			att.remove__A();
		}
		Attracties.clear();
	}
	public void ReloadConfig()
	{
		c = YamlConfiguration.loadConfiguration(config);
		
		removeAllAttracties();
		getAllAttracties();
	}
}
