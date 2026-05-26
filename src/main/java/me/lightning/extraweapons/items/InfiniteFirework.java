package me.lightning.extraweapons.items;

import me.lightning.extraweapons.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InfiniteFirework extends CustomItem{
    @Override
    public String getID() {
        return "infinite_firework";
    }

    @Override
    public ItemStack createItem() {
        return new ItemBuilder(Material.FIREWORK_ROCKET)
                .itemName(Component.text("Infinite Firework")
                        .color(TextColor.color(237, 61, 41))
                        .decoration(TextDecoration.BOLD,true))
                .flags(ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_ADDITIONAL_TOOLTIP)
                .glow(true)
                .maxStackSize(1)
                .lore(
                Component.text("§6§lABILITY: RIGHT CLICK"),
                Component.text("§7Boosts elytra flight."))
                .build();
    }

    @Override
    public void rightClick(PlayerInteractEvent e) {
        e.setCancelled(true);
        Player player = e.getPlayer();
        ItemStack held = player.getInventory().getItemInMainHand();
        ItemMeta meta = held.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        held.setItemMeta(meta);


        if (player.isGliding()) {
            player.fireworkBoost(ItemStack.of(Material.FIREWORK_ROCKET));
        }
    }

}
