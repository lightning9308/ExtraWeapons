package me.lightning.extraweapons.armor.armorsets;

import me.lightning.extraweapons.Cooldown;
import me.lightning.extraweapons.ExtraWeapons;
import me.lightning.extraweapons.Keys;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.time.Duration;
import java.util.*;

public class ThunderSet {
    private static final FileConfiguration config = ExtraWeapons.getPlugin().getConfig();

    static double bonus_speed = config.getDouble("armor.thunder_set.bonus_speed", 0.5);
    static double bonus_damage = config.getDouble("armor.thunder_set.bonus_damage", 0.3);

    static Cooldown cooldown = new Cooldown();

    private static final Map<UUID, Integer> voltage = new HashMap<>();
    private static final Set<UUID> activeAbility = new HashSet<>();

    public static void onHit(EntityDamageByEntityEvent e) {
        Player player = (Player) e.getDamager();
        UUID playerUUID = player.getUniqueId();

        if (!cooldown.isOver(player)) return;

        if (activeAbility.contains(playerUUID)) return;

        int currentVolt = voltage.getOrDefault(playerUUID,0) + 1;
        voltage.put(playerUUID, currentVolt);

        if (currentVolt >= 10) {
            player.sendActionBar(
                    Component.text("VOLTAGE [MAX]")
                            .decoration(TextDecoration.BOLD, true)
                            .color(TextColor.color(225, 205, 0)));
        }
        else {
            player.sendActionBar(
                    Component.text("VOLTAGE [" + currentVolt + "/10]")
                            .decoration(TextDecoration.BOLD, true)
                            .color(TextColor.color(225, 205, 0)));
        }

        cooldown.start(player, 1);
    }

    public static void onSneak(PlayerToggleSneakEvent e) {
        if (!e.isSneaking()) return;
        Player player = e.getPlayer();
        UUID playerUUID = player.getUniqueId();
        int currentVoltage = voltage.getOrDefault(playerUUID,0);

        if (currentVoltage >= 10 && !activeAbility.contains(playerUUID)) {
            //visual effect
            player.sendActionBar(Component.empty());
            player.showTitle(Title.title(Component.empty(), Component.text("[!] OVERCHARGE [!]")
                            .decoration(TextDecoration.BOLD, true).color(TextColor.color(225, 205, 0))
                    ,Title.Times.times(Duration.ofMillis(200), Duration.ofMillis(1000), Duration.ofMillis(200))));

            player.getWorld().strikeLightningEffect(player.getLocation());

            //buffs
            activeAbility.add(playerUUID);
            voltage.remove(playerUUID);
            removeBuffs(player);

            AttributeInstance speed = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
            AttributeInstance kb = player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
            AttributeInstance damage = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);

            speed.addTransientModifier(new AttributeModifier(Keys.stats("thunder_set"),bonus_speed, AttributeModifier.Operation.MULTIPLY_SCALAR_1));
            kb.addTransientModifier(new AttributeModifier(Keys.stats("thunder_set"),1, AttributeModifier.Operation.ADD_NUMBER));
            damage.addTransientModifier(new AttributeModifier(Keys.stats("thunder_set"),bonus_damage, AttributeModifier.Operation.MULTIPLY_SCALAR_1));

            //duration
            Bukkit.getScheduler().runTaskLater(ExtraWeapons.getPlugin(), task -> {
                activeAbility.remove(playerUUID);
                removeBuffs(player);

                //visual effect
                Particle.ASH
                        .builder()
                        .location(player.getEyeLocation())
                        .count(60)
                        .offset(0.5, 0.5 ,0.5)
                        .spawn();

            },100);
        }
    }

    public static void onDeath(PlayerDeathEvent e) {
        Player player = e.getPlayer();
        UUID playerUUID = player.getUniqueId();

        voltage.remove(playerUUID);

        if (activeAbility.contains(playerUUID)) {
            activeAbility.remove(playerUUID);

            removeBuffs(player);
        }
    }

    private static void removeBuffs(Player player) {
        AttributeInstance speed = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        AttributeInstance kb = player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
        AttributeInstance damage = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);

        speed.removeModifier(Keys.stats("thunder_set"));
        kb.removeModifier(Keys.stats("thunder_set"));
        damage.removeModifier(Keys.stats("thunder_set"));
    }
}
