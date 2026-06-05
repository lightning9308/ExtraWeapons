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
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class MoltenEdge extends CustomItem {

    double baseDamage = config.getDouble("items.molten_edge.damage");

    @Override
    public String getID() {
        return "molten_edge";
    }

    @Override
    protected ItemStack createItem() {
        return new ItemBuilder(Material.DIAMOND_SWORD)
                .itemName(Component.text("Molten Edge")
                        .decoration(TextDecoration.BOLD, true)
                        .color(TextColor.color(165, 77, 23)))
                .flags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP, ItemFlag.HIDE_ATTRIBUTES)
                .attribute(Attribute.GENERIC_ATTACK_SPEED,
                        new AttributeModifier(
                                Keys.stats(getID()),
                                -2.4,
                                AttributeModifier.Operation.ADD_NUMBER,
                                EquipmentSlotGroup.MAINHAND
                        ))
                .attribute(Attribute.GENERIC_ATTACK_DAMAGE,
                        new AttributeModifier(
                                Keys.stats(getID()),
                                baseDamage - 1,
                                AttributeModifier.Operation.ADD_NUMBER,
                                EquipmentSlotGroup.MAINHAND
                        ))
                .lore(
                        Component.text("§7§l──────────"),
                        Component.text("§7Damage: §c+%s".formatted(baseDamage).replace(".0","")),
                        Component.empty(),
                        Component.text("§6§lPASSIVE"),
                        Component.text("§7Take 25% less damage from fire ticks."),
                        Component.text("§7Deal 50% more base damage while on fire."),
                        Component.text("§7Fire ticks last 50% longer."))
                .build();
    }

    public void onCombust(EntityCombustEvent e) {
        e.setDuration((float) (e.getDuration() * 1.5));
    }

    public void onDamage(EntityDamageEvent e) {
        if (e.getCause() != EntityDamageEvent.DamageCause.FIRE_TICK) return;
        e.setDamage(e.getDamage() * 0.75);
    }

    @Override
    public void onHit(EntityDamageByEntityEvent e) {
        Player player = (Player) e.getDamager();

        if (player.getFireTicks() > 0) {
            e.setDamage(e.getFinalDamage() + (baseDamage/2));
        }
    }
}
