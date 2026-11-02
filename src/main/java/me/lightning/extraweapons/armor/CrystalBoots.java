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
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;

public class CrystalBoots extends CustomArmor {

    double defense = config.getDouble("armor.crystal_boots.defense", 3);

    @Override
    public String getID() {
        return "crystal_boots";
    }

    @Override
    public ItemStack createItem() {
        return new ItemBuilder(Material.LEATHER_BOOTS)
                .itemName(Component.text("Crystal Boots")
                        .decoration(TextDecoration.BOLD, true)
                        .color(TextColor.color(165, 243, 255)))
                .color(Color.fromRGB(255, 255, 255))
                .armorTrim(new ArmorTrim(TrimMaterial.DIAMOND, TrimPattern.FLOW))
                .maxDamage(500)
                .attribute(Attribute.GENERIC_ARMOR, new AttributeModifier(Keys.stats(getID()),defense, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET))
                .attribute(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(Keys.stats(getID()),3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET))
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
        return EquipmentSlot.FEET;
    }
}
