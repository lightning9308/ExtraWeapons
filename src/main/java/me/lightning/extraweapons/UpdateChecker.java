package me.lightning.extraweapons;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.apache.maven.artifact.versioning.ComparableVersion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class UpdateChecker {

    private static UpdateChecker instance;

    private final JavaPlugin plugin;
    private volatile String latestVersion;

    public UpdateChecker(JavaPlugin plugin) {
        this.plugin = plugin;
        instance = this;
        check();
    }

    private void check() {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try {
                URL url = new URL("https://api.modrinth.com/v2/project/extra_weapons/version");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("User-Agent", "ExtraWeapons/" + plugin.getDescription().getVersion());
                connection.setRequestProperty("Accept", "application/json");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                if (connection.getResponseCode() != 200) {
                    plugin.getLogger().warning("Unable to check for updates: HTTP " + connection.getResponseCode());
                    return;
                }

                try (InputStreamReader reader = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)) {
                    JsonArray versions = JsonParser.parseReader(reader).getAsJsonArray();

                    if (versions.isEmpty()) return;

                    String latest = null;
                    String latestDate = "";

                    for (int i = 0; i < versions.size(); i++) {
                        JsonObject version = versions.get(i).getAsJsonObject();
                        String date = version.get("date_published").getAsString();

                        if (latest == null || date.compareTo(latestDate) > 0) {
                            latest = version.get("version_number").getAsString();
                            latestDate = date;
                        }
                    }

                    if (latest != null) {
                        String finalLatest = latest;
                        this.latestVersion = finalLatest;

                        Bukkit.getScheduler().runTask(plugin, () -> {
                            notifyConsole(finalLatest);
                            Bukkit.getOnlinePlayers().forEach(this::sendUpdateMessage);
                        });
                    }
                }
            } catch (IOException | RuntimeException e) {
                plugin.getLogger().warning("Unable to check for updates: " + e.getMessage());
            }
        });
    }

    private boolean isUpdateAvailable(String latest) {
        return new ComparableVersion(latest)
                .compareTo(new ComparableVersion(plugin.getDescription().getVersion())) > 0;
    }

    private void notifyConsole(String latest) {
        String current = plugin.getDescription().getVersion();

        if (!isUpdateAvailable(latest)) {
            plugin.getLogger().info("You are running the latest version of ExtraWeapons.");
            return;
        }

        plugin.getLogger().warning("--------------------------------------------------");
        plugin.getLogger().warning("A new version of ExtraWeapons is available!");
        plugin.getLogger().warning("Current version: " + current);
        plugin.getLogger().warning("Latest version: " + latest);
        plugin.getLogger().warning("Download: https://modrinth.com/plugin/extra_weapons");
        plugin.getLogger().warning("--------------------------------------------------");
    }

    public static void notifyPlayer(Player player) {
        UpdateChecker checker = instance;
        if (checker == null) return;

        checker.sendUpdateMessage(player);
    }

    private void sendUpdateMessage(Player player) {
        String latest = this.latestVersion;
        if (latest == null) return;
        if (!isUpdateAvailable(latest)) return;

        String current = plugin.getDescription().getVersion();
        String url = "https://modrinth.com/plugin/extra_weapons";

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (!player.isOnline()) return;
            if (!player.isOp() && !player.hasPermission("extraweapons.admin")) return;

            player.sendMessage("§8[§6ExtraWeapons§8] §7A new version is available: §f" + latest + " §7(you are on §f" + current + "§7)");
            player.sendMessage(LegacyComponentSerializer.legacySection().deserialize("§8[§6ExtraWeapons§8] §7Download on ")
                    .append(LegacyComponentSerializer.legacySection().deserialize("§aModrinth")
                                    .clickEvent(ClickEvent.openUrl(url))));
        }, 20);
    }
}
