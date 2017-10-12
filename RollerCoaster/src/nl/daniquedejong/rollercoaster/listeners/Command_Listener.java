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
package nl.daniquedejong.rollercoaster.listeners;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.daniquedejong.rollercoaster.Main;
import nl.daniquedejong.rollercoaster.Rollercoaster;

import net.md_5.bungee.api.ChatColor;

public class Command_Listener implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{
		if(cmd.getName().equalsIgnoreCase("rc"))
		{
			if(args.length == 1)
			{
				if(args[0].equalsIgnoreCase("reload"))
				{
					Main.r.ReloadConfig();
					sender.sendMessage(ChatColor.GREEN + "Reloading the configuration files... All carts are removed.");
					return false;
				}
			}
			if(args.length == 4)
			{
				if(Main.r.Attracties.containsKey(args[0]))
				{
					Rollercoaster Rollercoaster = Main.r.Attracties.get(args[0]);
					if(Rollercoaster.getRunCommand().equals(args[1]))
					{
						if(sender instanceof Player){
							if(!((Player)sender).hasPermission(Rollercoaster.getRunPermissions())) return false;
						}
						if(Rollercoaster.checkIfIdExists(Integer.parseInt(args[2]))){
							if(Rollercoaster.checkIfRunIdExists(args[3])){
								Rollercoaster.runRun(Integer.parseInt(args[2]), args[3]);
								sender.sendMessage(args[1] + " has been started");
							}else{
								sender.sendMessage(ChatColor.RED + "No valid run id given.");
							}
						}else{
							sender.sendMessage(ChatColor.RED + "This cart id doesn't exists.");
						}
						return false;
					}
					if(Rollercoaster.getSpawnCommand().equals(args[1]))
					{
						if(sender instanceof Player){
							if(!((Player)sender).hasPermission(Rollercoaster.getSpawnPermissions())) return false;
						}
						if(!Rollercoaster.checkIfIdExists(Integer.parseInt(args[2]))){
							if(Rollercoaster.checkIfSpawnIdExists(args[3])){
								Rollercoaster.runSpawn(Integer.parseInt(args[2]), args[3]);
								sender.sendMessage(args[1] + " has been spawned");
							}else{
								sender.sendMessage(ChatColor.RED + "No valid spawn id given.");
							}
						}else{
							sender.sendMessage(ChatColor.RED + "This cart id already exists.");
						}
						return false;
					}else{
						sender.sendMessage(ChatColor.RED + "This attraction doesn't exists.");
					}
				}
			}else{
				sender.sendMessage(ChatColor.RED + "Not all values given.");
			}
		}
		return false;
	}
}