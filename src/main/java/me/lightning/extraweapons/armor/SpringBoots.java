package me.lightning.extraweapons.armor;

import me.lightning.extraweapons.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpringBoots extends CustomArmor {

    private static final Map<UUID, Integer> charge = new HashMap<>();

    Cooldown cooldown = new Cooldown();

    double defense = config.getDouble("armor.spring_boots.defense", 3);
    int maxCharge = config.getInt("armor.spring_boots.max_charge", 100);
    int chargeRate = config.getInt("armor.spring_boots.charge_rate", 7);
    double jumpPower = config.getDouble("armor.spring_boots.jump_power", 2);
    double duration = config.getDouble("armor.spring_boots.cooldown", 3);

    @Override
    public String getID() {
        return "spring_boots";
    }

    @Override
    public ItemStack createItem() {
        return new ItemBuilder(Material.LEATHER_BOOTS)
                .itemName(Component.text("Spring Boots")
                        .decoration(TextDecoration.BOLD, true)
                        .color(TextColor.color(50, 200, 55)))
                .color(Color.fromRGB(50, 200, 55))
                .maxDamage(400)
                .attribute(Attribute.GENERIC_ARMOR, new AttributeModifier(Keys.stats(getID()),defense, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET))
                .attribute(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(Keys.stats(getID()),3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.FEET))
                .flags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DYE, ItemFlag.HIDE_ARMOR_TRIM)
                .lore(
                        Component.text("§7§l──────────"),
                        Component.text("§7Defense: §2+%s".formatted(defense).replace(".0","")),
                        Component.empty(),
                        Component.text("§6§lABILITY: SHIFT"),
                        Component.text("§7Charge a super jump while sneaking."),
                        Component.text("§7The longer you charge, the higher"),
                        Component.text("§7you launch."),
                        Component.text("§8Cooldown: %ss".formatted(duration).replace(".0","")),
                        Component.empty(),
                        Component.text("§6§lPASSIVE"),
                        Component.text("§7Bounce off the ground instead"),
                        Component.text("§7of taking fall damage."))
                .build();
    }

    @Override
    public EquipmentSlot getType() {
        return EquipmentSlot.FEET;
    }

    @Override
    public void onDamage(EntityDamageEvent e) {
        if (e.getCause() != EntityDamageEvent.DamageCause.FALL) return;
        Player player = (Player) e.getEntity();
        e.setCancelled(true);

        if (player.getFallDistance() < 5) return;
        if (player.isSneaking()) return;

        Vector velocity = player.getVelocity();
        velocity.setY(Math.min(2.5, Math.sqrt(player.getFallDistance()) * 0.35));
        player.setVelocity(velocity);

        Location location = player.getLocation();
        player.getWorld().playSound(location, Sound.BLOCK_SLIME_BLOCK_FALL, 1, 1);

        Particle.BLOCK
                .builder()
                .location(location)
                .data(Material.SLIME_BLOCK.createBlockData())
                .count(20)
                .offset(0, 0, 0)
                .spawn();
    }

    @Override
    public void onSneak(PlayerToggleSneakEvent e) {
        if (!e.isSneaking()) return;
        Player player = e.getPlayer();

        if (!cooldown.isOver(player)) {
            player.sendMessage(Utils.cooldownMsg(String.valueOf(cooldown.getRemaining(player))));
            return;
        }

        UUID uuid = player.getUniqueId();

        charge.put(uuid, 0);

        Bukkit.getScheduler().runTaskTimer(ExtraWeapons.getPlugin(), task -> {
            if (!player.isOnline() || !Utils.isItem(player.getInventory().getBoots(), getID())) {
                task.cancel();
                charge.remove(uuid);
                return;
            }

            if (!player.isSneaking()) {
                task.cancel();

                if (player.isOnGround() && charge.getOrDefault(uuid, 0) > 0) {
                    launch(player, charge.getOrDefault(uuid, 0));
                    cooldown.start(player, duration);
                }

                charge.remove(uuid);
                player.sendActionBar(Component.empty());
                return;

            }

            int currentCharge = charge.getOrDefault(uuid, 0);

            if (player.isOnGround()) {
                currentCharge = Math.min(maxCharge, currentCharge + chargeRate);
                charge.put(uuid, currentCharge);
            }

            if (currentCharge <= 0) return;

            player.sendActionBar(Component.text(progressBar(currentCharge, maxCharge)));
        }, 0, 2);
    }

    private void launch(Player player, int charge) {
        Vector velocity = player.getVelocity();
        velocity.setY(jumpPower * charge / maxCharge);
        player.setVelocity(velocity);
    }

    private String progressBar(int current, int total) {
        int totalParts = total / 2;
        int filledParts = (int) ((long) current * totalParts / total);
        int emptyParts = totalParts - filledParts;

        StringBuilder bar = new StringBuilder();

        for (int i = 0; i < filledParts; i++) {
            bar.append("§a|");
        }
        for (int i = 0; i < emptyParts; i++) {
            bar.append("§8|");
        }

        return bar.toString();
    }

    public static void cleanUp(Player player) {
        charge.remove(player.getUniqueId());
    }
}
