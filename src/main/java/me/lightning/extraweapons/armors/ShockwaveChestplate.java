package me.lightning.extraweapons.armors;

import me.lightning.extraweapons.Cooldown;
import me.lightning.extraweapons.ItemBuilder;
import me.lightning.extraweapons.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Collection;

public class ShockwaveChestplate extends CustomArmor {

    Cooldown cooldown = new Cooldown();

    double duration = config.getDouble("armor.shockwave_chestplate.cooldown");
    double push_strength = config.getDouble("armor.shockwave_chestplate.push");

    @Override
    public String getID() {
        return "shockwave_chestplate";
    }

    @Override
    public ItemStack createItem() {
        return new ItemBuilder(Material.LEATHER_CHESTPLATE)
                .itemName(Component.text("Shockwave Chestplate")
                        .decoration(TextDecoration.BOLD, true)
                        .color(TextColor.color(180, 146, 33)))
                .color(Color.fromRGB(189, 149, 39))
                .flags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DYE)
                .maxDamage(500)
                .lore(
                        Component.text("§6§lABILITY: SHIFT"),
                        Component.text("§7Releases a shockwave that"),
                        Component.text("§7pushes nearby enemies away."),
                        Component.text("§8Cooldown: %ss".formatted(duration).replace(".0","")))
                .build();
    }

    @Override
    public EquipmentSlot getType() {
        return EquipmentSlot.CHEST;
    }

    public void onSneak(PlayerToggleSneakEvent e) {
        if (!e.isSneaking()) return;
        Player player = e.getPlayer();
        Location playerLoc = player.getLocation();

        if (!cooldown.isOver(player)) {
            player.sendMessage(Utils.cooldownMsg(String.valueOf(cooldown.getRemaining(player))));
            return;
        }

        //get the entities
        Collection<LivingEntity> entities = playerLoc.getNearbyLivingEntities(5,5,5);
        entities.remove(player);
        if (entities.isEmpty()) return;

        player.spawnParticle(Particle.SWEEP_ATTACK,playerLoc.clone().add(0,1,0),5,0.5,0.5,0.5,1);

        //apply the push
        for (LivingEntity entity : entities) {
            Vector push = entity.getLocation().toVector().subtract(playerLoc.toVector()).normalize();

            entity.setVelocity(push.multiply(push_strength).add(new Vector(0,0.5,0)));
        }

        cooldown.start(player, 7);
    }

}
