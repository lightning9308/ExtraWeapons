package me.lightning.extraweapons.commands;

import me.lightning.extraweapons.ArmorRegistry;
import me.lightning.extraweapons.ExtraWeapons;
import me.lightning.extraweapons.ItemRegistry;
import me.lightning.extraweapons.armors.CustomArmor;
import me.lightning.extraweapons.items.CustomItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

public class ExtraWeaponCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (!(sender instanceof Player player)) return true;

        String helpMsg = """
                §8§m§l----------------§6§lExtra§Weapons§8§m§l----------------
                §5
                §8§l» §6/extraweapons get <item> §7- §fGet a custom item
                §8§l» §6/extraweapons reload     §7- §fReload configuration
                §8§l» §6/extraweapons menu       §7- §fOpen the custom items menu
                §8§m§l--------------------------------------------
                """;

        if (args.length == 0) {
            player.sendMessage(helpMsg);
            return true;
        }

        String subCommand = args[0];

        if (subCommand.equalsIgnoreCase("get")) {
            if (args.length < 2) {
                player.sendMessage("§8[§6ExtraWeapons§8] §cUsage: /extraweapons get <item>");
                return true;
            }

            String itemName = args[1];

            // item
            CustomItem item = ItemRegistry.get(itemName.toLowerCase());
            if (item != null) {
                player.getInventory().addItem(item.getItem());
                return true;
            }
            // armor
            CustomArmor armor = ArmorRegistry.get(itemName.toLowerCase());
            if (armor != null) {
                player.getInventory().addItem(armor.getItem());
                return true;
            }

            player.sendMessage("§8[§6ExtraWeapons§8] §cItem doesn't exist!");
            return true;
        }

        if (subCommand.equalsIgnoreCase("menu")) {
            Inventory inv = Bukkit.createInventory(player, 36, Component.text("ExtraWeapons"));
            for (CustomArmor armor : ArmorRegistry.getAll()) {
                inv.addItem(armor.getItem());
            }
            for (CustomItem item : ItemRegistry.getAll()) {
                inv.addItem(item.getItem());
            }
            player.openInventory(inv);

            return true;
        }

        if (subCommand.equalsIgnoreCase("reload")) {
            ExtraWeapons.getPlugin().reloadConfig();
            ItemRegistry.load();
            ArmorRegistry.load();
            player.sendMessage("§8[§6ExtraWeapons§8] §aConfiguration reloaded!");

            return true;
        }

        player.sendMessage(helpMsg);

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (args.length == 1) {
            return List.of(
                    "get",
                    "reload",
                    "menu"
            );
        }

        if (args[0].equalsIgnoreCase("get")) {
            return Stream.concat(
                            ItemRegistry.getAll().stream().map(CustomItem::getID),
                            ArmorRegistry.getAll().stream().map(CustomArmor::getID)
            ).toList();
        }
        return List.of();
    }
}
