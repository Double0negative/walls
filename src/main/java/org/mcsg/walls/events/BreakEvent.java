package org.mcsg.walls.events;

import java.util.ArrayList;


import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.mcsg.walls.Game;
import org.mcsg.walls.GameManager;
import org.mcsg.walls.SettingsManager;







public class BreakEvent implements Listener
{
	public ArrayList<Integer> notallowedBreak = new ArrayList<Integer>();

	public BreakEvent() {
		this.notallowedBreak.addAll(SettingsManager.getInstance().getConfig().getIntegerList("block.break.whitelist"));
	}
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event) {
		Player p = event.getPlayer();
		int pid = GameManager.getInstance().getPlayerGameId(p);

		if (pid == -1) {
			int blockgameid = GameManager.getInstance().getBlockGameId(event.getBlock().getLocation());

			if ((blockgameid != -1) && 
					(GameManager.getInstance().getGame(blockgameid).getGameMode() != Game.GameMode.DISABLED)) {
				event.setCancelled(true);
			}

			return;
		}

		if(GameManager.getInstance().isOnWall(event.getBlock().getLocation())){
			event.setCancelled(true);
			return;
		}

		Game g = GameManager.getInstance().getGame(pid);

		if (g.getMode() == Game.GameMode.DISABLED) {
			return;
		}
		if (g.getMode() != Game.GameMode.INGAME) {
			event.setCancelled(true);
			return;
		}
		if(pid != GameManager.getInstance().getBlockGameId(event.getBlock().getLocation())){
			event.setCancelled(true);
			return;
		}

		if (this.notallowedBreak.contains(Integer.valueOf(event.getBlock().getTypeId()))) event.setCancelled(true);
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	public void onBlockBreak(EntityExplodeEvent event) {
		for(Block b:event.blockList().toArray(new Block[0])){
			Location l = b.getLocation();
			int g = GameManager.getInstance().getBlockGameId(l);
			if(g == -1){
				return;
			}
			else{
				if(GameManager.getInstance().getGame(g).isOnWall(l)){
					event.blockList().remove(b);
				}
			}
			if(b.getState() instanceof Chest){
				/*Chest c = (Chest)b.getState();
				QueueManager.getInstance().addChest(g, c, c.getBlockInventory().getContents().clone());*/
				event.blockList().remove(b);
			}
		}
	}
}