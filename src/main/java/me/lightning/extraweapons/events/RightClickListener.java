package me.lightning.extraweapons.events;

import me.lightning.extraweapons.Utils;
import me.lightning.extraweapons.items.CustomItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class RightClickListener implements Listener {

    @EventHandler
    public void rightClick(PlayerInteractEvent e) {
        if (!e.getAction().isRightClick()) return;
        if (e.getHand() != EquipmentSlot.HAND) return;
        ItemStack held = e.getPlayer().getInventory().getItemInMainHand();
        if (!held.hasItemMeta() || held.getType().isAir()) return;

        Utils.updateItem(held);
        CustomItem item = Utils.getCustomItem(held);
        if (item == null) return;
        item.rightClick(e);
    }
}
