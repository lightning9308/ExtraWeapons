package me.lightning.extraweapons.items;

import me.lightning.extraweapons.Cooldown;
import me.lightning.extraweapons.ExtraWeapons;
import me.lightning.extraweapons.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class ShortBow extends CustomItem {

    FileConfiguration config = ExtraWeapons.getPlugin().getConfig();
    double duration = config.getDouble("items.shortbow.cooldown");

    Cooldown cooldown = new Cooldown();

    @Override
    public String getID() {
        return "shortbow";
    }

    @Override
    protected ItemStack createItem() {
        return new ItemBuilder(Material.BOW)
                .itemName(Component.text("Shortbow")
                        .decoration(TextDecoration.BOLD, true)
                        .color(TextColor.color(139, 107, 74)))
                .flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ADDITIONAL_TOOLTIP)
                .lore(
                        Component.text("§6§lABILITY: RIGHT CLICK"),
                        Component.text("§7Shoots arrows instantly."),
                        Component.text("§8Cooldown: %ss".formatted(duration).replace(".0","")))
                .build();
    }

    @Override
    public void rightClick(PlayerInteractEvent e) {
        e.setCancelled(true);
        Player player = e.getPlayer();

        if (!cooldown.isOver(player)) return;

        boolean creative = player.getGameMode().equals(GameMode.CREATIVE);

        if (!creative && !player.getInventory().contains(Material.ARROW)) return;

        Arrow arrow = player.launchProjectile(Arrow.class);

        if (creative) {
            arrow.setPickupStatus(AbstractArrow.PickupStatus.CREATIVE_ONLY);
        } else {
            int slot = player.getInventory().first(Material.ARROW);
            if (slot == -1) return;
            ItemStack arrowItem = player.getInventory().getItem(slot);
            if (arrowItem != null) {
                arrowItem.setAmount(arrowItem.getAmount() - 1);
            }
        }

        cooldown.start(player, duration);
    }
}
