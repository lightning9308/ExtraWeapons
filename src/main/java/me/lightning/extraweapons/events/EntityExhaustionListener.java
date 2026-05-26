package me.lightning.extraweapons.events;

import me.lightning.extraweapons.ArmorRegistry;
import me.lightning.extraweapons.Utils;
import me.lightning.extraweapons.armors.SpeedsterBoots;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExhaustionEvent;
import org.bukkit.inventory.ItemStack;

public class EntityExhaustionListener implements Listener {

    @EventHandler
    public void exhaustion(EntityExhaustionEvent e) {
        if (!(e.getEntity() instanceof Player player)) return;


        ItemStack boots = player.getInventory().getBoots();
        if (boots != null && !boots.getType().isAir() && boots.hasItemMeta()) {
            if (Utils.isItem(boots, "speedster_boots")) {
                ((SpeedsterBoots) ArmorRegistry.get("speedster_boots")).exhaustion(e);
            }
        }
    }
}
