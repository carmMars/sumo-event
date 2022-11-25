package me.sumo;

import java.io.File;

import me.sumo.BungeeCord.BungeeListener;
import me.sumo.Scoreboard.Scoreboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.sumo.Commands.countdownCMD;
import me.sumo.Commands.pingCMD;
import me.sumo.Commands.settingsCMD;
import me.sumo.Commands.startCMD;
import me.sumo.Commands.statsCMD;
import me.sumo.Commands.whitelistCMD;
import me.sumo.GUI.spectateTeleportGUI;
import me.sumo.GUI.statsGUI;
import me.sumo.Game.GameData;
import me.sumo.Game.GameManager;
import me.sumo.Game.Spectate;
import me.sumo.Listeners.CPSChecker;
import me.sumo.Listeners.ClickInventoryEvent;
import me.sumo.Listeners.DropItemListener;
import me.sumo.Listeners.GUIAntiMove;
import me.sumo.Listeners.LeaveItemRightClick;
import me.sumo.Listeners.NoBlockBreakAndPlaceListener;
import me.sumo.Listeners.NoDamage;
import me.sumo.Listeners.NoHungerLoseListener;
import me.sumo.Listeners.PlayerJoinListener;
import me.sumo.Listeners.PlayerLeaveListener;
import me.sumo.Listeners.PlayerPreLoginEvent;
import me.sumo.Listeners.motd;
import me.sumo.api.PlaceHolderAPIHook;
import me.sumo.utils.YamlManager;

public class Main extends JavaPlugin{
	
	private static Main instance;
  	public String nmsVersion;
  	
  	//data.yml
  	public YamlManager CM = new YamlManager();
    private File yamlpath;
    private String YamlName;

    public void onEnable() {
    	instance = this;
    	loadData();
    	loadMessage();
    	loadConfig();
    	loadLocation();
    	updateScoreboard();
    	loadServerVersion();
    	registerListeners();
    	registerCommands();
    	registerNewClass();
    	sumoWorldFix();
    	HookPlugins();
    	this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    	this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeListener());
    	Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "[" + ChatColor.BOLD + "Sumo" + ChatColor.YELLOW + "] " + ChatColor.BOLD + "Sumo Plugin ");
    	Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "[" + ChatColor.BOLD + "Sumo" + ChatColor.YELLOW + "] " + ChatColor.RED + " ");
    	Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "[" + ChatColor.BOLD + "Sumo" + ChatColor.YELLOW + "] " + ChatColor.YELLOW + "Version: " + getDescription().getVersion());
    	Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "[" + ChatColor.BOLD + "Sumo" + ChatColor.YELLOW + "] " + ChatColor.YELLOW + "Author: CARTERHH");
    }
    
	public static Main getInstance() {
  		return instance;
  	}
	
  	public String getNmsVersion() {
		return nmsVersion;
	}	
  	
	public void loadServerVersion() {
		nmsVersion = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
	}
	
	public void registerNewClass() {
		new GameData();
    	new statsGUI();
    	new spectateTeleportGUI();
	}
	
	public void registerListeners() {
		File ConfigFile = new File(Main.getInstance().getDataFolder() + File.separator + "configuration.yml");
		YamlConfiguration ConfigYaml = Main.getInstance().CM.GetLoadedYaml(ConfigFile);
		Bukkit.getPluginManager().registerEvents(new GameManager(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerLeaveListener(), this);
		Bukkit.getPluginManager().registerEvents(new NoBlockBreakAndPlaceListener(), this);
		Bukkit.getPluginManager().registerEvents(new NoHungerLoseListener(), this);
		Bukkit.getPluginManager().registerEvents(new NoDamage(), this);
		Bukkit.getPluginManager().registerEvents(new CPSChecker(), this);
		Bukkit.getPluginManager().registerEvents(new GUIAntiMove(), this);
		Bukkit.getPluginManager().registerEvents(new LeaveItemRightClick(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerPreLoginEvent(), this);
		Bukkit.getPluginManager().registerEvents(new DropItemListener(), this);
		Bukkit.getPluginManager().registerEvents(new Spectate(), this);
		Bukkit.getPluginManager().registerEvents(new ClickInventoryEvent(), this);
		if (ConfigYaml.getBoolean("motd.enable")) {
			Bukkit.getPluginManager().registerEvents(new motd(), this);
		}
	}
	
	public void registerCommands() {
		getCommand("settings").setExecutor(new settingsCMD(this));
		getCommand("start").setExecutor(new startCMD());
		getCommand("ping").setExecutor(new pingCMD());
		getCommand("countdown").setExecutor(new countdownCMD());
		getCommand("stats").setExecutor(new statsCMD());
		getCommand("wl").setExecutor(new whitelistCMD());
	}
	
	public void sumoWorldFix() {
		File ConfigFile = new File(Main.getInstance().getDataFolder() + File.separator + "configuration.yml");
		YamlConfiguration ConfigYaml = Main.getInstance().CM.GetLoadedYaml(ConfigFile);
		World w = Bukkit.getWorld(ConfigYaml.getString("event-world-name"));
		w.setGameRuleValue("doDaylightCycle", "false");
		w.setGameRuleValue("doMobSpawning", "false");
		w.setGameRuleValue("mobGriefing", "false");
		new BukkitRunnable() {
			public void run() {			
				w.setTime(6000);
			}
		}.runTaskTimer(Main.getInstance(), 0L, 1200L);
	}
	
	public void updateScoreboard() {
  		new BukkitRunnable() {
            @Override
            public void run() {
                for(Player p : Bukkit.getServer().getOnlinePlayers()) {
                	Scoreboard.updateScoreboard(p);
                }  
            }
        }.runTaskTimer(this, 20L, 20L);
  	}
	
	//send message to console if the plugin is enabled
	@SuppressWarnings("deprecation") //For placeholderapi
	public void HookPlugins() {
		Bukkit.getConsoleSender().sendMessage(" ");
	    if (Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
	    	new PlaceHolderAPIHook().register();
	    	Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "PlaceholderAPI §§ahas been hooked!");
	    }
		Bukkit.getConsoleSender().sendMessage(" ");		
	}
    
    public void loadData() {
		Bukkit.getConsoleSender().sendMessage(" ");
	    Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Loading data.yml...");
		CM = new YamlManager();
		yamlpath = new File(this.getDataFolder() + File.separator + "data.yml");
		YamlName = "data.yml";
		CM.SetupMConfig(yamlpath, YamlName);
		CM.reloadMConfig(yamlpath, YamlName);
	}
    
    public void loadMessage() {
		Bukkit.getConsoleSender().sendMessage(" ");
	    Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Loading message.yml...");
		CM = new YamlManager();
		yamlpath = new File(this.getDataFolder() + File.separator + "message.yml");
		YamlName = "data.yml";
		CM.SetupMConfig(yamlpath, YamlName);
		CM.reloadMConfig(yamlpath, YamlName);
	}
    
    public void loadConfig() {
		Bukkit.getConsoleSender().sendMessage(" ");
	    Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Loading configuration.yml...");
		CM = new YamlManager();
		yamlpath = new File(this.getDataFolder() + File.separator + "configuration.yml");
		YamlName = "configuration.yml";
		CM.SetupMConfig(yamlpath, YamlName);
		CM.reloadMConfig(yamlpath, YamlName);
	}
    
    public void loadLocation() {
		Bukkit.getConsoleSender().sendMessage(" ");
	    Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Loading location.yml...");
		CM = new YamlManager();
		yamlpath = new File(this.getDataFolder() + File.separator + "location.yml");
		YamlName = "location.yml";
		CM.SetupMConfig(yamlpath, YamlName);
		CM.reloadMConfig(yamlpath, YamlName);
	}

}
