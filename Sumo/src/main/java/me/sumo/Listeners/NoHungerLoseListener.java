package me.sumo.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class NoHungerLoseListener implements Listener {
	
	@EventHandler
	public void noLoseHunger(FoodLevelChangeEvent e) {
		e.setFoodLevel(20);
	}

}
