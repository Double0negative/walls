package org.mcsg.walls.commands;

import org.bukkit.entity.Player;
import org.mcsg.walls.MessageManager;
import org.mcsg.walls.SettingsManager;
import org.mcsg.walls.MessageManager.PrefixType;

public class Teleport implements SubCommand{

	@Override
	public boolean onCommand(Player player, String[] args) {
		if(player.hasPermission(permission())){
			if(args.length == 1){
				try{
					int a = Integer.parseInt(args[0]);
					try{
						player.teleport(SettingsManager.getInstance().getSpawnPoint(a, 1));
					}catch(Exception e){
						MessageManager.getInstance().sendMessage(MessageManager.PrefixType.ERROR, "error.nospawns", player);
					}
				}catch(NumberFormatException e){
					MessageManager.getInstance().sendFMessage(PrefixType.ERROR, "error.notanumber", player, "input-" + args[0]);
				}
			}
			else{
				MessageManager.getInstance().sendFMessage(MessageManager.PrefixType.ERROR, "error.notspecified", player, "input-Game ID");
			}
		}
		else {
			MessageManager.getInstance().sendFMessage(PrefixType.WARNING, "error.nopermission", player);
		}
		return true;
	}

	@Override
	public String help(Player p) {
		return "/w tp <arenaid> - " + SettingsManager.getInstance().getMessageConfig().getString("messages.help.teleport","Teleport to an arena");
	}

	@Override
	public String permission() {
		return "walls.arena.teleport";
	}

}
