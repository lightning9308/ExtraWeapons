package me.lightning.extraweapons;

import me.lightning.extraweapons.items.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ItemRegistry {
    private static final Map<String, CustomItem> REGISTRY = new HashMap<>();

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
