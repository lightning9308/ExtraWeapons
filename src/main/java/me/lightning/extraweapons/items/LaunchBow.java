package me.lightning.extraweapons.items;

import me.lightning.extraweapons.Cooldown;
import me.lightning.extraweapons.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class LaunchBow extends CustomItem {


    double duration = config.getDouble("items.launch_bow.cooldown");
    String cooldownMsg = config.getString("messages.cooldown");

    Cooldown cooldown = new Cooldown();

    @Override
    public String getID() {
        return "launch_bow";
    }

    @Override
    protected ItemStack createItem() {
        return new ItemBuilder(Material.BOW)
                .itemName(Component.text("Launch Bow")
                        .color(TextColor.color(123, 174, 127))
                        .decoration(TextDecoration.BOLD, true))
                .flags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP, ItemFlag.HIDE_ATTRIBUTES)
                .lore(
                        Component.text("§6§lPASSIVE"),
                        Component.text("§7Launches the shooter forward"),
                        Component.text("§7instead of firing."),
                        Component.text("§8Cooldown: %ss".formatted(duration).replace(".0","")))
                .build();
    }

    public void onBowShoot(EntityShootBowEvent e) {
        Player shooter = (Player) e.getEntity();
        Entity arrow = e.getProjectile();
        arrow.remove();

        if (cooldownMsg != null) {
            cooldownMsg = ChatColor.translateAlternateColorCodes('&',cooldownMsg);
        }
        if (!cooldown.isOver(shooter)) {
            shooter.sendMessage(cooldownMsg.replace("%time%",String.valueOf(cooldown.getRemaining(shooter))));
            return;
        }

        Vector launch = arrow.getVelocity();
        shooter.setVelocity(launch);

        cooldown.start(shooter, duration);
    }
}
