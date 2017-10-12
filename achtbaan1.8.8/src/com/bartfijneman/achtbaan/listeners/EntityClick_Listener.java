package com.bartfijneman.achtbaan.listeners;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import com.bartfijneman.achtbaan.Main;
import com.bartfijneman.achtbaan.attractie;

public class EntityClick_Listener implements Listener 
{
	@EventHandler
	public void EntityClick(PlayerInteractAtEntityEvent e)
	{
		Player p = e.getPlayer();
		if(e.getRightClicked() instanceof ArmorStand){
			ArmorStand clickedA = (ArmorStand) e.getRightClicked();
			if(clickedA.getPassenger() == null){
				for(attractie att : Main.r.Attracties.values()){
					for(ArmorStand a : att.getSeats())
					{
						if(a.getLocation().getX() == clickedA.getLocation().getX() && 
								a.getLocation().getY() == clickedA.getLocation().getY() && 
								a.getLocation().getZ() == clickedA.getLocation().getZ())
						{
							if(att.allowSit == true){
								e.setCancelled(true);
								p.teleport(clickedA);
								clickedA.setPassenger(p);
								att.addPassagier(p, clickedA);
							}
							return;
						}
					}
				}
			}
		}
	}
	@EventHandler
	public void ArmorStandGreef(PlayerArmorStandManipulateEvent e){
		if(e.getRightClicked() instanceof ArmorStand){
			ArmorStand clickedA = (ArmorStand) e.getRightClicked();
			if(clickedA.getPassenger() == null){
				ArrayList<Location> all_main_seats = new ArrayList<Location>();
				for(attractie att : Main.r.Attracties.values()){
					for(ArmorStand a : att.getMainSeats())
					{
						all_main_seats.add(a.getLocation());
					}
				}
				if(all_main_seats.contains(clickedA.getLocation())){
					e.setCancelled(true);
				}
			}
		}
	}
}
