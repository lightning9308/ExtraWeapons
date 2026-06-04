package me.lightning.extraweapons.items;

import me.lightning.extraweapons.Cooldown;
import me.lightning.extraweapons.ItemBuilder;
import me.lightning.extraweapons.Keys;
import me.lightning.extraweapons.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class BlinkDagger extends CustomItem {

    Cooldown cooldown = new Cooldown();

    double duration = config.getDouble("items.blink_dagger.cooldown");
    int range = config.getInt("items.blink_dagger.range");
    double baseDamage = config.getDouble("items.blink_dagger.damage");

    @Override
    public String getID() {
        return "blink_dagger";
    }

    @Override
    protected ItemStack createItem() {
        return new ItemBuilder(Material.IRON_SWORD)
                .itemName(Component.text("Blink Dagger")
                        .decoration(TextDecoration.BOLD, true)
                        .color(TextColor.color(109, 116, 155)))
                .attribute(Attribute.GENERIC_ATTACK_SPEED,
                        new AttributeModifier(
                                Keys.stats(getID()),
                                -2,
                                AttributeModifier.Operation.ADD_NUMBER,
                                EquipmentSlotGroup.MAINHAND
                        ))
                .attribute(Attribute.GENERIC_ATTACK_DAMAGE,
                        new AttributeModifier(
                                Keys.stats(getID()),
                                baseDamage - 1,
                                AttributeModifier.Operation.ADD_NUMBER,
                                EquipmentSlotGroup.MAINHAND
                        ))
                .flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ADDITIONAL_TOOLTIP)
                .lore(
                        Component.text("§7§l──────────"),
                        Component.text("§7Damage: §c+%s".formatted(baseDamage).replace(".0","")),
                        Component.empty(),
                        Component.text("§6§lABILITY: RIGHT CLICK"),
                        Component.text("§7Teleports you behind"),
                        Component.text("§7the targeted enemy."),
                        Component.text("§8Cooldown: %ss".formatted(duration).replace(".0","")))
                .build();
    }

    @Override
    public void rightClick(PlayerInteractEvent e) {
        e.setCancelled(true);
        Player player = e.getPlayer();
        Entity entity = player.getTargetEntity(range);

        if (!(entity instanceof LivingEntity)) return;

        if (!cooldown.isOver(player)) {
            player.sendMessage(Utils.cooldownMsg(String.valueOf(cooldown.getRemaining(player))));
            return;
        }

        Vector entityDir = entity.getLocation().getDirection().multiply(-1);
        Location entityLoc =  entity.getLocation().add(entityDir);

        player.teleport(entityLoc);

        cooldown.start(player, duration);
    }
}
