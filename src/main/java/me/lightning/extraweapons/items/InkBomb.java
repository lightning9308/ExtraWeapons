package me.lightning.extraweapons.items;

import me.lightning.extraweapons.Cooldown;
import me.lightning.extraweapons.ExtraWeapons;
import me.lightning.extraweapons.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class InkBomb extends CustomItem {

    private final FileConfiguration config = ExtraWeapons.getPlugin().getConfig();
    double duration = config.getDouble("items.ink_bomb.cooldown");
    String cooldownMsg = config.getString("messages.cooldown");

    Cooldown cooldown = new Cooldown();
    @Override
    public String getID() {
        return "ink_bomb";
    }

    @Override
    protected ItemStack createItem() {
        return new ItemBuilder(Material.INK_SAC)
                .itemName(Component.text("Ink Bomb")
                        .color(TextColor.color(36, 37, 38))
                        .decoration(TextDecoration.BOLD, true))
                .flags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP, ItemFlag.HIDE_ATTRIBUTES)
                .lore(
                        Component.text("§6§lABILITY: RIGHT CLICK"),
                        Component.text("§7Launches an ink bomb, blinding"),
                        Component.text("§7all enemies caught in the blast."),
                        Component.text("§8Cooldown: %ss".formatted(duration).replace(".0","")))
                .build();
    }

    @Override
    public void rightClick(PlayerInteractEvent e) {
        e.setCancelled(true);
        Player player = e.getPlayer();
        ItemStack inkOrbItem = player.getInventory().getItemInMainHand();

        if (cooldownMsg != null) {
            cooldownMsg = ChatColor.translateAlternateColorCodes('&',cooldownMsg);
        }
        if (!cooldown.isOver(player)) {
            player.sendMessage(cooldownMsg.replace("%time%",String.valueOf(cooldown.getRemaining(player))));
            return;
        }

        inkOrbItem.setAmount(inkOrbItem.getAmount() - 1);

        Snowball inkOrb = player.launchProjectile(Snowball.class);
        inkOrb.setMetadata("ink_bomb", new FixedMetadataValue(ExtraWeapons.getPlugin(), true));
        inkOrb.setItem(new ItemStack(Material.INK_SAC));

        cooldown.start(player, duration);

    }

    public void onProjectileHit(ProjectileHitEvent e) {
        e.setCancelled(true);
        if (!(e.getEntity().getShooter() instanceof Player player)) return;

        Location hitPoint = e.getEntity().getLocation().clone().add(0,1.5,0);

        for (LivingEntity entity : hitPoint.getNearbyLivingEntities(3,3,3)) {
            if (player.equals(entity)) continue;

            entity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,40,2,false,false, false));
        }

        e.getEntity().getWorld().spawnParticle(Particle.SQUID_INK, hitPoint, 80, 1.5, 1, 1.5, 0);
    }
}
