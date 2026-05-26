package me.lightning.extraweapons.armors;

import me.lightning.extraweapons.ExtraWeapons;
import me.lightning.extraweapons.ItemBuilder;
import me.lightning.extraweapons.Keys;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;

public class TitanChestPlate extends CustomArmor {

    private final FileConfiguration config = ExtraWeapons.getPlugin().getConfig();
    double speed = config.getDouble("armor.titan_chestplate.speed");
    double health = config.getDouble("armor.titan_chestplate.health");
    double damageRed = config.getDouble("armor.titan_chestplate.damage_reduction");
    double damagePenalty = config.getDouble("armor.titan_chestplate.damage_penalty");


    @Override
    public String getID() {
        return "titan_chestplate";
    }

    @Override
    public ItemStack createItem() {
        return new ItemBuilder(Material.LEATHER_CHESTPLATE)
                .itemName(Component.text("Titan Chestplate")
                        .color(TextColor.color(84, 91, 98))
                        .decoration(TextDecoration.BOLD, true))
                .color(Color.fromRGB(85, 90, 100))
                .flags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DYE, ItemFlag.HIDE_ARMOR_TRIM)
                .armorTrim(new ArmorTrim(TrimMaterial.NETHERITE, TrimPattern.TIDE))
                .attribute(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(Keys.stats,health, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST))
                .attribute(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(Keys.stats,speed, AttributeModifier.Operation.MULTIPLY_SCALAR_1, EquipmentSlotGroup.CHEST))
                .maxDamage(500)
                .lore(
                Component.text("§7§l──────────"),
                Component.text("§7Health: §c+%s ❤".formatted(health / 2).replace(".0","")),
                Component.text("§7Speed: §e%s%%".formatted(speed * 100).replace(".0","")),
                Component.empty(),
                Component.text("§6§lPASSIVE"),
                Component.text("§7Reduces incoming damage by %s%%".formatted(damageRed * 100).replace(".0","")),
                Component.text("§7Reduces damage dealt by %s%%".formatted(damagePenalty * 100).replace(".0","")))
                .build();
    }


    @Override
    public EquipmentSlot getType() {
        return EquipmentSlot.CHEST;
    }

    @Override
    public void onDamage(EntityDamageEvent e) {
        e.setDamage(e.getDamage() * (1 - damageRed));
    }

    public void onHit(EntityDamageByEntityEvent e) {
        e.setDamage(e.getDamage() * (1 - damagePenalty));
    }


}
