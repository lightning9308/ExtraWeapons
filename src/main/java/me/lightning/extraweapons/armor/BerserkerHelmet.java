package me.lightning.extraweapons.armor;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import me.lightning.extraweapons.ItemBuilder;
import me.lightning.extraweapons.Keys;
import me.lightning.extraweapons.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BerserkerHelmet extends CustomArmor {

    double defense = config.getDouble("armor.berserker_helmet.defense", 3);

    @Override
    public String getID() {
        return "berserker_helmet";
    }

    @Override
    public ItemStack createItem() {
        return new ItemBuilder(Material.PLAYER_HEAD)
                .itemName(Component.text("Berserker Helmet")
                        .color(TextColor.color(139, 0, 0))
                        .decoration(TextDecoration.BOLD, true))
                .skullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjgyMjA5NDYyYjViMDJmZWYxYWJlNjhhNjUxNGFmZjM1YzYzOThiYzIyNjE1NDExMjY5NjgyZTAzZDNjMzA0OCJ9fX0=")
                .maxStackSize(1)
                .attribute(Attribute.GENERIC_ARMOR, new AttributeModifier(Keys.stats(getID()), defense, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HEAD))
                .flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ADDITIONAL_TOOLTIP, ItemFlag.HIDE_DYE, ItemFlag.HIDE_ARMOR_TRIM)
                .lore(
                        Component.text("§7§l──────────"),
                        Component.text("§7Defense: §2+%s".formatted(defense).replace(".0","")),
                        Component.empty(),
                        Component.text("§6§lPASSIVE"),
                        Component.text("§7Below 30% HP gain 20% movement speed,"),
                        Component.text("§720% attack speed, and knockback immunity."),
                        Component.text("§7While Strength is active take 10% increased damage."),
                        Component.text("§7Kills grant Strength I for 8s."))
                .build();
    }

    @Override
    public EquipmentSlot getType() {
        return EquipmentSlot.HEAD;
    }

    @Override
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player player)) return;

        if (player.hasPotionEffect(PotionEffectType.STRENGTH)) {
            e.setDamage(e.getDamage() * 1.1);
        }

        if ((player.getHealth() - e.getFinalDamage()) / player.getMaxHealth() <= 0.3) {
            applyBuffs(player);
        }
        else {
            removeBuffs(player);
        }

    }

    @Override
    public void onArmorChange(PlayerArmorChangeEvent e) {
        Player player = e.getPlayer();

        if (Utils.isItem(e.getNewItem(), getID())) {
            if ((player.getHealth() / player.getMaxHealth()) <= 0.3) {
                applyBuffs(player);
            }
            return;
        }

        removeBuffs(player);
    }


    public static void onDeath(EntityDeathEvent e) {
        if (e.getEntity() instanceof Player player) {
            removeBuffs(player);
        }

        Player killer = e.getEntity().getKiller();
        if (killer != null && Utils.isItem(killer.getInventory().getHelmet(), "berserker_helmet")) {
            killer.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 20 * 8, 0), false);
        }
    }

    public static void onRegen(EntityRegainHealthEvent e) {
        if (!(e.getEntity() instanceof Player player)) return;

        if ((player.getHealth() + e.getAmount()) / player.getMaxHealth() > 0.3) {
            removeBuffs(player);
        }
    }

    private static void removeBuffs(Player player) {
        AttributeInstance knockback = player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
        AttributeInstance speed = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        AttributeInstance attk_speed = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED);

        knockback.removeModifier(Keys.stats("berserker_helmet"));
        speed.removeModifier(Keys.stats("berserker_helmet"));
        attk_speed.removeModifier(Keys.stats("berserker_helmet"));
    }

    private static void applyBuffs(Player player) {
        removeBuffs(player);

        AttributeInstance knockback = player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
        AttributeInstance speed = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        AttributeInstance attk_speed = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED);

        knockback.addTransientModifier(new AttributeModifier(Keys.stats("berserker_helmet"), 1, AttributeModifier.Operation.ADD_NUMBER));
        speed.addTransientModifier(new AttributeModifier(Keys.stats("berserker_helmet"),0.2, AttributeModifier.Operation.MULTIPLY_SCALAR_1));
        attk_speed.addTransientModifier(new AttributeModifier(Keys.stats("berserker_helmet"),0.2, AttributeModifier.Operation.MULTIPLY_SCALAR_1));
    }
}
