package me.lightning.extraweapons.items;

import me.lightning.extraweapons.ExtraWeapons;
import me.lightning.extraweapons.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Comparator;

public class TrackerBow extends CustomItem {
    @Override
    public String getID() {
        return "tracker_bow";
    }

    @Override
    protected ItemStack createItem() {
        return new ItemBuilder(Material.BOW)
                .itemName(Component.text("Tracker Bow")
                        .color(TextColor.color(51, 102, 255))
                        .decoration(TextDecoration.BOLD, true))
                .flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ADDITIONAL_TOOLTIP)
                .lore(
                        Component.text("§6§lPASSIVE"),
                        Component.text("§7Arrows auto lock on enemies."),
                        Component.text("§7Shots travel 25% slower."))
                .build();
    }

    public void onBowShoot(EntityShootBowEvent e) {
        LivingEntity shooter = e.getEntity();
        Entity arrow = e.getProjectile();

        Vector reducedSpeed = arrow.getVelocity().multiply(0.75);
        arrow.setVelocity(reducedSpeed);

        double baseSpeed = reducedSpeed.length();

        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {

                if (arrow.isDead() || ticks >= 50 || arrow.isOnGround()) {
                    arrow.remove();
                    cancel();
                    return;
                }
                ticks++;

                LivingEntity target = arrow.getLocation().getNearbyLivingEntities(3,3,3).stream().filter(entity -> entity != shooter).min(Comparator.comparingDouble(entity -> entity.getLocation().distanceSquared(arrow.getLocation()))).orElse(null);

                if (target == null) return;

                Vector targetDir = target.getLocation().toVector().subtract(arrow.getLocation().toVector()).normalize();

                arrow.setVelocity(targetDir.multiply(baseSpeed));

            }
        }.runTaskTimer(ExtraWeapons.getPlugin(), 0, 3);


    }
}
