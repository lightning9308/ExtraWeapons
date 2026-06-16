package me.lightning.extraweapons.events;

import me.lightning.extraweapons.armor.armorsets.ThunderSet;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {

        ThunderSet.onDeath(e);
    }
}
