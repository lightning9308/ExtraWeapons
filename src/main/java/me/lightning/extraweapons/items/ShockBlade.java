package me.lightning.extraweapons.items;

import me.lightning.extraweapons.Cooldown;
import me.lightning.extraweapons.ExtraWeapons;
import me.lightning.extraweapons.ItemBuilder;
import me.lightning.extraweapons.Keys;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public class ShockBlade extends CustomItem {

    Cooldown cooldown = new Cooldown();
    private final FileConfiguration config = ExtraWeapons.getPlugin().getConfig();
    double baseDamage = config.getDouble("items.shock_blade.damage");
    double duration = config.getDouble("items.shock_blade.cooldown");
    int xp = config.getInt("items.shock_blade.xp_cost");
    double abilityDamage = config.getDouble("items.shock_blade.ability_damage");

    @Override
    public String getID() {
        return "shock_blade";
    }

    @Override
    public ItemStack createItem() {
        return new ItemBuilder(Material.NETHERITE_SWORD)
                .itemName(Component.text("Shock Blade")
                        .color(TextColor.color(89, 89, 105))
                        .decoration(TextDecoration.BOLD,true))
                .flags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP,ItemFlag.HIDE_ATTRIBUTES)
                .glow(true)
                .attribute(Attribute.GENERIC_ATTACK_SPEED,
                        new AttributeModifier(
                                Keys.stats,
                                -2.4,
                                AttributeModifier.Operation.ADD_NUMBER
                        ))
                .attribute(Attribute.GENERIC_ATTACK_DAMAGE,
                        new AttributeModifier(
                                Keys.stats,
                                baseDamage - 1,
                                AttributeModifier.Operation.ADD_NUMBER
                        ))
                .lore(
                Component.text("§7§l──────────"),
                Component.text("§7Damage: §c+%s".formatted(baseDamage).replace(".0","")),
                Component.empty(),
                Component.text("§6§lABILITY: RIGHT CLICK"),
                Component.text("§7Releases a small explosion"),
                Component.text("§7dealing §c%s damage §7to nearby enemies.".formatted(abilityDamage).replace(".0","")),
                Component.empty(),
                Component.text("§7Cost: §a%s XP".formatted(xp).replace(".0","")),
                Component.text("§8Cooldown: %ss".formatted(duration).replace(".0","")))
                .build();
    }

    @Override

    public void rightClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        if (!cooldown.isOver(player)) return;

        if (player.getTotalExperience() < xp) {
            player.sendMessage("not enough exp");
            return;
        }

        player.getWorld().spawnParticle(Particle.EXPLOSION,player.getLocation(),4,2,0,2);
        Collection<Entity> entities = player.getNearbyEntities(4,2,4);

        player.giveExp(-xp);

        for (Entity entity:entities) {
            if (!(entity instanceof LivingEntity livingEntity)) continue;
            livingEntity.damage(abilityDamage);
        }
        cooldown.start(player,duration);

    }
}
