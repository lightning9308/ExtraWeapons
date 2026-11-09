package me.lightning.extraweapons.events;

import me.lightning.extraweapons.armor.BerserkerHelmet;
import me.lightning.extraweapons.armor.armorsets.crystal.CrystalSet;
import me.lightning.extraweapons.armor.armorsets.thunder.ThunderSet;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class DeathListener implements Listener {

    @EventHandler
    public void onDeath(EntityDeathEvent e) {

        ThunderSet.onDeath(e);
        CrystalSet.onDeath(e);
        BerserkerHelmet.onDeath(e);
    }
}
