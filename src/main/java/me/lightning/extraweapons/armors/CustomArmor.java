package me.lightning.extraweapons.armors;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import me.lightning.extraweapons.ExtraWeapons;
import me.lightning.extraweapons.Keys;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public abstract class CustomArmor {

    protected final FileConfiguration config = ExtraWeapons.getPlugin().getConfig();

    public abstract String getID();

    public abstract ItemStack createItem();

    public void onDamage(EntityDamageEvent e) {}

    public void onArmorChange(PlayerArmorChangeEvent e) {}

    public abstract EquipmentSlot getType();

    public ItemStack getItem() {
        ItemStack item = createItem();
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.getPersistentDataContainer().set(Keys.CUSTOMITEM, PersistentDataType.STRING, getID());
            item.setItemMeta(meta);
        }
        return item;
    }
}
