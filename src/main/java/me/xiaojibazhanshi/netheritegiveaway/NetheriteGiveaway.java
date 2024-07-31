package me.xiaojibazhanshi.netheritegiveaway;

import me.xiaojibazhanshi.netheritegiveaway.commands.NetheriteCMDTabCompleter;
import me.xiaojibazhanshi.netheritegiveaway.commands.NetheriteCommand;
import me.xiaojibazhanshi.netheritegiveaway.gui.NetheriteGUI;
import me.xiaojibazhanshi.netheritegiveaway.listeners.GUIClickListener;
import me.xiaojibazhanshi.netheritegiveaway.listeners.InventoryCloseListener;
import me.xiaojibazhanshi.netheritegiveaway.managers.ConfigManager;
import me.xiaojibazhanshi.netheritegiveaway.managers.ItemStackManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class NetheriteGiveaway extends JavaPlugin {

    private ConfigManager configManager;
    private ItemStackManager itemStackManager;
    private NetheriteGUI netheriteGUI;

    @Override
    public void onEnable() {
        getLogger().info("[NetheriteGiveaway] Thanks for using my plugin! ~XiaoJibaZhanshi");

        configManager = new ConfigManager(this);
        itemStackManager = new ItemStackManager(this, configManager);
        netheriteGUI = new NetheriteGUI(configManager, itemStackManager);

        getCommand("netherite").setExecutor(new NetheriteCommand
                (netheriteGUI, itemStackManager, configManager));
        getCommand("netherite").setTabCompleter(new NetheriteCMDTabCompleter());

        Bukkit.getPluginManager().registerEvents(new InventoryCloseListener(), this);
        Bukkit.getPluginManager().registerEvents(new GUIClickListener(), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("[NetheriteGiveaway] Disabling the plugin...");
    }
}
