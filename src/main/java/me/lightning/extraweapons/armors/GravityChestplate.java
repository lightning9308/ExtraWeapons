package me.lightning.extraweapons.armors;

import me.lightning.extraweapons.Cooldown;
import me.lightning.extraweapons.ItemBuilder;
import me.lightning.extraweapons.Keys;
import me.lightning.extraweapons.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Collection;

public class GravityChestplate extends CustomArmor {

    Cooldown cooldown = new Cooldown();

    double duration = config.getDouble("armor.gravity_chestplate.cooldown");
    double pull_strength = config.getDouble("armor.gravity_chestplate.pull");
    double defence = config.getDouble("armor.gravity_chestplate.defence");

    @Override
    public String getID() {
        return "gravity_chestplate";
    }

    @Override
    public ItemStack createItem() {
        return new ItemBuilder(Material.LEATHER_CHESTPLATE)
                .itemName(Component.text("Gravity Chestplate")
                        .decoration(TextDecoration.BOLD, true)
                        .color(TextColor.color(69, 123, 180)))
                .color(Color.fromRGB(69, 123, 180))
                .flags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DYE)
                .attribute(Attribute.GENERIC_ARMOR, new AttributeModifier(Keys.stats(getID()),defence, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST))
                .attribute(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(Keys.stats(getID()),3, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.CHEST))
                .maxDamage(500)
                .lore(
                        Component.text("§7§l──────────"),
                        Component.text("§7Defence: §2+%s".formatted(defence).replace(".0","")),
                        Component.empty(),
                        Component.text("§6§lABILITY: SHIFT"),
                        Component.text("§7Pulls nearby enemies toward you."),
                        Component.text("§8Cooldown: %ss".formatted(duration).replace(".0","")))
                .build();
    }

    @Override
    public EquipmentSlot getType() {
        return EquipmentSlot.CHEST;
    }

    public void onSneak(PlayerToggleSneakEvent e) {
        if (!e.isSneaking()) return;
        Player player = e.getPlayer();
        Location playerLoc = player.getLocation();

        if (!cooldown.isOver(player)) {
            player.sendMessage(Utils.cooldownMsg(String.valueOf(cooldown.getRemaining(player))));
            return;
        }

        //get the entities
        Collection<LivingEntity> entities = playerLoc.getNearbyLivingEntities(5,5,5);
        entities.remove(player);
        if (entities.isEmpty()) return;

        Particle.SWEEP_ATTACK
                .builder()
                .location(playerLoc.clone().add(0,1,0))
                .count(5)
                .offset(0.5,0.5,0.5)
                .extra(1)
                .spawn();

        //apply the pull
        for (LivingEntity entity : entities) {
            Vector pull = playerLoc.toVector().subtract(entity.getLocation().toVector()).normalize();

            entity.setVelocity(pull.multiply(pull_strength).add(new Vector(0,0.5,0)));
        }

        cooldown.start(player, duration);
    }

}
