package me.lightning.extraweapons;

import me.lightning.extraweapons.commands.ExtraWeaponCommand;
import me.lightning.extraweapons.events.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class ExtraWeapons extends JavaPlugin {

    private static ExtraWeapons plugin;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        plugin = this;
        ItemRegistry.load();
        ArmorRegistry.load();


        getCommand("extraweapons").setExecutor(new ExtraWeaponCommand());

        getServer().getPluginManager().registerEvents(new PickupListener(), this);
        getServer().getPluginManager().registerEvents(new EntityExhaustionListener(), this);
        getServer().getPluginManager().registerEvents(new AnvilListener(), this);
        getServer().getPluginManager().registerEvents(new LeftClickListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(), this);
        getServer().getPluginManager().registerEvents(new RightClickListener(), this);
        getServer().getPluginManager().registerEvents(new ArmorChangeListener(), this);
        getServer().getPluginManager().registerEvents(new DamageListener(), this);
        getServer().getPluginManager().registerEvents(new SneakListener(), this);
        getServer().getPluginManager().registerEvents(new ProjectileHitListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    public static ExtraWeapons getPlugin() {
        return plugin;
    }
}
