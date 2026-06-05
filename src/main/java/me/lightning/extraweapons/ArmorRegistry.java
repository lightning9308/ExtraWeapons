package me.lightning.extraweapons;

import me.lightning.extraweapons.armors.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ArmorRegistry {

    private static final Map<String, CustomArmor> REGISTRY = new HashMap<>();

    public static void load() {
        register(new EarthquakeBoots());
        register(new Jetpack());
        register(new TitanChestplate());
        register(new SpeedsterBoots());
        register(new ShockwaveChestplate());
        register(new GravityChestplate());
        register(new MoltenLeggings());
    }

    private static void register(CustomArmor armor) {REGISTRY.put(armor.getID(), armor);}

    public static CustomArmor get(String ID) {return REGISTRY.get(ID);}

    public static Collection<CustomArmor> getAll() {return REGISTRY.values();}

}
