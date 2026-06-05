package me.lightning.extraweapons.armors;

import me.lightning.extraweapons.ItemBuilder;
import me.lightning.extraweapons.Keys;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class MoltenLeggings extends CustomArmor {

    double defence = config.getDouble("armor.molten_leggings.defence");

    @Override
    public String getID() {
        return "molten_leggings";
    }

    @Override
    public ItemStack createItem() {
        return new ItemBuilder(Material.LEATHER_LEGGINGS)
                .itemName(Component.text("Molten Leggings")
                        .decoration(TextDecoration.BOLD,true)
                        .color(TextColor.color(187, 80, 24)))
                .color(Color.fromRGB(165, 77, 23))
                .attribute(Attribute.GENERIC_ARMOR, new AttributeModifier(Keys.stats(getID()),defence, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS))
                .attribute(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(Keys.stats(getID()),3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS))
                .flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ADDITIONAL_TOOLTIP, ItemFlag.HIDE_DYE)
                .maxDamage(400)
                .lore(
                    Component.text("§7§l──────────"),
                    Component.text("§7Defence: §2+%s".formatted(defence).replace(".0","")),
                    Component.empty(),
                    Component.text("§6§lPASSIVE"),
                    Component.text("§7Hits burn enemies while on fire."),
                    Component.text("§7Fire ticks last 50% longer."))
                .build();
    }

    @Override
    public EquipmentSlot getType() {
        return EquipmentSlot.LEGS;
    }

    public void onCombust(EntityCombustEvent e) {
        e.setDuration((float) (e.getDuration() * 1.5));
    }


    public void onHit(EntityDamageByEntityEvent e) {
        Player player = (Player) e.getDamager();
        LivingEntity entity = (LivingEntity) e.getEntity();

        if (player.getFireTicks() > 0) {
            entity.setFireTicks(100);
        }
    }
}
