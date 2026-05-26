package me.lightning.extraweapons.events;

import me.lightning.extraweapons.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class PickupListener implements Listener {

    @EventHandler
    public void onPickup(PlayerAttemptPickupItemEvent e) {
        ItemStack item = e.getItem().getItemStack();

        Utils.updateItem(item);
    }
}
