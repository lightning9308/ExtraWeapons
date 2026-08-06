package me.lightning.extraweapons.items;

import me.lightning.extraweapons.Cooldown;
import me.lightning.extraweapons.ExtraWeapons;
import me.lightning.extraweapons.ItemBuilder;
import me.lightning.extraweapons.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class WebShooter extends CustomItem {

    double duration = config.getDouble("items.web_shooter.cooldown", 8);
    Cooldown cooldown = new Cooldown();

    @Override
    public String getID() {
        return "web_shooter";
    }

    @Override
    protected ItemStack createItem() {
        return new ItemBuilder(Material.STRING)
                .itemName(Component.text("Web Shooter")
                        .color(TextColor.color(92, 92, 92))
                        .decoration(TextDecoration.BOLD, true))
                .flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ADDITIONAL_TOOLTIP)
                .maxStackSize(1)
                .lore(
                        Component.text("§6§lABILITY: RIGHT CLICK"),
                        Component.text("§7Shoots a web projectile "),
                        Component.text("§7that spawns cobwebs on impact."),
                        Component.text("§8Cooldown: %ss".formatted(duration).replace(".0", "")))
                .build();
    }

    @Override
    public void rightClick(PlayerInteractEvent e) {
        e.setCancelled(true);
        Player player = e.getPlayer();

        if (!cooldown.isOver(player)) {
            player.sendMessage(Utils.cooldownMsg(String.valueOf(cooldown.getRemaining(player))));
            return;
        }

        Snowball webShooterProj = player.launchProjectile(Snowball.class);

        webShooterProj.setItem(ItemStack.of(Material.COBWEB));
        webShooterProj.setMetadata("web_shooter", new FixedMetadataValue(ExtraWeapons.getPlugin(), true));
        webShooterProj.setGravity(false);
        webShooterProj.setVelocity(webShooterProj.getVelocity().multiply(2));

        cooldown.start(player, duration);
    }

    public void onProjectileHit(ProjectileHitEvent e) {
        Block block;

        if (e.getHitEntity() != null) {
            block = e.getHitEntity().getLocation().getBlock();
        } else if (e.getHitBlock() != null) {
            block = e.getHitBlock().getRelative(BlockFace.UP);
        } else {
            return;
        }

        //middle block
        if (!block.isSolid()) {
            block.setType(Material.COBWEB);
        }

        //sides
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (x == 0 && z == 0) continue;
                Block relative = block.getRelative(x, 0, z);
                if (!relative.isSolid() && Math.random() <= 0.6) {
                    relative.setType(Material.COBWEB);
                }
            }
        }
    }
}
