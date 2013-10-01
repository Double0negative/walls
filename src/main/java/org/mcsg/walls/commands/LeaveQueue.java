package org.mcsg.walls.commands;

import org.bukkit.entity.Player;
import org.mcsg.walls.GameManager;
import org.mcsg.walls.SettingsManager;



public class LeaveQueue implements SubCommand{

    @Override
    public boolean onCommand(Player player, String[] args) {
        GameManager.getInstance().removeFromOtherQueues(player, -1);
        return true;
    }

    @Override
    public String help(Player p) {
        return "/w lq - " + SettingsManager.getInstance().getMessageConfig().getString("messages.help.leavequeue", "Leave the queue for any queued games");
    }

	@Override
	public String permission() {
		return null;
	}

}
