package me.lightning.extraweapons.events;

import me.lightning.extraweapons.Utils;
import me.lightning.extraweapons.armor.CustomArmor;
import me.lightning.extraweapons.armor.armorsets.thunder.ThunderSet;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class SneakListener implements Listener {

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e) {
        Player player = e.getPlayer();

        for (EquipmentSlot slot : List.of(
                EquipmentSlot.HEAD,
                EquipmentSlot.CHEST,
                EquipmentSlot.LEGS,
                EquipmentSlot.FEET)) {

            ItemStack item = player.getEquipment().getItem(slot);

            CustomArmor armorPiece = Utils.getCustomArmor(item);
            if (armorPiece == null) continue;

            armorPiece.onSneak(e);

        }

        //armor sets
        if (Utils.fullSet(player, "thunder")) {
            ThunderSet.onSneak(e);
        }

    }
}