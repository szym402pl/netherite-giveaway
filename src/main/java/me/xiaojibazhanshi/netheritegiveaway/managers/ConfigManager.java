package me.xiaojibazhanshi.netheritegiveaway.managers;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.stream.Collectors;

public class ConfigManager {
    private final FileConfiguration config;
    private ConfigurationSection itemsSection;
    private int netheriteGUISize;
    private Material fillerMaterial;
    private String netheriteGUIName;
    private List<String> headLore;
    private boolean sortByItemCount;
    private ConfigurationSection commandSection;

    public ConfigManager(JavaPlugin plugin) {
        plugin.saveDefaultConfig();
        this.config = plugin.getConfig();
        loadConfigValues();
    }

    private void loadConfigValues() {
        commandSection = config.getConfigurationSection("netherite-command");
        sortByItemCount = config.getString("netherite-gui.sort-by").equalsIgnoreCase("itemcount");
        netheriteGUISize = config.getInt("netherite-gui.size");
        fillerMaterial = Material.valueOf(config.getString("netherite-gui.filler-material"));
        itemsSection = config.getConfigurationSection("giveaway-items");
        netheriteGUIName = ChatColor.translateAlternateColorCodes
                ('&', config.getString("netherite-gui.name"));
        headLore = config.getStringList("netherite-gui.head-lore").stream()
                .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                .collect(Collectors.toList());
    }

    public ConfigurationSection getCommandSection() {
        return commandSection;
    }

    public ConfigurationSection getItemsSection() {
        return itemsSection;
    }

    public String getNetheriteGUIName() {
        return netheriteGUIName;
    }

    public int getNetheriteGUISize() {
        return netheriteGUISize;
    }

    public Material getFillerMaterial() {
        return fillerMaterial;
    }

    public boolean isSortedByItemCount() {
        return sortByItemCount;
    }

    public List<String> getHeadLore() {
        return headLore;
    }
}
