package me.abandoncaptian.MyMOTD;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin implements Listener{

	Logger myPluginLogger = Bukkit.getLogger();
	MyConfigManager manager;
	MyConfig Config;
	
	@EventHandler
	public void onServerPing(ServerListPingEvent e){
		String motd = Config.getString("Message");
		motd = motd.replaceAll("&", "\u00A7");
		e.setMotd(motd);
	}

	@Override
	public void onEnable()
	{
		myPluginLogger.info("My MOTD Enabled");
		getServer().getPluginManager().registerEvents(this, this);
		manager = new MyConfigManager(this);
		Config = manager.getNewConfig("MOTD.yml", new String[] {"My MOTD Config File"});
		if(!Config.contains("Message")){
			Config.set("Message", "&9&lDisplay Message");
			Config.saveConfig();
		}
	}

	@Override
	public void onDisable()
	{
		myPluginLogger.info("My MOTD Disabled");
	}

	public boolean onCommand(CommandSender theSender, Command cmd, String commandLabel,String[] args)
	{
		if(commandLabel.equalsIgnoreCase("motd-reload")){
			if(!Config.contains("Message")){
				Config.set("Message", "&9&lDisplay Message");
				Config.saveConfig();
			}
			Config.reloadConfig();
			theSender.sendMessage(ChatColor.GREEN + "MyMOTD Reloaded!");
		}
		if(commandLabel.equalsIgnoreCase("motd-set")){
			StringBuilder str = new StringBuilder();
			for(int i = 0; i < args.length; i++){
				str.append(args[i] + " ");
			}
			String motd = str.toString();
			Config.set("Message", motd);
			Config.saveConfig();
			Config.reloadConfig();
			String motdset = Config.getString("Message");
			motdset = motdset.replaceAll("&", "§");
			theSender.sendMessage(ChatColor.GREEN + "MyMOTD set to " + ChatColor.RESET + motdset);
		}
		return  true;
	}
}
