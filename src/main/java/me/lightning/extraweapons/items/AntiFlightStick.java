package me.lightning.extraweapons.items;

import me.lightning.extraweapons.Cooldown;
import me.lightning.extraweapons.ExtraWeapons;
import me.lightning.extraweapons.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class AntiFlightStick extends CustomItem {

    Cooldown cooldown = new Cooldown();

    double duration = config.getDouble("items.anti-flight_stick.cooldown");
    String cooldownMsg = config.getString("messages.cooldown");

    @Override
    public String getID() {
        return "antiflight_stick";
    }

    @Override
    public ItemStack createItem() {
        return new ItemBuilder(Material.STICK)
                .itemName(Component.text("Anti-Flight Stick")
                        .color(TextColor.color(235, 167, 29))
                        .decoration(TextDecoration.BOLD,true))
                .flags(ItemFlag.HIDE_ATTRIBUTES)
                .glow(true)
                .maxStackSize(1)
                .lore(
                Component.text("§6§lABILITY: RIGHT CLICK"),
                Component.text("§7Fires a beam that forces flyers to land."),
                Component.text("§8Cooldown: %ss".formatted(duration).replace(".0","")))
                .build();
    }

    @Override
    public void rightClick(PlayerInteractEvent e) {
        e.setCancelled(true);
        Player player = e.getPlayer();


        if (cooldownMsg != null) {
            cooldownMsg = ChatColor.translateAlternateColorCodes('&',cooldownMsg);
        }
        if (!cooldown.isOver(player)) {
            player.sendMessage(cooldownMsg.replace("%time%",String.valueOf(cooldown.getRemaining(player))));
            return;
        }

        Location start = player.getEyeLocation();
        Vector direction = start.getDirection();
        for (double i = 0; i < 20; i ++) {
            Location point = start.clone().add(direction.clone().multiply(i));
            player.getWorld().spawnParticle(Particle.SMOKE, point, 1, 0, 0, 0, 0);
        }

        Entity target = player.getTargetEntity(20);

        if (!(target instanceof LivingEntity)) return;

        Bukkit.getScheduler().runTaskTimer(ExtraWeapons.getPlugin(), task -> {

            if (target instanceof Player) {
                if (((Player) target).getGameMode().equals(GameMode.SPECTATOR)) return;
            }
            if (!target.getLocation().subtract(0,1,0).getBlock().isEmpty()) {
                task.cancel();
                return;
            }

            target.setVelocity(new Vector(0, -1, 0));
            target.setFallDistance(0);
        }, 0, 1);
        cooldown.start(player, duration);
    }
}
