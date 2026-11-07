package me.lightning.extraweapons;

import me.lightning.extraweapons.armor.*;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ArmorRegistry {

    private static final Map<String, CustomArmor> REGISTRY = new LinkedHashMap<>();

    public static void load() {
        register(new ThunderHelmet());
        register(new ThunderChestplate());
        register(new ThunderLeggings());
        register(new ThunderBoots());
        register(new EarthquakeBoots());
        register(new Jetpack());
        register(new TitanChestplate());
        register(new SpeedsterBoots());
        register(new SpringBoots());
        register(new ShockwaveChestplate());
        register(new GravityChestplate());
        register(new GravityLeggings());
        register(new MoltenLeggings());
        register(new PhotonVisor());
        register(new BerserkerHelmet());
        register(new CrystalHelmet());
        register(new CrystalChestplate());
        register(new CrystalLeggings());
        register(new CrystalBoots());
    }

    private static void register(CustomArmor armor) {
        REGISTRY.put(armor.getID(), armor);
    }

    public static CustomArmor get(String ID) {
        return REGISTRY.get(ID);
    }

    public static Collection<CustomArmor> getAll() {
        return REGISTRY.values();
    }

}