package me.lightning.extraweapons.armors;

import me.lightning.extraweapons.Cooldown;
import me.lightning.extraweapons.ExtraWeapons;
import me.lightning.extraweapons.ItemBuilder;
import me.lightning.extraweapons.Keys;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.Collection;

public class EarthquakeBoots extends CustomArmor {

    Cooldown cooldown = new Cooldown();

    double duration = config.getDouble("armor.earthquake_boots.cooldown");
    double defense = config.getDouble("armor.earthquake_boots.defense");
    String cooldownMsg = config.getString("messages.cooldown");

    @Override
    public String getID() {
        return "earthquake_boots";
    }

    @Override
    public ItemStack createItem() {
        return new ItemBuilder(Material.NETHERITE_BOOTS)
                .glow(true)
                .itemName(Component.text("Earthquake Boots")
                        .color(TextColor.color(100, 73, 8))
                        .decoration(TextDecoration.BOLD,true))
                .flags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP,ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_UNBREAKABLE)
                .attribute(Attribute.GENERIC_ARMOR, new AttributeModifier(Keys.stats(getID()),defense, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET))
                .attribute(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(Keys.stats(getID()),3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET))
                .unbreakable(true)
                .lore(
                Component.text("§7§l──────────"),
                Component.text("§7Defense: §2+%s".formatted(defense).replace(".0","")),
                Component.empty(),
                Component.text("§6§lPASSIVE"),
                Component.text("§7Fall damage releases a shockwave"),
                Component.text("§7that damages and slows enemies inside."),
                Component.text("§8Cooldown: %ss".formatted(duration).replace(".0","")))
                .build();
    }

    @Override
    public void onDamage(EntityDamageEvent e) {
        Player player = (Player) e.getEntity();
        if (e.getCause() != EntityDamageEvent.DamageCause.FALL) return;
        if (e.isCancelled()) return;

        if (cooldownMsg != null) {
            cooldownMsg = ChatColor.translateAlternateColorCodes('&',cooldownMsg);
        }
        if (!cooldown.isOver(player)) {
            player.sendMessage(cooldownMsg.replace("%time%",String.valueOf(cooldown.getRemaining(player))));
            return;
        }

        cooldown.start(player,duration);


        Location center = player.getLocation().clone();

        BoundingBox box = new BoundingBox(center.getX() - 4,
                                        center.getY() - 1,
                                        center.getZ() - 4,
                                        center.getX() + 4,
                                        center.getY() + 1,
                                        center.getZ() + 4);

        new BukkitRunnable() {
            int time = 140;
            Collection<Entity> entities = player.getWorld().getNearbyEntities(box);
            @Override
            public void run() {
                if (time <= 0) cancel();

                if (time % 10 == 0) {
                    entities = player.getWorld().getNearbyEntities(box);
                }

                for (Entity entity : entities) {
                    if (!(entity instanceof LivingEntity) || entity.getUniqueId().equals(player.getUniqueId())) continue;
                    Vector vel = entity.getVelocity();
                    entity.setVelocity(new Vector(vel.getX() * 0.45,vel.getY() * 0.9,vel.getZ() * 0.45));
                    ((LivingEntity) entity).damage(0.2);

                }
                for (double x = -4; x <= 4; x += 1.7) {
                    for (double z = -4; z <= 4; z += 1.7) {

                        Location loc = center.clone().add(x, 0.1, z);
                        player.getWorld().spawnParticle(Particle.BLOCK, loc, 1, 0.5, 0, 0.5, 0,Bukkit.createBlockData("dripstone_block"));
                    }
                }

                time--;
            }
        }.runTaskTimer(ExtraWeapons.getPlugin(), 0,1);


    }

    @Override
    public EquipmentSlot getType() {
        return EquipmentSlot.FEET;
    }
}
