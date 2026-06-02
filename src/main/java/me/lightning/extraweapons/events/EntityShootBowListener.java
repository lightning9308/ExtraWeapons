package me.lightning.extraweapons.events;

import me.lightning.extraweapons.Utils;
import me.lightning.extraweapons.items.LaunchBow;
import me.lightning.extraweapons.items.TrackerBow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

public class EntityShootBowListener implements Listener {
    @EventHandler
    public void onBowShoot(EntityShootBowEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        ItemStack bow = e.getBow();

        if (Utils.isItem(bow,"tracker_bow")) {
            ((TrackerBow)Utils.getCustomItem(bow)).onBowShoot(e);
        }

        if (Utils.isItem(bow,"launch_bow")) {
            ((LaunchBow)Utils.getCustomItem(bow)).onBowShoot(e);
        }
    }
}
