package me.lightning.extraweapons.events;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import me.lightning.extraweapons.Utils;
import me.lightning.extraweapons.armor.CustomArmor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ArmorChangeListener implements Listener {

    @EventHandler
    public void armorChange(PlayerArmorChangeEvent e) {
        CustomArmor oldArmor = Utils.getCustomArmor(e.getOldItem());
        if (oldArmor != null) oldArmor.onArmorChange(e);

        CustomArmor newArmor = Utils.getCustomArmor(e.getNewItem());
        if (newArmor != null) newArmor.onArmorChange(e);
    }
}

