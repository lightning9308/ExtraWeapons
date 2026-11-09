package me.lightning.extraweapons;

import me.lightning.extraweapons.items.CustomItem;
import me.lightning.extraweapons.items.melee.*;
import me.lightning.extraweapons.items.miscellaneous.*;
import me.lightning.extraweapons.items.ranged.LaunchBow;
import me.lightning.extraweapons.items.ranged.ShortBow;
import me.lightning.extraweapons.items.ranged.ThunderRod;
import me.lightning.extraweapons.items.ranged.TrackerBow;
import me.lightning.extraweapons.items.throwables.FrostOrb;
import me.lightning.extraweapons.items.throwables.InkBomb;
import me.lightning.extraweapons.items.throwables.Shuriken;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ItemRegistry {
    private static final Map<String, CustomItem> REGISTRY = new LinkedHashMap<>();

    public static void load() {
        register(new AntiFlightStick());
        register(new HealingWand());
        register(new InfiniteBeef());
        register(new InfiniteFirework());
        register(new LongSword());
        register(new VampireBlade());
        register(new LastStrike());
        register(new DualBlades());
        register(new ShortDagger());
        register(new ShockBlade());
        register(new Shuriken());
        register(new FirstStrike());
        register(new GravityBlade());
        register(new FrostOrb());
        register(new InkBomb());
        register(new TrackerBow());
        register(new LaunchBow());
        register(new ShortBow());
        register(new BlinkDagger());
        register(new MoltenEdge());
        register(new ThunderRod());
        register(new Magnet());
        register(new WardenShield());
        register(new WebShooter());
    }

    private static void register(CustomItem item) {
        REGISTRY.put(item.getID(), item);
    }

    public static CustomItem get(String ID) {
        return REGISTRY.get(ID);
    }

    public static Collection<CustomItem> getAll() {
        return REGISTRY.values();
    }

}
