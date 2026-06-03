package me.lightning.extraweapons.armors;

import me.lightning.extraweapons.Cooldown;
import me.lightning.extraweapons.ExtraWeapons;
import me.lightning.extraweapons.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Jetpack extends CustomArmor {

    private final Map<UUID, Integer> fuel = new HashMap<>();

    Cooldown cooldown = new Cooldown();

    int maxFuel = config.getInt("armor.jetpack.fuel");


    @Override
    public String getID() {
        return "jetpack";
    }

    @Override
    public ItemStack createItem() {
        return new ItemBuilder(Material.LEATHER_CHESTPLATE)
                .itemName(Component.text("Jetpack")
                        .decoration(TextDecoration.BOLD,true)
                        .color(TextColor.color(26, 188, 156)))
                .flags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP,ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_UNBREAKABLE,ItemFlag.HIDE_DYE,ItemFlag.HIDE_ARMOR_TRIM)
                .unbreakable(true)
                .color(Color.fromRGB(235,235,235))
                .armorTrim(new ArmorTrim(TrimMaterial.NETHERITE, TrimPattern.BOLT))
                .lore(
                Component.text("§6§lABILITY: SHIFT"),
                Component.text("§7Consumes fuel to enable flight."),
                Component.text("§8Fuel: §a%s".formatted(maxFuel)),
                Component.empty(),
                Component.text("§6§lPASSIVE"),
                Component.text("§7When fuel runs out, it fully recharges"),
                Component.text("§7after a short period. Grant immunity"),
                Component.text("§7to fall damage."))
                .build();
    }

    @Override
    public void onDamage(EntityDamageEvent e) {
        if (e.getCause() != EntityDamageEvent.DamageCause.FALL) return;
        e.setCancelled(true);
    }

    @Override
    public EquipmentSlot getType() {
        return EquipmentSlot.BODY;
    }

    public void onSneak(PlayerToggleSneakEvent e) {
        if (!e.isSneaking()) return;
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        fuel.putIfAbsent(uuid, maxFuel);

        if (cooldown.isFinished(player)) {
            fuel.put(uuid, maxFuel);
        }

        Bukkit.getScheduler().runTaskTimer(ExtraWeapons.getPlugin(), task -> {
            if (!player.isSneaking()) {
                task.cancel();
                return;
            }

            if (fuel.get(uuid) <= 0) {
                player.sendActionBar(
                        Component.text("Fuel: " + fuel.get(uuid)).
                                decoration(TextDecoration.BOLD,true).
                                color(TextColor.color(46, 204, 113))
                );
                task.cancel();
                if (cooldown.isOver(player)) {
                cooldown.start(player,5);
                }
                return;
            }

            fuel.put(uuid, fuel.get(uuid) - 1);
            player.sendActionBar(
                    Component.text("Fuel: " + fuel.get(uuid)).
                            decoration(TextDecoration.BOLD,true).
                            color(TextColor.color(46, 204, 113))
            );

            Location loc = player.getLocation().clone();
            Vector direction = loc.getDirection();
            loc.add(direction.multiply(-0.5)).add(0,1,0);
            player.getWorld().spawnParticle(Particle.FLAME,loc,3,0.15, 0.1, 0.15,0);

            player.setVelocity(direction.multiply(-0.75).add(new Vector(0, 0.5, 0)));

        }, 0L, 1L);
    }


}
