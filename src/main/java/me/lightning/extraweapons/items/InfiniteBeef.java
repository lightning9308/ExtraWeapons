package me.lightning.extraweapons.items;

import me.lightning.extraweapons.Cooldown;
import me.lightning.extraweapons.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class InfiniteBeef extends CustomItem {

    Cooldown cooldown = new Cooldown();

    double duration = config.getDouble("items.infinite_beef.cooldown");
    String cooldownMsg = config.getString("messages.cooldown");

    @Override
    public String getID() {
        return "infinite_beef";
    }

    @Override
    public ItemStack createItem() {
        return new ItemBuilder(Material.COOKED_BEEF)
                .itemName(Component.text("Infinite Beef")
                        .color(TextColor.color(165, 42, 42))
                        .decoration(TextDecoration.BOLD,true))
                .flags(ItemFlag.HIDE_ATTRIBUTES)
                .glow(true)
                .maxStackSize(1)
                .lore(
                Component.text("§6§lABILITY: RIGHT CLICK"),
                Component.text("§7Restores hunger."),
                Component.text("§8Cooldown: %ss".formatted(duration).replace(".0","")))
                .build();
    }

    @Override
    public void rightClick(PlayerInteractEvent e) {
        e.setCancelled(true);
        Player player = e.getPlayer();

        if (player.getFoodLevel() == 20) return;

        if (cooldownMsg != null) {
            cooldownMsg = ChatColor.translateAlternateColorCodes('&',cooldownMsg);
        }
        if (!cooldown.isOver(player)) {
            player.sendMessage(cooldownMsg.replace("%time%",String.valueOf(cooldown.getRemaining(player))));
            return;
        }

        player.setFoodLevel(20);
        cooldown.start(player,duration);

    }

}
