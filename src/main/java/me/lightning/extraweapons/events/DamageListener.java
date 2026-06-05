package me.lightning.extraweapons.events;

import me.lightning.extraweapons.ItemRegistry;
import me.lightning.extraweapons.Utils;
import me.lightning.extraweapons.armors.CustomArmor;
import me.lightning.extraweapons.items.MoltenEdge;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class DamageListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player player)) return;

        for (EquipmentSlot slot : List.of(
                EquipmentSlot.HEAD,
                EquipmentSlot.CHEST,
                EquipmentSlot.LEGS,
                EquipmentSlot.FEET)) {

            ItemStack item = player.getEquipment().getItem(slot);

            CustomArmor armorPiece = Utils.getCustomArmor(item);
            if (armorPiece == null) continue;

            armorPiece.onDamage(e);

        }

        ItemStack held = player.getInventory().getItemInMainHand();

        if (Utils.isItem(held, "molten_edge")) {
            ((MoltenEdge) ItemRegistry.get("molten_edge")).onDamage(e);
        }
    }
}
