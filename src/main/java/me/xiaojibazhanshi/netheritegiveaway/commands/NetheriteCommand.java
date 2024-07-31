package me.xiaojibazhanshi.netheritegiveaway.commands;

import me.xiaojibazhanshi.netheritegiveaway.gui.NetheriteGUI;
import me.xiaojibazhanshi.netheritegiveaway.managers.ConfigManager;
import me.xiaojibazhanshi.netheritegiveaway.managers.ItemStackManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class NetheriteCommand implements CommandExecutor {

    private final NetheriteGUI netheriteGUI;
    private final ItemStackManager itemStackManager;
    private final ConfigManager configManager;

    public NetheriteCommand(NetheriteGUI netheriteGUI, ItemStackManager itemStackManager, ConfigManager configManager) {
        this.netheriteGUI = netheriteGUI;
        this.itemStackManager = itemStackManager;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player player)) {
            Bukkit.getLogger().info("Only a player can execute this command!");
            return true;
        }

        ConfigurationSection section = configManager.getCommandSection();

        if (!(player.hasPermission(section.getString("permission"))) || !(player.isOp())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes
                    ('&', section.getString("no-perms-message")));
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Usage: " +  command.getUsage());
            return true;
        }

        switch(args[0].toLowerCase()) {
            case "show" -> {
                player.openInventory(netheriteGUI.getNetheriteGUI());
                NetheriteGUI.playersWithGUIOpened.add(player.getUniqueId());
            }

            case "giveaway" -> {
                ArrayList<ItemStack> itemList = itemStackManager.getItemsFromConfig();
                ArrayList<Player> playerList = new ArrayList<>(Bukkit.getOnlinePlayers());

                int minIndex = Math.min(itemList.size(), playerList.size());
                String globalMessage = section.getString("global-giveaway-message");

                if (!globalMessage.equalsIgnoreCase("empty")) {
                    Bukkit.broadcastMessage(globalMessage);
                }

                for (int i = 0; i < minIndex; i++) {
                    Player target = playerList.get(i);
                    ItemStack item = itemList.get(i);

                    boolean fullInventory = target.getInventory().firstEmpty() == -1;

                    target.sendMessage(section.getString("personal-giveaway-message"));

                    if (section.getBoolean("sound"))
                        target.playSound(target, Sound.ENTITY_PLAYER_LEVELUP, 1, 1);

                    if (fullInventory) {
                        target.getWorld().dropItem(target.getLocation(), item);
                        target.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC
                                + "Inventory full, dropping the item on the ground.");
                    } else {
                        target.getInventory().addItem(item);
                    }
                }
            }

            default -> {
                player.sendMessage(ChatColor.RED + "Usage: " +  command.getUsage());
                return true;
            }
        }

        return true;
    }
}
