package me.lightning.extraweapons.items;

import me.lightning.extraweapons.Cooldown;
import me.lightning.extraweapons.ExtraWeapons;
import me.lightning.extraweapons.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FrostOrb extends CustomItem {

    private final Map<UUID, Integer> frost = new HashMap<>();
    Cooldown cooldown = new Cooldown();

    @Override
    public String getID() {
        return "frost_orb";
    }

    @Override
    protected ItemStack createItem() {
        return new ItemBuilder(Material.SNOWBALL)
                .itemName(Component.text("Frost Orb")
                        .color(TextColor.color(116, 204, 244))
                        .decoration(TextDecoration.BOLD, true))
                .flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ADDITIONAL_TOOLTIP)
                .lore(
                        Component.text("§6§lABILITY: RIGHT CLICK"),
                        Component.text("§7Landing 4 consecutive hits"),
                        Component.text("§7freezes the enemy for 1.5s."))
                .build();
    }

    @Override
    public void rightClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        e.setCancelled(true);
        ItemStack frostOrb = player.getInventory().getItemInMainHand();

        if (!cooldown.isOver(player)) return;
        frostOrb.setAmount(frostOrb.getAmount() - 1);

        Snowball snowball = player.launchProjectile(Snowball.class);
        snowball.setMetadata("frost_orb", new FixedMetadataValue(ExtraWeapons.getPlugin(), true));

        cooldown.start(player, 0.5);
    }

    public void onSnowballHit(ProjectileHitEvent e) {
        if (!(e.getHitEntity() instanceof LivingEntity entity)) return;
        UUID entityUUID = entity.getUniqueId();

        int currentHits = frost.getOrDefault(entityUUID, 1);

        if (currentHits >= 4) {
            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS,30,10,false,false, false));
            entity.setFreezeTicks(170);
            Bukkit.getScheduler().runTaskLater(ExtraWeapons.getPlugin(), () -> {

                if (entity.isValid()) {
                    entity.setFreezeTicks(0);
                }
            }, 30L);

            frost.remove(entityUUID);
        } else {
            frost.put(entityUUID, currentHits + 1);
        }

    }
}
