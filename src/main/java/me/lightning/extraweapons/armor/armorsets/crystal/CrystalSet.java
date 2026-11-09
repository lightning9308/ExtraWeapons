package me.lightning.extraweapons.armor.armorsets.crystal;

import me.lightning.extraweapons.ExtraWeapons;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CrystalSet {
    private static final FileConfiguration config = ExtraWeapons.getPlugin().getConfig();

    static int charges_needed = config.getInt("armor.crystal_set.charges", 100);
    static double absorption = config.getDouble("armor.crystal_set.absorption", 4);
    static double damage = config.getDouble("armor.crystal_set.damage", 6);
    static double push_strength = config.getDouble("armor.crystal_set.push", 3);

    private static final Map<UUID, Integer> charges = new HashMap<>();

    public static void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player player)) return;
        UUID playerUUID = player.getUniqueId();

        double hitDamage = e.getDamage();
        int current = charges.getOrDefault(playerUUID, 0) + Math.max(0, (int) Math.round(hitDamage));

        Location playerLoc = player.getLocation();

        player.getWorld().playSound(playerLoc, Sound.BLOCK_AMETHYST_BLOCK_HIT, 1, 1);

        if (current >= charges_needed) {
            charges.remove(playerUUID);
            player.sendActionBar(Component.empty());
            crystal_shatter(player);

        }
        else {
            charges.put(playerUUID, current);
            player.sendActionBar(
                    Component.text("CRYSTAL [" + current + "/" + charges_needed + "]")
                            .decoration(TextDecoration.BOLD, true)
                            .color(TextColor.color(165, 243, 255)));
        }
    }

    private static void crystal_shatter(Player player) {
        PotionEffect absorp = new PotionEffect(PotionEffectType.ABSORPTION, 100, Math.max(0, (int) Math.round(absorption / 4) - 1), false, false, false);
        player.addPotionEffect(absorp);

        Location playerLoc = player.getLocation();

        player.getWorld().playSound(playerLoc, Sound.BLOCK_GLASS_BREAK, 1, 1);

        player.showTitle(Title.title(Component.empty(), Component.text("[!] SHATTERED [!]")
                        .decoration(TextDecoration.BOLD, true).color(TextColor.color(165, 243, 255))
                , Title.Times.times(Duration.ofMillis(200), Duration.ofMillis(1000), Duration.ofMillis(200))));

        Particle.BLOCK
                .builder()
                .location(playerLoc)
                .data(Material.WHITE_STAINED_GLASS.createBlockData())
                .count(75)
                .offset(0.5, 0.5, 0.5)
                .spawn();

        Collection<LivingEntity> entities = playerLoc.getNearbyLivingEntities(5, 5, 5);
        entities.remove(player);

        for (LivingEntity entity : entities) {
            Vector push = entity.getLocation().toVector().subtract(playerLoc.toVector());

            if (push.lengthSquared() == 0) push = player.getLocation().getDirection();

            entity.setVelocity(push.normalize().multiply(push_strength).add(new Vector(0, 0.5, 0)));
            entity.damage(damage, player);
        }
    }

    public static void onDeath(EntityDeathEvent e) {
        if (!(e.getEntity() instanceof Player player)) return;
        charges.remove(player.getUniqueId());
    }
}
