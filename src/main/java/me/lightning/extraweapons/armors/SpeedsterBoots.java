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
import org.bukkit.event.entity.EntityExhaustionEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.event.entity.EntityExhaustionEvent.ExhaustionReason.*;

public class SpeedsterBoots extends CustomArmor {

    private final FileConfiguration config = ExtraWeapons.getPlugin().getConfig();
    double bonusSpeed = config.getDouble("armor.speedster_boots.bonus_speed");

    @Override
    public String getID() {
        return "speedster_boots";
    }

    @Override
    public ItemStack createItem() {
        return new ItemBuilder(Material.LEATHER_BOOTS)
                .itemName(Component.text("Speedster Boots")
                        .color(TextColor.color(80, 117, 145))
                        .decoration(TextDecoration.BOLD, true))
                .color(Color.fromRGB(85, 120, 150))
                .flags(ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ADDITIONAL_TOOLTIP)
                .attribute(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(Keys.stats(getID()),bonusSpeed, AttributeModifier.Operation.MULTIPLY_SCALAR_1, EquipmentSlotGroup.FEET))
                .lore(
                Component.text("§6§lPASSIVE"),
                Component.text("§7Adds %s%% speed, consume".formatted(bonusSpeed * 100).replace(".0","")),
                Component.text("§7more hunger during movement."))
                .build();
    }

    @Override
    public EquipmentSlot getType() {
        return EquipmentSlot.FEET;
    }

    public void exhaustion(EntityExhaustionEvent e) {
        if (e.getExhaustionReason() != WALK && e.getExhaustionReason() != SPRINT && e.getExhaustionReason() != JUMP) return;
        e.setExhaustion(e.getExhaustion() * 5f);
    }
}
