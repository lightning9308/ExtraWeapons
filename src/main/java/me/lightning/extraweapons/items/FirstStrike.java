package me.lightning.extraweapons.items;

import me.lightning.extraweapons.ItemBuilder;
import me.lightning.extraweapons.Keys;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class FirstStrike extends CustomItem {

    double baseDamage = config.getDouble("items.first_strike.damage");

    @Override
    public String getID() {
        return "first_strike";
    }

    @Override
    protected ItemStack createItem() {
        return new ItemBuilder(Material.GOLDEN_SWORD)
                .itemName(Component.text("First Strike")
                        .color(TextColor.color(130,195,0))
                        .decoration(TextDecoration.BOLD, true))
                .flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ADDITIONAL_TOOLTIP)
                .attribute(Attribute.GENERIC_ATTACK_SPEED,
                        new AttributeModifier(
                                Keys.stats(getID()),
                                -2.4,
                                AttributeModifier.Operation.ADD_NUMBER,
                                EquipmentSlotGroup.MAINHAND))
                .attribute(Attribute.GENERIC_ATTACK_DAMAGE,
                        new AttributeModifier(
                                Keys.stats(getID()),
                                baseDamage - 1,
                                AttributeModifier.Operation.ADD_NUMBER,
                                EquipmentSlotGroup.MAINHAND))
                .maxDamage(250)
                .lore(
                        Component.text("§7§l──────────"),
                        Component.text("§7Damage: §c+%s".formatted(baseDamage).replace(".0","")),
                        Component.empty(),
                        Component.text("§6§lPASSIVE"),
                        Component.text("§7Deals more damage the higher your health is"),
                        Component.text("§7+100% (100% HP), +50% (75% HP)"))
                .build();
    }

    @Override
    public void onHit(EntityDamageByEntityEvent e) {
        Player player = (Player) e.getDamager();
        double currentHealth = (player.getHealth() / player.getMaxHealth()) * 100;

        if (currentHealth >= 100.0) {
            e.setDamage(e.getDamage() + baseDamage);
        } else if (currentHealth >= 75.0) {
            e.setDamage(e.getDamage() + baseDamage/2);
        }
    }
}
