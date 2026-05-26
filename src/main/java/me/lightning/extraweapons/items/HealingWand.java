package me.lightning.extraweapons.items;

import me.lightning.extraweapons.Cooldown;
import me.lightning.extraweapons.ExtraWeapons;
import me.lightning.extraweapons.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class HealingWand extends CustomItem {

    Cooldown cooldown = new Cooldown();
    FileConfiguration config = ExtraWeapons.getPlugin().getConfig();
    double duration = config.getDouble("items.healing_wand.cooldown");
    double healAmount = config.getDouble("items.healing_wand.heal");
    String cooldownMsg = config.getString("messages.cooldown");

    @Override
    public String getID() {
        return "healing_wand";
    }

    @Override
    public ItemStack createItem() {
        return new ItemBuilder(Material.BLAZE_ROD)
                .itemName(Component.text("Healing Wand")
                        .color(TextColor.color(255, 193, 7))
                        .decoration(TextDecoration.BOLD,true))
                .flags(ItemFlag.HIDE_ATTRIBUTES)
                .maxStackSize(1)
                .lore(
                Component.text("§6§lABILITY: RIGHT CLICK"),
                Component.text("§7Restores §c%s ❤ §7on use.".formatted(healAmount /2).replace(".0","")),
                Component.text("§8Cooldown: %ss".formatted(duration).replace(".0","")))
                .build();
    }

    @Override
    public void rightClick(PlayerInteractEvent e) {
        e.setCancelled(true);
        Player player = e.getPlayer();
        if (player.getHealth() >= player.getMaxHealth()) return;

        if (cooldownMsg != null) {
            cooldownMsg = ChatColor.translateAlternateColorCodes('&',cooldownMsg);
        }
        if (!cooldown.isOver(player)) {
            player.sendMessage(cooldownMsg.replace("%time%",String.valueOf(cooldown.getRemaining(player))));
            return;
        }

        player.heal(healAmount);

        cooldown.start(player,duration);

    }

}
