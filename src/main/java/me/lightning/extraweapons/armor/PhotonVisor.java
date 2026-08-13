package me.lightning.extraweapons.armor;

import me.lightning.extraweapons.ExtraWeapons;
import me.lightning.extraweapons.ItemBuilder;
import me.lightning.extraweapons.Keys;
import me.lightning.extraweapons.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.*;

public class PhotonVisor extends CustomArmor {

    double range = config.getDouble("armor.photon_visor.range", 8);
    int maxEnergy = config.getInt("armor.photon_visor.energy", 100);
    int defense = config.getInt("armor.photon_visor.defense", 2);
    double baseDamage = config.getDouble("armor.photon_visor.damage", 2);
    int rechargeRate = config.getInt("armor.photon_visor.recharge_rate", 2);


    private final Particle.DustOptions RED_DUST = new Particle.DustOptions(Color.RED, 1);
    private static final Map<UUID, Integer> energy = new HashMap<>();
    private static final Set<UUID> active = new HashSet<>();
    private static PhotonVisor INSTANCE;

    public PhotonVisor() {
        INSTANCE = this;

        Bukkit.getScheduler().runTaskTimer(ExtraWeapons.getPlugin(), task -> {

            if (INSTANCE != this) {
                task.cancel();
                return;
            }

            //energy recharge
            energy.forEach((uuid, currentEnergy) -> {

                Player player = Bukkit.getPlayer(uuid);

                if (player == null) {
                    return;
                }

                if (!Utils.isItem(player.getInventory().getHelmet(), getID())) {
                    active.remove(uuid);
                    return;
                }

                if (active.contains(uuid)) return;

                int newEnergy = Math.min(maxEnergy, currentEnergy + rechargeRate);
                energy.put(uuid, newEnergy);

                player.sendActionBar(
                        Component.text(progressBar(newEnergy, maxEnergy, maxEnergy / 2))
                );

            });
        }, 0, 10);
    }

    @Override
    public String getID() {
        return "photon_visor";
    }

    @Override
    public ItemStack createItem() {
        return new ItemBuilder(Material.LEATHER_HELMET)
                .itemName(Component.text("Photon Visor")
                        .decoration(TextDecoration.BOLD, true)
                        .color(TextColor.color(255, 55, 55)))
                .maxDamage(250)
                .color(Color.fromRGB(255, 55, 55))
                .armorTrim(new ArmorTrim(TrimMaterial.REDSTONE, TrimPattern.SILENCE))
                .flags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DYE, ItemFlag.HIDE_ARMOR_TRIM)
                .attribute(Attribute.GENERIC_ARMOR, new AttributeModifier(Keys.stats(getID()),defense, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HEAD))
                .attribute(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(Keys.stats(getID()),1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HEAD))
                .lore(
                        Component.text("§7§l──────────"),
                        Component.text("§7Defense: §2+%s".formatted(defense).replace(".0", "")),
                        Component.empty(),
                        Component.text("§6§lABILITY: SHIFT"),
                        Component.text("§7Consumes energy to activate a continuous"),
                        Component.text("§7laser beam, piercing and damaging all"),
                        Component.text("§7enemies in its path."),
                        Component.text("§8Energy: §a%s".formatted(maxEnergy)),
                        Component.empty(),
                        Component.text("§6§lPASSIVE"),
                        Component.text("§7Energy automatically recharges"),
                        Component.text("§7when inactive.")
                )
                .build();
    }

    @Override
    public EquipmentSlot getType() {
        return EquipmentSlot.HEAD;
    }

    @Override
    public void onSneak(PlayerToggleSneakEvent e) {
        if (!e.isSneaking()) return;
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        if (active.contains(uuid)) {
            active.remove(uuid);
            return;
        }

        energy.putIfAbsent(uuid, maxEnergy);
        active.add(uuid);

        Bukkit.getScheduler().runTaskTimer(ExtraWeapons.getPlugin(), task -> {
            Location from = player.getEyeLocation().add(0, 0.1, 0);

            if (!active.contains(uuid)) {
                task.cancel();
                return;
            }

            int currentEnergy = energy.get(uuid);

            player.sendActionBar(
                    Component.text(progressBar(currentEnergy, maxEnergy, maxEnergy / 2))
            );

            if (currentEnergy <= 0) {

                task.cancel();
                active.remove(uuid);
                return;
            }

            energy.put(uuid, energy.get(uuid) - 2);

            //damage and drawing the laser
            Location to = from.clone().add(from.getDirection().multiply(range));
            BoundingBox box = BoundingBox.of(from, to);

            drawBeam(from, to);

            for (Entity entity : player.getWorld().getNearbyEntities(box)) {
                if (!(entity instanceof LivingEntity livingEntity) || entity == player) continue;

                livingEntity.damage(baseDamage, player);

            }
        },0, 2);


    }

    private void drawBeam(Location from, Location to) {
        Vector step = to.toVector().subtract(from.toVector());
        double distance = step.length();
        if (distance <= 0) return;

        step.normalize().multiply(0.2);
        Location point = from.clone();

        for (double travelled = 0; travelled <= distance; travelled += 0.2) {
            point.getWorld().spawnParticle(
                    Particle.DUST,
                    point,
                    1,
                    0.01, 0.01, 0.01,
                    0,
                    RED_DUST

            );
            point.add(step);
        }
    }

    private String progressBar(int current, int total, int totalParts) {
        int filledParts = (int) ((long) current * totalParts / total);
        int emptyParts = totalParts - filledParts;

        StringBuilder bar = new StringBuilder();

        for (int i = 0; i < filledParts; i++) {
            bar.append("§4|");
        }
        for (int i = 0; i < emptyParts; i++) {
            bar.append("§8|");
        }

        return bar.toString();
    }

    public static void cleanUp(Player player) {
        active.remove(player.getUniqueId());
        energy.remove(player.getUniqueId());
    }
}
