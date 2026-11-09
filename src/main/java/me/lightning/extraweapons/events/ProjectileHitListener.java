package me.lightning.extraweapons.events;

import me.lightning.extraweapons.ItemRegistry;
import me.lightning.extraweapons.items.miscellaneous.WebShooter;
import me.lightning.extraweapons.items.throwables.FrostOrb;
import me.lightning.extraweapons.items.throwables.InkBomb;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class ProjectileHitListener implements Listener {
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        Projectile projectile = e.getEntity();

        if (projectile.hasMetadata("frost_orb")) {
            ((FrostOrb)ItemRegistry.get("frost_orb")).onProjectileHit(e);
        }
        if (projectile.hasMetadata("ink_bomb")) {
            ((InkBomb)ItemRegistry.get("ink_bomb")).onProjectileHit(e);
        }
        if (projectile.hasMetadata("web_shooter")) {
            ((WebShooter)ItemRegistry.get("web_shooter")).onProjectileHit(e);
        }
    }
}
