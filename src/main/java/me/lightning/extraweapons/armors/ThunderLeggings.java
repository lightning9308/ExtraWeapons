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
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;

public class ThunderLeggings extends CustomArmor {

    double defense = config.getDouble("armor.thunder_leggings.defense");

    @Override
    public String getID() {
        return "thunder_leggings";
    }

    @Override
    public ItemStack createItem() {
        return new ItemBuilder(Material.LEATHER_LEGGINGS)
                .itemName(Component.text("Thunder Leggings")
                        .decoration(TextDecoration.BOLD, true)
                        .color(TextColor.color(0, 59, 155)))
                .color(Color.fromRGB(0, 59, 155))
                .armorTrim(new ArmorTrim(TrimMaterial.LAPIS, TrimPattern.FLOW))
                .maxDamage(500)
                .attribute(Attribute.GENERIC_ARMOR, new AttributeModifier(Keys.stats(getID()), defense, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS))
                .attribute(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(Keys.stats(getID()),3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.LEGS))
                .flags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DYE, ItemFlag.HIDE_ARMOR_TRIM)
                .lore(
                        Component.text("§7§l──────────"),
                        Component.text("§7Defense: §2+%s".formatted(defense).replace(".0","")),
                        Component.empty(),
                        Component.text("§6§lFULL SET BONUS"),
                        Component.text("§7Landing hits build Voltage. (1/s)"),
                        Component.text("§7Sneak at max Voltage to"),
                        Component.text("§7become Overcharged for 5s."))
                .build();
    }

    @Override
    public EquipmentSlot getType() {
        return EquipmentSlot.LEGS;
    }
}
