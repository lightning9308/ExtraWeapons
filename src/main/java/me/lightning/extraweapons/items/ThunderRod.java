package me.lightning.extraweapons.items;

import me.lightning.extraweapons.Cooldown;
import me.lightning.extraweapons.ItemBuilder;
import me.lightning.extraweapons.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;

public class ThunderRod extends CustomItem {

    Cooldown cooldown = new Cooldown();

    double damage = config.getDouble("items.thunder_rod.damage");
    double duration = config.getDouble("items.thunder_rod.cooldown");

    @Override
    public String getID() {
        return "thunder_rod";
    }

    @Override
    protected ItemStack createItem() {
        return new ItemBuilder(Material.LIGHTNING_ROD)
                .itemName(Component.text("Thunder Rod")
                        .color(TextColor.color(225, 205, 0))
                        .decoration(TextDecoration.BOLD, true))
                .flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ADDITIONAL_TOOLTIP)
                .lore(
                        Component.text("§6§lABILITY: RIGHT CLICK"),
                        Component.text("§7Strikes lightning at the target location,"),
                        Component.text("§7dealing §c%s damage §7to nearby enemies.".formatted(damage).replace(".0","")),
                        Component.text("§8Cooldown: %ss".formatted(duration).replace(".0","")))
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

       //get the target location
        RayTraceResult rayTraceBlocks = player.rayTraceBlocks(10);
        RayTraceResult rayTraceEntity = player.rayTraceEntities(10);
        Location target = null;

        if (rayTraceBlocks != null) {
             target = rayTraceBlocks.getHitPosition().toLocation(player.getWorld());
        } else if (rayTraceEntity != null) {
            target = rayTraceEntity.getHitPosition().toLocation(player.getWorld());
        }

        //summon lightning and deal the dmg
        if (target != null) {
            player.getWorld().strikeLightningEffect(target);
            for (LivingEntity entity : target.getNearbyLivingEntities(2,2,2)) {
                if (entity == player) continue;

                entity.damage(damage, player);
                entity.setFireTicks(60);
            }
            cooldown.start(player,duration);
        }
    }
}
