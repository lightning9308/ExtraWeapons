package me.lightning.extraweapons.events;

import me.lightning.extraweapons.ArmorRegistry;
import me.lightning.extraweapons.ItemRegistry;
import me.lightning.extraweapons.Utils;
import me.lightning.extraweapons.armors.MoltenLeggings;
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
        ItemStack legs = player.getInventory().getLeggings();

        if (Utils.isItem(held, "molten_edge")) {
            ((MoltenEdge) ItemRegistry.get("molten_edge")).onCombust(e);
        }

        if (Utils.isItem(legs, "molten_leggings")) {
            ((MoltenLeggings) ArmorRegistry.get("molten_leggings")).onCombust(e);
        }
    }
}
