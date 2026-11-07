package me.lightning.extraweapons;

import me.lightning.extraweapons.commands.ExtraWeaponCommand;
import me.lightning.extraweapons.events.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ExtraWeapons extends JavaPlugin {

    private static ExtraWeapons plugin;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        plugin = this;
        ItemRegistry.load();
        ArmorRegistry.load();

        new UpdateChecker(this);

        getCommand("extraweapons").setExecutor(new ExtraWeaponCommand());

        getServer().getPluginManager().registerEvents(new playerQuitListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PickupListener(), this);
        getServer().getPluginManager().registerEvents(new EntityExhaustionListener(), this);
        getServer().getPluginManager().registerEvents(new AnvilListener(), this);
        getServer().getPluginManager().registerEvents(new LeftClickListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(), this);
        getServer().getPluginManager().registerEvents(new RightClickListener(), this);
        getServer().getPluginManager().registerEvents(new ArmorChangeListener(), this);
        getServer().getPluginManager().registerEvents(new HealthChangeListener(), this);
        getServer().getPluginManager().registerEvents(new SneakListener(), this);
        getServer().getPluginManager().registerEvents(new ProjectileHitListener(), this);
        getServer().getPluginManager().registerEvents(new EntityShootBowListener(), this);
        getServer().getPluginManager().registerEvents(new EntityCombustListener(), this);
        getServer().getPluginManager().registerEvents(new DeathListener(), this);

    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
    }


    public static ExtraWeapons getPlugin() {
        return plugin;
    }
}
