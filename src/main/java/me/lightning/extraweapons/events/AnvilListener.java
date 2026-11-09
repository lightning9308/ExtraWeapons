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
        ItemStack first = e.getInventory().getFirstItem();
        ItemStack second = e.getInventory().getSecondItem();

        if (Utils.isCustom(first) && (second == null || second.getType() != Material.ENCHANTED_BOOK)) {
            e.setResult(ItemStack.of(Material.AIR));
        }
    }
}
