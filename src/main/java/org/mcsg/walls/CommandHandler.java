package org.mcsg.walls;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.mcsg.walls.MessageManager.PrefixType;
import org.mcsg.walls.commands.AddLobbyWall;
import org.mcsg.walls.commands.AddWall;
import org.mcsg.walls.commands.CreateArena;
import org.mcsg.walls.commands.DelArena;
import org.mcsg.walls.commands.Disable;
import org.mcsg.walls.commands.Enable;
import org.mcsg.walls.commands.Flag;
import org.mcsg.walls.commands.ForceStart;
import org.mcsg.walls.commands.Join;
import org.mcsg.walls.commands.Leave;
import org.mcsg.walls.commands.LeaveQueue;
import org.mcsg.walls.commands.ListArenas;
import org.mcsg.walls.commands.ListPlayers;
import org.mcsg.walls.commands.Reload;
import org.mcsg.walls.commands.ResetSpawns;
import org.mcsg.walls.commands.SetLobbySpawn;
import org.mcsg.walls.commands.SetLobbyWall;
import org.mcsg.walls.commands.SetSpawn;
import org.mcsg.walls.commands.Spectate;
import org.mcsg.walls.commands.SubCommand;
import org.mcsg.walls.commands.Teleport;
import org.mcsg.walls.commands.Vote;



public class CommandHandler implements CommandExecutor {
	private Plugin plugin;
	private HashMap < String, SubCommand > commands;
	private HashMap < String, Integer > helpinfo;
	private MessageManager msgmgr = MessageManager.getInstance();
	public CommandHandler(Plugin plugin) {
		this.plugin = plugin;
		commands = new HashMap < String, SubCommand > ();
		helpinfo = new HashMap < String, Integer > ();
		loadCommands();
		loadHelpInfo();
	}

	private void loadCommands() {
		commands.put("createarena", new CreateArena());
		commands.put("join", new Join());
		commands.put("addwall", new AddWall());
		commands.put("setspawn", new SetSpawn());
		commands.put("getcount", new ListArenas());
		commands.put("disable", new Disable());
		commands.put("start", new ForceStart());
		commands.put("enable", new Enable());
		commands.put("vote", new Vote());
		commands.put("leave", new Leave());
		commands.put("setlobbyspawn", new SetLobbySpawn());
		commands.put("setlobbywall", new SetLobbyWall());
		commands.put("resetspawns", new ResetSpawns());
		commands.put("delarena", new DelArena());
		commands.put("flag", new Flag());
		commands.put("spectate", new Spectate());
		commands.put("lq", new LeaveQueue());
		commands.put("leavequeue", new LeaveQueue());
		commands.put("list", new ListPlayers());
		commands.put("tp", new Teleport());
		commands.put("reload", new Reload());
		commands.put("addlobbywall", new AddLobbyWall());
		// commands.put("sponsor", new Sponsor());
	}

	private void loadHelpInfo() {
		//you can do this by iterating thru the hashmap from a certian index btw instead of using a new hashmap,
		//plus, instead of doing three differnet ifs, just iterate thru and check if the value == the page
		helpinfo.put("createarena", 3);
		helpinfo.put("join", 1);
		helpinfo.put("addwall", 3);
		helpinfo.put("setspawn", 3);
		helpinfo.put("getcount", 3);
		helpinfo.put("disable", 2);
		helpinfo.put("start", 2);
		helpinfo.put("enable", 2);
		helpinfo.put("vote", 1);
		helpinfo.put("leave", 1);
		helpinfo.put("setlobbyspawn", 3);
		helpinfo.put("resetspawns", 3);
		helpinfo.put("delarena", 3);
		helpinfo.put("flag", 3);
		helpinfo.put("spectate", 1);
		helpinfo.put("lq", 1);
		helpinfo.put("leavequeue", 1);
		helpinfo.put("list", 1);
		commands.put("reload", new Reload());

		//helpinfo.put("sponsor", 1);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd1, String commandLabel, String[] args) {
		PluginDescriptionFile pdfFile = plugin.getDescription();
		if (!(sender instanceof Player)) {
			msgmgr.logMessage(PrefixType.WARNING, "Only in-game players can use SurvivalGames commands! ");
			return true;
		}

		Player player = (Player) sender;

		if (Walls.config_todate == false) {
			msgmgr.sendMessage(PrefixType.WARNING, "The config file is out of date. Please tell an administrator to reset the config.", player);
			return true;
		}

		if (Walls.dbcon == false) {
			msgmgr.sendMessage(PrefixType.WARNING, "Could not connect to server. Plugin disabled.", player);
			return true;
		}

		if (cmd1.getName().equalsIgnoreCase("walls")) {
			if (args == null || args.length < 1) {
				msgmgr.sendMessage(PrefixType.INFO, "Version " + pdfFile.getVersion() + " by Double0negative", player);
				msgmgr.sendMessage(PrefixType.INFO, "Type /walls help <player | staff | admin> for command information", player);
				return true;
			}
			if (args[0].equalsIgnoreCase("help")) {
				if (args.length == 1) {
					help(player, 1);
				}
				else {
					if (args[1].toLowerCase().startsWith("player")) {
						help(player, 1);
						return true;
					}
					if (args[1].toLowerCase().startsWith("staff")) {
						help(player, 2);
						return true;
					}
					if (args[1].toLowerCase().startsWith("admin")) {
						help(player, 3);
						return true;
					}
					else {
						msgmgr.sendMessage(PrefixType.WARNING, args[1] + " is not a valid page! Valid pages are Player, Staff, and Admin.", player);
					}
				}
				return true;
			}
			String sub = args[0];
			Vector < String > l = new Vector < String > ();
			l.addAll(Arrays.asList(args));
			l.remove(0);
			args = (String[]) l.toArray(new String[0]);
			if (!commands.containsKey(sub)) {
				msgmgr.sendMessage(PrefixType.WARNING, "Command doesn't exist.", player);
				msgmgr.sendMessage(PrefixType.INFO, "Type /sg help for command information", player);
				return true;
			}
			try {
				commands.get(sub).onCommand(player, args);
			} catch (Exception e) {
				e.printStackTrace();
				msgmgr.sendFMessage(PrefixType.ERROR, "error.command", player, "command-["+sub+"] "+Arrays.toString(args));
				msgmgr.sendMessage(PrefixType.INFO, "Type /sg help for command information", player);
			}
			return true;
		}
		return false;
	}

	public void help (Player p, int page) {
		if (page == 1) {
			p.sendMessage(ChatColor.BLUE + "------------ " + msgmgr.pre + ChatColor.DARK_AQUA + " Player Commands" + ChatColor.BLUE + " ------------");
		}
		if (page == 2) {
			p.sendMessage(ChatColor.BLUE + "------------ " + msgmgr.pre + ChatColor.DARK_AQUA + " Staff Commands" + ChatColor.BLUE + " ------------");
		}
		if (page == 3) {
			p.sendMessage(ChatColor.BLUE + "------------ " + msgmgr.pre + ChatColor.DARK_AQUA + " Admin Commands" + ChatColor.BLUE + " ------------");
		}

		for (String command : commands.keySet()) {
			try{
				if (helpinfo.get(command) == page) {

					msgmgr.sendMessage(PrefixType.INFO, commands.get(command).help(p), p);
				}
			}catch(Exception e){}
		}
		/*for (SubCommand v : commands.values()) {
            if (v.permission() != null) {
                if (p.hasPermission(v.permission())) {
                    msgmgr.sendMessage(PrefixType.INFO1, v.help(p), p);
                } else {
                    msgmgr.sendMessage(PrefixType.WARNING, v.help(p), p);
                }
            } else {
                msgmgr.sendMessage(PrefixType.INFO, v.help(p), p);
            }
        }*/
	}
}