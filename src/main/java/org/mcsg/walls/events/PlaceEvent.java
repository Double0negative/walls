package org.mcsg.walls.events;

import java.util.ArrayList;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.mcsg.walls.Game;
import org.mcsg.walls.GameManager;
import org.mcsg.walls.SettingsManager;

public class PlaceEvent
  implements Listener
{
  public ArrayList<Integer> notallowedPlace = new ArrayList<Integer>();

  public PlaceEvent() {
    this.notallowedPlace.addAll(SettingsManager.getInstance().getConfig().getIntegerList("block.place.blacklist"));
  }
  @EventHandler(priority=EventPriority.HIGHEST)
  public void onBlockPlace(BlockPlaceEvent event) {
    Player p = event.getPlayer();
    int id = GameManager.getInstance().getPlayerGameId(p);

    if (id == -1) {
      int gameblockid = GameManager.getInstance().getBlockGameId(event.getBlock().getLocation());
      if ((gameblockid != -1) && 
        (GameManager.getInstance().getGame(gameblockid).getGameMode() != Game.GameMode.DISABLED)) {
        event.setCancelled(true);
      }

      return;
    }

    Game g = GameManager.getInstance().getGame(id);
    if (g.isPlayerinactive(p)) {
      return;
    }
    if (g.getMode() == Game.GameMode.DISABLED) {
      return;
    }
    if (g.getMode() != Game.GameMode.INGAME) {
      event.setCancelled(true);
      return;
    }

	if(id != GameManager.getInstance().getBlockGameId(event.getBlock().getLocation())){
		event.setCancelled(true);
		return;
	}
    if (this.notallowedPlace.contains(Integer.valueOf(event.getBlock().getTypeId())))
      event.setCancelled(true);
  }
}