package me.sumo.Commands;

import java.io.File;

import me.sumo.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.sumo.GUI.statsGUI;

public class statsCMD implements CommandExecutor {
	
	public File MessageFile = new File(Main.getInstance().getDataFolder() + File.separator + "message.yml");
	public YamlConfiguration MessageYaml = Main.getInstance().CM.GetLoadedYaml(MessageFile);
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		if (!p.hasPermission("sumo.stats")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.MessageYaml.getString("no-permission")));
    		return false;
    	}
		statsGUI.getInstance().open(p);
		return false;
	}

}
