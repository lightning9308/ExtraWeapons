package me.lightning.extraweapons.items;

import me.lightning.extraweapons.Keys;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public abstract class CustomItem {
    public abstract String getID();

    protected abstract ItemStack createItem();

    public void rightClick(PlayerInteractEvent e) {}

    public void leftClick(PlayerInteractEvent e) {}

    public void onHit(EntityDamageByEntityEvent e) {}

    public ItemStack getItem() {
        ItemStack item = createItem();
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.getPersistentDataContainer().set(Keys.CUSTOMITEM, PersistentDataType.STRING, getID() );
            item.setItemMeta(meta);
        }
        return item;
    }
}
