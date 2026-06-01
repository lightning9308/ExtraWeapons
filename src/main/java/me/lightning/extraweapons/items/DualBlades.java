package me.lightning.extraweapons.items;

import me.lightning.extraweapons.ExtraWeapons;
import me.lightning.extraweapons.ItemBuilder;
import me.lightning.extraweapons.Keys;
import me.lightning.extraweapons.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class DualBlades extends CustomItem {

    private final FileConfiguration config = ExtraWeapons.getPlugin().getConfig();
    double baseDamage = config.getDouble("items.dual_blades.damage");

    @Override
    public String getID() {
        return "dual_blades";
    }

    @Override
    public ItemStack createItem() {
        return new ItemBuilder(Material.IRON_SWORD)
                .itemName(Component.text("Dual Blade")
                        .color(TextColor.color(80,160,255))
                        .decoration(TextDecoration.BOLD,true))
                .flags(ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_ADDITIONAL_TOOLTIP)
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
                                EquipmentSlotGroup.MAINHAND))
                .lore(
                Component.text("§7§l──────────"),
                Component.text("§7Damage: §c+%s".formatted(baseDamage).replace(".0","")),
                Component.empty(),
                Component.text("§6§lPASSIVE"),
                Component.text("§7Deals double base damage while holding"),
                Component.text("§7another Dual Blade in your offhand."))
                .build();
    }

    @Override
    public void onHit(EntityDamageByEntityEvent e) {
        Player player = (Player) e.getDamager();
        ItemStack offHand = player.getInventory().getItemInOffHand();

        if (Utils.isItem(offHand,getID())) {
            Bukkit.getScheduler().runTaskLater(ExtraWeapons.getPlugin(), player::swingOffHand, 6L);
            e.setDamage(e.getDamage() + baseDamage);
        }
    }
}
