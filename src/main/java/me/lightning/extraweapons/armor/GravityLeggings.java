package me.lightning.extraweapons.armor;

import me.lightning.extraweapons.ItemBuilder;
import me.lightning.extraweapons.Keys;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class GravityLeggings extends CustomArmor {

    double defense = config.getDouble("armor.gravity_leggings.defense", 6);
    double kbResistance = config.getDouble("armor.gravity_leggings.knockback_resistance", 0.5);
    double fallSpeed = config.getDouble("armor.gravity_leggings.fall_speed", 0.25);

    @Override
    public String getID() {
        return "gravity_leggings";
    }

    @Override
    public ItemStack createItem() {
        return new ItemBuilder(Material.LEATHER_LEGGINGS)
                .itemName(Component.text("Gravity Leggings")
                        .decoration(TextDecoration.BOLD, true)
                        .color(TextColor.color(69, 123, 180)))
                .color(Color.fromRGB(69, 123, 180))
                .flags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DYE)
                .attribute(Attribute.GENERIC_ARMOR, new AttributeModifier(Keys.stats(getID()), defense, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS))
                .attribute(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(Keys.stats(getID()), 3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS))
                .attribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE, new AttributeModifier(Keys.stats(getID()), kbResistance, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS))
                .attribute(Attribute.GENERIC_GRAVITY, new AttributeModifier(Keys.stats(getID()), fallSpeed, AttributeModifier.Operation.MULTIPLY_SCALAR_1, EquipmentSlotGroup.LEGS))
                .maxDamage(400)
                .lore(
                        Component.text("§7§l──────────"),
                        Component.text("§7Defense: §2+%s".formatted(defense).replace(".0","")),
                        Component.empty(),
                        Component.text("§6§lPASSIVE"),
                        Component.text("§7Take %s%% less knockback.".formatted(kbResistance * 100).replace(".0","")),
                        Component.text("§7Fall %s%% faster.".formatted(fallSpeed * 100).replace(".0","")))
                .build();
    }

    @Override
    public EquipmentSlot getType() {
        return EquipmentSlot.LEGS;
    }
}