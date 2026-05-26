package me.lightning.extraweapons.events;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class ArmorChangeListener implements Listener {

    @EventHandler
    public void armorChange(PlayerArmorChangeEvent e) {
        ItemStack newItem = e.getNewItem();
        ItemStack oldItem = e.getOldItem();
    }
}

