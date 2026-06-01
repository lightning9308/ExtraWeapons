package me.lightning.extraweapons.items;

import me.lightning.extraweapons.ExtraWeapons;
import me.lightning.extraweapons.ItemBuilder;
import me.lightning.extraweapons.Keys;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class GravityBlade extends CustomItem {

    private final FileConfiguration config = ExtraWeapons.getPlugin().getConfig();
    double baseDamage = config.getDouble("items.gravity_blade.damage");
    double pull_strength = config.getDouble("items.gravity_blade.pull");

    @Override
    public String getID() {
        return "gravity_blade";
    }

    @Override
    protected ItemStack createItem() {
        return new ItemBuilder(Material.IRON_SWORD)
                .itemName(Component.text("Gravity Blade")
                        .color(TextColor.color(78, 44, 150))
                        .decoration(TextDecoration.BOLD, true))
                .flags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP, ItemFlag.HIDE_ATTRIBUTES)
                .attribute(Attribute.GENERIC_ATTACK_DAMAGE,
                        new AttributeModifier(
                                Keys.stats(getID()),
                                baseDamage - 1,
                                AttributeModifier.Operation.ADD_NUMBER,
                                EquipmentSlotGroup.MAINHAND))
                .attribute(Attribute.GENERIC_ATTACK_SPEED,
                        new AttributeModifier(
                                Keys.stats(getID()),
                                -2.4,
                                AttributeModifier.Operation.ADD_NUMBER,
                                EquipmentSlotGroup.MAINHAND))
                .maxDamage(250)
                .glow(true)
                .lore(
                        Component.text("§7§l──────────"),
                        Component.text("§7Damage: §c+%s".formatted(baseDamage).replace(".0","")),
                        Component.empty(),
                        Component.text("§6§lPASSIVE"),
                        Component.text("§7Pulls enemies toward you."))
                .build();
    }

    @Override
    public void onHit(EntityDamageByEntityEvent e) {
        LivingEntity entity = (LivingEntity) e.getEntity();
        Player player = (Player) e.getDamager();

        Vector pull = player.getLocation().toVector().subtract(entity.getLocation().toVector()).normalize();

        entity.setVelocity(pull.multiply(pull_strength));
    }
}
