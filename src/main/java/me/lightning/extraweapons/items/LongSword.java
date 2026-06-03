package me.lightning.extraweapons.items;

import me.lightning.extraweapons.ItemBuilder;
import me.lightning.extraweapons.Keys;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class LongSword extends CustomItem {

    double farDamage = config.getDouble("items.longsword.far_damage");
    double closeDamage = config.getDouble("items.longsword.close_damage");
    double range = config.getDouble("items.longsword.range");

    @Override
    public String getID() {
        return "longsword";
    }

    @Override
    public ItemStack createItem() {
        return new ItemBuilder(Material.IRON_SWORD)
                .itemName(Component.text("Longsword")
                        .color(TextColor.color(160, 160, 160))
                        .decoration(TextDecoration.BOLD,true))
                .flags(ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_ADDITIONAL_TOOLTIP)
                .attribute(Attribute.PLAYER_ENTITY_INTERACTION_RANGE,
                        new AttributeModifier(
                        Keys.stats(getID()), range,
                        AttributeModifier.Operation.ADD_NUMBER))
                .attribute(Attribute.GENERIC_ATTACK_SPEED,
                        new AttributeModifier(
                                Keys.stats(getID()),
                                -2.4,
                                AttributeModifier.Operation.ADD_NUMBER,
                                EquipmentSlotGroup.MAINHAND
                        ))
                .attribute(Attribute.GENERIC_ATTACK_DAMAGE,
                        new AttributeModifier(
                                Keys.stats(getID()),
                                farDamage - 1,
                                AttributeModifier.Operation.ADD_NUMBER,
                                EquipmentSlotGroup.MAINHAND
                        ))
                .lore(
                Component.text("§7§l──────────"),
                Component.text("§7Damage: §c+%s".formatted(farDamage).replace(".0","")),
                Component.text("§7Range: §a+%s".formatted(range).replace(".0","")),
                Component.empty(),
                Component.text("§6§lPASSIVE"),
                Component.text("§7Deals less damage up close."))
                .build();
    }

    @Override
    public void onHit(EntityDamageByEntityEvent e) {
        Player player = (Player) e.getDamager();
        Location damagerLoc = player.getLocation();
        Location entityLoc = e.getEntity().getLocation();

        double range = entityLoc.distance(damagerLoc);

        double reducedDmg = farDamage - closeDamage;

        if (range < 3) {
            e.setDamage(e.getDamage() - reducedDmg);
        }
    }
}
