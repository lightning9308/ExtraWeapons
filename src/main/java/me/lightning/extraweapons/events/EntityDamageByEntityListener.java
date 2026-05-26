package me.lightning.extraweapons.events;

import me.lightning.extraweapons.ArmorRegistry;
import me.lightning.extraweapons.Utils;
import me.lightning.extraweapons.armors.TitanChestPlate;
import me.lightning.extraweapons.items.CustomItem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class EntityDamageByEntityListener implements Listener {

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player player) || !(e.getEntity() instanceof LivingEntity entity)) return;

        // custom item
        ItemStack held = player.getInventory().getItemInMainHand();
        if (!held.getType().isAir() && held.hasItemMeta()) {
            CustomItem item = Utils.getCustomItem(held);
            if (item == null) return;
            item.onHit(e);
            Utils.updateItem(held);
        }

        // custom armor
        ItemStack chest = player.getInventory().getChestplate();
        if (chest != null && !chest.getType().isAir() && chest.hasItemMeta()) {
            if (Utils.isItem(chest, "titan_chestplate")) {
                ((TitanChestPlate) ArmorRegistry.get("titan_chestplate")).onHit(e);
            }
        }
    }
}
