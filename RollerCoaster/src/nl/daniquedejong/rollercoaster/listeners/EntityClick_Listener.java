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

import java.util.ArrayList;

import nl.daniquedejong.rollercoaster.Main;
import nl.daniquedejong.rollercoaster.Rollercoaster;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class EntityClick_Listener implements Listener 
{
	@EventHandler
	public void EntityClick(PlayerInteractAtEntityEvent e)
	{
		Player p = e.getPlayer();
		if(e.getRightClicked() instanceof ArmorStand){
			ArmorStand clickedA = (ArmorStand) e.getRightClicked();
			if(clickedA.getPassenger() == null){
				for(Rollercoaster att : Main.r.Attracties.values()){
					if(att.getSeats().contains(clickedA)){
						if(att.allowSit != false){
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
	@EventHandler
	public void ArmorStandGreef(PlayerArmorStandManipulateEvent e){
		if(e.getRightClicked() instanceof ArmorStand){
			ArmorStand clickedA = (ArmorStand) e.getRightClicked();
			if(clickedA.getPassenger() == null){
				ArrayList<ArmorStand> all_main_seats = new ArrayList<ArmorStand>();
				for(Rollercoaster att : Main.r.Attracties.values()){
					all_main_seats.addAll(att.getMainSeats());
				}
				if(all_main_seats.contains(clickedA)){
					e.setCancelled(true);
				}
			}
		}
	}
}
