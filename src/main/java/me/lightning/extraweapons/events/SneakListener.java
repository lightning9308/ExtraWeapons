package me.lightning.extraweapons.events;

import me.lightning.extraweapons.ArmorRegistry;
import me.lightning.extraweapons.Utils;
import me.lightning.extraweapons.armors.Jetpack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

public class SneakListener implements Listener {

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e) {
        Player player = e.getPlayer();

        ItemStack chestplate = player.getInventory().getChestplate();

        if (Utils.isItem(chestplate, "jetpack")) {
            ((Jetpack) ArmorRegistry.get("jetpack")).onSneak(e);
        }

    }
}