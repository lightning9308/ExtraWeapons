package me.lightning.extraweapons.items;

import me.lightning.extraweapons.Cooldown;
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
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class VampireBlade extends CustomItem {

    Cooldown passiveCooldown = new Cooldown();
    private final FileConfiguration config = ExtraWeapons.getPlugin().getConfig();
    double duration = config.getDouble("items.vampire_blade.cooldown");
    double baseDamage = config.getDouble("items.vampire_blade.damage");
    double ratio = config.getDouble("items.vampire_blade.lifesteal_ratio");

    @Override
    public String getID() {
        return "vampire_blade";
    }

    @Override
    public ItemStack createItem() {
        return new ItemBuilder(Material.DIAMOND_SWORD)
                .itemName(Component.text("Vampire Blade")
                        .color(TextColor.color(120, 0, 20))
                        .decoration(TextDecoration.BOLD, true))
                .glow(true)
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
                .flags(ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_ADDITIONAL_TOOLTIP)
                .lore(
                Component.text("§7§l──────────"),
                Component.text("§7Damage: §c+%s".formatted(baseDamage).replace(".0","")),
                Component.empty(),
                Component.text("§6§lPASSIVE"),
                Component.text("§7Restores §a%s%% §7of damage dealt as health.".formatted(ratio * 100).replace(".0","")),
                Component.text("§8Cooldown: %ss".formatted(duration).replace(".0","")))
                .build();
    }

    @Override
    public void onHit(EntityDamageByEntityEvent e) {
        Player player = (Player) e.getDamager();

        double finalDmg = e.getFinalDamage();

        if (!passiveCooldown.isOver(player)) return;
        player.heal(finalDmg * ratio);
        passiveCooldown.start(player,duration);

    }
}
