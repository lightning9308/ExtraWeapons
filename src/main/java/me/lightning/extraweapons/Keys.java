package me.lightning.extraweapons;

import org.bukkit.NamespacedKey;

public class Keys {
    public static final NamespacedKey stats = new NamespacedKey(ExtraWeapons.getPlugin(),"stats");

    public static NamespacedKey stats(String ID) {
        return new NamespacedKey(ExtraWeapons.getPlugin(),ID + "_" + "stats");
    }

    public static final NamespacedKey CUSTOMITEM = new NamespacedKey(ExtraWeapons.getPlugin(),"customitem");


}
