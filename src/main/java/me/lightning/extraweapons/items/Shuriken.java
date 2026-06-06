package me.lightning.extraweapons.items;

import me.lightning.extraweapons.Cooldown;
import me.lightning.extraweapons.ExtraWeapons;
import me.lightning.extraweapons.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;

public class Shuriken extends CustomItem {

    Cooldown cooldown = new Cooldown();
    double duration = config.getDouble("items.shuriken.cooldown");
    double baseDamage = config.getDouble("items.shuriken.damage");


    @Override
    public String getID() {
        return "shuriken";
    }

    @Override
    protected ItemStack createItem() {
        return new ItemBuilder(Material.NETHER_STAR)
                .itemName(Component.text("Shuriken")
                        .color(TextColor.color(224, 226, 219))
                        .decoration(TextDecoration.BOLD,true))
                .maxStackSize(16)
                .flags(ItemFlag.HIDE_ATTRIBUTES)
                .lore(
                Component.text("§6§lABILITY: RIGHT CLICK"),
                Component.text("§7Shoots the shuriken"),
                Component.text("§7dealing §c%s damage§7.".formatted(baseDamage).replace(".0","")),
                Component.text("§8Cooldown: %ss".formatted(duration).replace(".0","")))

                .build();
    }
    
    @Override
    public void rightClick(PlayerInteractEvent e) {
        e.setCancelled(true);
        Player player = e.getPlayer();

        if (!cooldown.isOver(player)) return;

        Location point = player.getLocation();
        point.add(0,1.2,0);

        ItemStack shurikens = player.getInventory().getItemInMainHand();
        shurikens.setAmount(shurikens.getAmount() - 1);

        cooldown.start(player, duration);

        new BukkitRunnable() {
            int time = 140;
            @Override
            public void run() {
                // move and spawn shuriken
                point.add(point.getDirection().multiply(1));
                player.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, point,1,0,0,0,0);

                // damage
                Collection<LivingEntity> entities = point.getNearbyLivingEntities(0.12,0.12,0.12);

                if (entities.isEmpty()) return;
                LivingEntity target = entities.iterator().next();

                if (target != player) {
                    target.damage(baseDamage);
                    cancel();
                    return;
                }

                // duration
                if (time <= 0) {
                    cancel();
                    return;
                }
                time--;
            }
        }.runTaskTimer(ExtraWeapons.getPlugin(),0,1);
    }
}
