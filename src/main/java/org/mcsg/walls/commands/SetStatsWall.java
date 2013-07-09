package org.mcsg.walls.commands;

import org.bukkit.entity.Player;
import org.mcsg.walls.SettingsManager;



public class SetStatsWall implements SubCommand {

    @Override
    public boolean onCommand(Player player, String[] args) {
        //StatsWallManager.getInstance().setStatsSignsFromSelection(player);
        return false;
    }

    
    public String help(Player p){
        return "/sg setstatswall - "+ SettingsManager.getInstance().getMessageConfig().getString("messages.help.setstatswall", "Sets the stats wall");
    }

	@Override
	public String permission() {
		return null;
	}
}