package me.lightning.extraweapons.events;

import me.lightning.extraweapons.armor.PhotonVisor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class playerQuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        PhotonVisor.cleanUp(e.getPlayer());
    }
}
