package me.lightning.extraweapons.items;

import me.lightning.extraweapons.ExtraWeapons;
import me.lightning.extraweapons.ItemBuilder;
import me.lightning.extraweapons.Keys;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ShortDagger extends CustomItem {

    private final FileConfiguration config = ExtraWeapons.getPlugin().getConfig();
    double baseDamage = config.getDouble("items.short_dagger.damage");

    @Override
    public String getID() {
        return "short_dagger";
    }

    @Override
    public ItemStack createItem() {
        return new ItemBuilder(Material.STONE_SWORD)
                .itemName(Component.text("Short Dagger")
                        .color(TextColor.color(126, 126, 126))
                        .decoration(TextDecoration.BOLD,true))
                .flags(ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_ADDITIONAL_TOOLTIP)
                .attribute(Attribute.GENERIC_ATTACK_DAMAGE,
                        new AttributeModifier(
                                Keys.stats,
                                baseDamage - 1,
                                AttributeModifier.Operation.ADD_NUMBER
                        ))
                .lore(
                Component.text("§7§l──────────"),
                Component.text("§7Damage: §c+%s".formatted(baseDamage).replace(".0","")),
                Component.empty(),
                Component.text("§6§lPASSIVE"),
                Component.text("§7Deals §c2x damage §7when striking"),
                Component.text("§7an enemy from behind."))
                .build();
    }

    @Override
    public void onHit(EntityDamageByEntityEvent e) {
        Player player = (Player) e.getDamager();
        if (!(e.getEntity() instanceof LivingEntity entity)) return;

        Vector entityDir = entity.getLocation().getDirection().normalize();
        Vector playerVec = player.getLocation().toVector()
                .subtract(entity.getLocation().toVector())
                .normalize();
        double dot = entityDir.dot(playerVec);


        if (dot < -0.55) {
            e.setDamage(e.getDamage() + baseDamage);
        }
    }
}
