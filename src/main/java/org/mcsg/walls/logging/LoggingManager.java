package org.mcsg.walls.logging;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.mcsg.walls.Game;
import org.mcsg.walls.GameManager;
import org.mcsg.walls.Walls;

public class LoggingManager implements Listener {
	public static HashMap<String, Integer> i = new HashMap<String, Integer>();

	private static LoggingManager instance = new LoggingManager();

	private LoggingManager() {

		i.put("BCHANGE", 1);
		i.put("BPLACE", 1);
		i.put("BFADE", 1);
		i.put("BBLOW", 1);
		i.put("BSTARTFIRE", 1);
		i.put("BBURN", 1);
		i.put("BREDSTONE", 1);
		i.put("LDECAY", 1);
		i.put("BPISTION", 1);

	}

	public static LoggingManager getInstance() {
		return instance;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void blockChanged(BlockBreakEvent e) {
		if (e.isCancelled())
			return;
		logBlockDestoryed(e.getBlock());
		i.put("BCHANGE", i.get("BCHANGE") + 1);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void blockChanged(BlockPlaceEvent e) {
		if (e.isCancelled())
			return;

		logBlockCreated(e.getBlock());
		i.put("BPLACE", i.get("BPLACE") + 1);

	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void blockChanged(BlockFadeEvent e) {
		if (e.isCancelled())
			return;

		logBlockDestoryed(e.getBlock());
		i.put("BFADE", i.get("BFADE") + 1);

	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void blockChange(EntityExplodeEvent e) {
		if (e.isCancelled())
			return;

		for (Block b : e.blockList()) {
			logBlockDestoryed(b);
		}

		i.put("BBLOW", i.get("BBLOW") + 1);

	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void blockChange(BlockIgniteEvent e) {
		if (e.isCancelled())
			return;

		logBlockCreated(e.getBlock());
		i.put("BSTARTFIRE", i.get("BSTARTFIRE") + 1);

	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void blockChanged(BlockBurnEvent e) {
		if (e.isCancelled())
			return;

		logBlockDestoryed(e.getBlock());
		i.put("BBURN", i.get("BBURN") + 1);


	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void blockChanged(BlockGrowEvent e) {
		if (e.isCancelled())
			return;

		logBlockCreated(e.getBlock());

	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void blockChanged(BlockFormEvent e) {
		logBlockCreated(e.getBlock());
	}


	@EventHandler(priority = EventPriority.MONITOR)
	public void blockChanged(LeavesDecayEvent e) {
		if (e.isCancelled())
			return;

		logBlockDestoryed(e.getBlock());
		i.put("LDECAY", i.get("LDECAY") + 1);


	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void blockChange(BlockPistonExtendEvent e) {
		if (e.isCancelled())
			return;

		for (Block b : e.getBlocks()) {
			logBlockCreated(b);
		}
		i.put("BPISTION", i.get("BPISTION") + 1);

	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void blockChange(PlayerInteractEvent e) {
		if (e.isCancelled())
			return;
		if(e.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		Block b = e.getClickedBlock();
		if(b.getType() != Material.CHEST)
			return;
		logBlockDestoryed(b);
	}

	public void logBlockCreated(Block b) {
		Walls.debug("Logging created: "+b+" ID: "+b.getTypeId());
		if (GameManager.getInstance().getBlockGameId(b.getLocation()) == -1)
			return;
		if (GameManager.getInstance().getGameMode(
				GameManager.getInstance().getBlockGameId(b.getLocation())) == Game.GameMode.DISABLED)
			return;

		QueueManager.getInstance().add(
				new BlockData(GameManager.getInstance().getBlockGameId(
						b.getLocation()), b.getWorld().getName(), 0, (byte) 0,
						b.getTypeId(), b.getData(), b.getX(), b.getY(), b
								.getZ(), null));
	}

	public void logBlockDestoryed(Block b) {
		Walls.debug("Logging destroyed: "+b+" ID: "+b.getTypeId());
		ItemStack[] stack = null;
		if (GameManager.getInstance().getBlockGameId(b.getLocation()) == -1)
			return;
		if (GameManager.getInstance().getGameMode(
				GameManager.getInstance().getBlockGameId(b.getLocation())) == Game.GameMode.DISABLED)
			return;
		if (b.getTypeId() == 51)
			return;
		if (b.getType() == Material.CHEST)
			stack = ((Chest) b.getState()).getBlockInventory().getContents();
		QueueManager.getInstance().add(
				new BlockData(GameManager.getInstance().getBlockGameId(
						b.getLocation()), b.getWorld().getName(),
						b.getTypeId(), b.getData(), 0, (byte) 0, b.getX(), b
								.getY(), b.getZ(), stack));
	}

}


