package me.lightning.extraweapons.items;

import me.lightning.extraweapons.Cooldown;
import me.lightning.extraweapons.ExtraWeapons;
import me.lightning.extraweapons.ItemBuilder;
import me.lightning.extraweapons.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class WardenShield extends CustomItem {

    double blockingDuration = config.getDouble("items.warden_shield.blocking_duration", 3);
    double duration = config.getDouble("items.warden_shield.cooldown", 5);
    private final Set<UUID> blocking = new HashSet<>();
    Cooldown cooldown = new Cooldown();

    @Override
    public String getID() {
        return "warden_shield";
    }

    @Override
    protected ItemStack createItem() {
        return new ItemBuilder(Material.ECHO_SHARD)
                .itemName(Component.text("Warden Shield")
                        .decoration(TextDecoration.BOLD, true)
                        .color(TextColor.color(5, 67, 75)))
                .flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ADDITIONAL_TOOLTIP)
                .lore(
                        Component.text("§6§lABILITY: RIGHT CLICK"),
                        Component.text("§7Consume the warden shield to block"),
                        Component.text("§7all incoming damage for %ss.".formatted(blockingDuration).replace(".0", "")),
                        Component.text("§8Cooldown: %ss".formatted(duration).replace(".0", "")))
                .build();
    }

    @Override
    public void rightClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        if (blocking.contains(player.getUniqueId())) {
            player.sendMessage("§cYou are already blocking!");
            return;
        }

        if (!cooldown.isOver(player)) {
            player.sendMessage(Utils.cooldownMsg(String.valueOf(cooldown.getRemaining(player))));
            return;
        }

        ItemStack wardenShieldItem = player.getInventory().getItemInMainHand();

        wardenShieldItem.subtract();

        blocking.add(player.getUniqueId());

        //visual and timing of blocking
        player.showTitle(Title.title(Component.empty(), Component.text("\uD83D\uDEE1 \uD83D\uDEE1 \uD83D\uDEE1")
                 .decoration(TextDecoration.BOLD, true).color(TextColor.color(176, 181, 179))
                ,Title.Times.times(Duration.ofMillis(0), Duration.ofMillis((long) (blockingDuration * 1000)), Duration.ofMillis(0))));

        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {

                if (!player.isOnline() || ticks >= 5 * blockingDuration) {
                    blocking.remove(player.getUniqueId());
                    cooldown.start(player, duration);
                    cancel();
                    return;
                }

                Location currentLoc = player.getLocation().clone();

                    Particle.CRIT
                            .builder()
                            .extra(0)
                            .location(currentLoc.add(0,2.4,0))
                            .spawn();

                ticks += 1;
            }
        }.runTaskTimer(ExtraWeapons.getPlugin(), 0, 4);
    }

    //blocking the dmg
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player player)) return;

        if (!blocking.contains(player.getUniqueId())) return;

        e.setCancelled(true);

        player.getWorld().playSound(
                player.getLocation(),
                Sound.ENTITY_GENERIC_BIG_FALL,
                1f, 1.5f);
    }
}