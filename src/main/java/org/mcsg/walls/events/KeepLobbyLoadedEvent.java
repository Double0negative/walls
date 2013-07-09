package org.mcsg.walls.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.mcsg.walls.LobbyManager;



public class KeepLobbyLoadedEvent implements Listener{
    
    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent e){
        LobbyManager.getInstance();
		if(LobbyManager.lobbychunks.contains(e.getChunk())){
            e.setCancelled(true);
        }
        //System.out.println("Chunk unloading");
    }

}
