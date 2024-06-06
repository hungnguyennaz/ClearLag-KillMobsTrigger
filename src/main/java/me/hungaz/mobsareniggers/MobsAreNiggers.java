package me.hungaz.mobsareniggers;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class MobsAreNiggers extends JavaPlugin {

    private double tpsThreshold;
    private int checkInterval;
    private boolean broadcastEnabled;
    private String broadcastMessage;

    @Override
    public void onEnable() {
        if (!isBob7lClearLagInstalled()) {
            getLogger().severe("Sir I need the ClearLag plugin by bob7l, you have to download the right one.");
            getLogger().severe("Please download the correct version from: https://www.spigotmc.org/resources/clearlagg.68271/");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        saveDefaultConfig();
        loadConfig();
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new TheHellNigga(), 0, checkInterval * 20L);
    }

    private boolean isBob7lClearLagInstalled() {
        Plugin clearLag = getServer().getPluginManager().getPlugin("ClearLag");
        if (clearLag != null) {
            PluginDescriptionFile desc = clearLag.getDescription();
            return "bob7l".equals(desc.getAuthors().get(0)) && "me.minebuilders.clearlag.Clearlag".equals(desc.getMain());
        }
        return false;
    }

    private void loadConfig() {
        FileConfiguration config = getConfig();
        tpsThreshold = config.getDouble("tps-threshold", 15.0);
        checkInterval = config.getInt("check-interval", 60);
        broadcastEnabled = config.getBoolean("broadcast-enabled", true);
        broadcastMessage = config.getString("broadcast-message", "§c§lCẢNH BÁO §eTPS server đang bị giảm đáng kể, mobs sẽ bị clear để cải thiện.");
    }

    private class TheHellNigga implements Runnable {
        @Override
        public void run() {
            double currentTPS = getServer().getTPS()[0];
            if (currentTPS < tpsThreshold) {
                Bukkit.getScheduler().runTask(MobsAreNiggers.this, () -> {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lagg killmobs");
                    if (broadcastEnabled) {
                        Bukkit.broadcastMessage(broadcastMessage);
                    }
                });
            }
        }
    }
}