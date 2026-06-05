package me.lightning.extraweapons.events;

import me.lightning.extraweapons.ItemRegistry;
import me.lightning.extraweapons.Utils;
import me.lightning.extraweapons.items.MoltenEdge;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.inventory.ItemStack;

public class EntityCombustListener implements Listener {

    @EventHandler
    public void onCombust(EntityCombustEvent e) {
        if (!(e.getEntity() instanceof Player player)) return;
        ItemStack held = player.getInventory().getItemInMainHand();

        if (Utils.isItem(held, "molten_edge")) {
            ((MoltenEdge) ItemRegistry.get("molten_edge")).onCombust(e);
        }

    }
}
