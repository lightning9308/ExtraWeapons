package me.lightning.extraweapons.events;

import me.lightning.extraweapons.Utils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;

public class AnvilListener implements Listener {

    @EventHandler
    public void anvil(PrepareAnvilEvent e) {
        if (Utils.isCustom(e.getInventory().getFirstItem())) {
            e.setResult(ItemStack.of(Material.AIR));
        }
    }
}
