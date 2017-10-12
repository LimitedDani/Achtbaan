package com.bartfijneman.achtbaan;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.bartfijneman.achtbaan.listeners.Command_Listener;
import com.bartfijneman.achtbaan.listeners.EntityClick_Listener;

public class Main extends JavaPlugin
{
	public static Main r;
	public Configuration c;
	public File config;
	
	public HashMap<String, attractie> Attracties = new HashMap<String, attractie>();
		
	public void onEnable()
	{
		r = this;
		saveDefaultConfig();
		c = getConfig();
		config = new File(this.getDataFolder(), "config.yml");
		
		try{
			if(!(new PluginStats().sendStats())){
				this.setEnabled(false);
				return;}} 
		catch (IOException e) {
			e.printStackTrace();
			this.setEnabled(false);
			return;}
		
		getAllAttracties();
		
		getCommand("achtbaan").setExecutor(new Command_Listener());
		
		Bukkit.getPluginManager().registerEvents(new EntityClick_Listener(), this);
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.r, new Runnable()
		{
		    public void run()
		    {
		    	for(attractie att : Attracties.values()){
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
		for(String att : c.getStringList("attracties")){
			
			HashMap<String, List<String>> runs = new HashMap<String, List<String>>();
			
			for(String runs2 : c.getStringList(att + ".runs")){
				runs.put(runs2, c.getStringList(att + ".run." + runs2));
			}
			HashMap<String, List<String>> spawns = new HashMap<String, List<String>>();
			
			for(String spawns2 : c.getStringList(att + ".spawns")){
				spawns.put(spawns2, c.getStringList(att + ".spawn." + spawns2));
			}
			
			attractie newAttractie = new attractie(
					att,
					c.getString(att + ".run_command"),
					c.getString(att + ".run_permissions"),
					c.getString(att + ".spawn_command"),
					c.getString(att + ".spawn_permissions"),
					spawns,
					runs
					);
			
			Attracties.put(att, newAttractie);
		}
	}
	public void removeAllAttracties()
	{
		for(attractie att : Attracties.values()){
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
