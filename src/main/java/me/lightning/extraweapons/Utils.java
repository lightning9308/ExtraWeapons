package me.lightning.extraweapons;

import me.lightning.extraweapons.armor.CustomArmor;
import me.lightning.extraweapons.items.CustomItem;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class Utils {
    static FileConfiguration config = ExtraWeapons.getPlugin().getConfig();
    static String cooldownMsg = config.getString("messages.cooldown", "&cAbility on cooldown for &e%time%s&c.");

    public static String cooldownMsg(String remaining) {
        return ChatColor.translateAlternateColorCodes('&',cooldownMsg).replace("%time%",remaining);
    }

    public static boolean isItem(ItemStack item, String ID) {
        if (item == null || item.getType().isAir()) return false;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        String id = meta.getPersistentDataContainer().get(Keys.CUSTOMITEM,PersistentDataType.STRING);
        if (id == null) return false;

        return id.equals(ID);
    }

    public static boolean isCustom(ItemStack item) {
        if (item == null || item.getType().isAir()) return false;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        return meta.getPersistentDataContainer().has(Keys.CUSTOMITEM);
    }

    public static void updateItem(ItemStack item) {
        if (item == null) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        // item
        CustomItem customItem = getCustomItem(item);
        if (customItem != null) {
            ItemMeta customItemMeta = customItem.getItem().getItemMeta();


            meta.itemName(customItemMeta.itemName());
            meta.displayName(customItemMeta.displayName());
            meta.lore(customItemMeta.lore());
            meta.setAttributeModifiers(customItemMeta.getAttributeModifiers());
        }
        // armor
        CustomArmor customArmor = getCustomArmor(item);
        if (customArmor != null) {
            ItemMeta customItemMeta = customArmor.getItem().getItemMeta();

            meta.itemName(customItemMeta.itemName());
            meta.displayName(customItemMeta.displayName());
            meta.lore(customItemMeta.lore());
            meta.setAttributeModifiers(customItemMeta.getAttributeModifiers());
        }

        item.setItemMeta(meta);
    }

    public static CustomItem getCustomItem(ItemStack item) {
        if (!isCustom(item)) return null;

        String id = item.getItemMeta().getPersistentDataContainer().get(Keys.CUSTOMITEM, PersistentDataType.STRING);
        if (id == null) return null;

        return ItemRegistry.get(id);
    }

    public static CustomArmor getCustomArmor(ItemStack item) {
        if (!isCustom(item)) return null;

        String id = item.getItemMeta().getPersistentDataContainer().get(Keys.CUSTOMITEM,PersistentDataType.STRING);
        if (id == null) return null;

        return ArmorRegistry.get(id);
    }

    public static boolean fullSet(Player player, String setName) {
        PlayerInventory inv = player.getInventory();

        return isItem(inv.getHelmet(), setName + "_helmet") &&
                isItem(inv.getChestplate(), setName + "_chestplate") &&
                isItem(inv.getLeggings(), setName + "_leggings") &&
                isItem(inv.getBoots(), setName + "_boots");
    }
}
