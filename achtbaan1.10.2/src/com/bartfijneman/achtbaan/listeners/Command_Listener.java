package com.bartfijneman.achtbaan.listeners;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.bartfijneman.achtbaan.Main;
import com.bartfijneman.achtbaan.attractie;

import net.md_5.bungee.api.ChatColor;

public class Command_Listener implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{
		if(cmd.getName().equalsIgnoreCase("achtbaan"))
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
					attractie Attractie = Main.r.Attracties.get(args[0]);
					if(Attractie.getRunCommand().equals(args[1]))
					{
						if(sender instanceof Player){
							if(!((Player)sender).hasPermission(Attractie.getRunPermissions())) return false;
						}
						if(Attractie.checkIfIdExists(Integer.parseInt(args[2]))){
							if(Attractie.checkIfRunIdExists(args[3])){
								Attractie.runRun(Integer.parseInt(args[2]), args[3]);
							}else{
								sender.sendMessage(ChatColor.RED + "No valid run id given.");
							}
						}else{
							sender.sendMessage(ChatColor.RED + "This cart id doesn't exists.");
						}
						return false;
					}
					if(Attractie.getSpawnCommand().equals(args[1]))
					{
						if(sender instanceof Player){
							if(!((Player)sender).hasPermission(Attractie.getSpawnPermissions())) return false;
						}
						if(!Attractie.checkIfIdExists(Integer.parseInt(args[2]))){
							if(Attractie.checkIfSpawnIdExists(args[3])){
								Attractie.runSpawn(Integer.parseInt(args[2]), args[3]);
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