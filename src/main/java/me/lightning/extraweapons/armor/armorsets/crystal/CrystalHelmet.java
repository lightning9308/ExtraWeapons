package me.lightning.extraweapons.armor.armorsets.crystal;

import me.lightning.extraweapons.ItemBuilder;
import me.lightning.extraweapons.Keys;
import me.lightning.extraweapons.armor.CustomArmor;
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

public class CrystalHelmet extends CustomArmor {

    double defense = config.getDouble("armor.crystal_helmet.defense", 3);

    @Override
    public String getID() {
        return "crystal_helmet";
    }

    @Override
    public ItemStack createItem() {
        return new ItemBuilder(Material.LEATHER_HELMET)
                .itemName(Component.text("Crystal Helmet")
                        .decoration(TextDecoration.BOLD, true)
                        .color(TextColor.color(165, 243, 255)))
                .color(Color.fromRGB(255, 255, 255))
                .armorTrim(new ArmorTrim(TrimMaterial.DIAMOND, TrimPattern.FLOW))
                .maxDamage(400)
                .attribute(Attribute.GENERIC_ARMOR, new AttributeModifier(Keys.stats(getID()), defense, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HEAD))
                .attribute(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(Keys.stats(getID()),3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HEAD))
                .flags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DYE, ItemFlag.HIDE_ARMOR_TRIM)
                .lore(
                        Component.text("§7§l──────────"),
                        Component.text("§7Defense: §2+%s".formatted(defense).replace(".0","")),
                        Component.empty(),
                        Component.text("§6§lFULL SET BONUS"),
                        Component.text("§7Taking damage builds Crystal Charges."),
                        Component.text("§7At max charges gain absorption and"),
                        Component.text("§7release a damaging shockwave."))
                .build();
    }

    @Override
    public EquipmentSlot getType() {
        return EquipmentSlot.HEAD;
    }
}
