package me.lightning.extraweapons.items.miscellaneous;

import me.lightning.extraweapons.Cooldown;
import me.lightning.extraweapons.ItemBuilder;
import me.lightning.extraweapons.Utils;
import me.lightning.extraweapons.items.CustomItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public class Magnet extends CustomItem {

    double duration = config.getDouble("items.magnet.cooldown", 5);
    double range = config.getDouble("items.magnet.range", 8);

    Cooldown cooldown = new Cooldown();

    @Override
    public String getID() {
        return "magnet";
    }

    @Override
    protected ItemStack createItem() {
        return new ItemBuilder(Material.IRON_INGOT)
                .itemName(Component.text("Magnet")
                        .decoration(TextDecoration.BOLD, true)
                        .color(TextColor.color(127, 140, 141)))
                .maxStackSize(1)
                .flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ADDITIONAL_TOOLTIP)
                .lore(
                        Component.text("§6§lABILITY: RIGHT CLICK"),
                        Component.text("§7Pulls nearby dropped items to you."),
                        Component.text("§8Cooldown: %ss".formatted(duration).replace(".0","")))
                .build();
    }

    @Override
    public void rightClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Location playerLoc = player.getLocation();

        if (!cooldown.isOver(player)) {
            player.sendMessage(Utils.cooldownMsg(String.valueOf(cooldown.getRemaining(player))));
            return;
        }

        Collection<Entity> entities = playerLoc.getNearbyEntities(range,range,range);

        for (Entity entity : entities) {
            if (!(entity instanceof Item item)) continue;

            item.setPickupDelay(0);

            item.teleport(player);
        }

        cooldown.start(player, duration);
    }
}
