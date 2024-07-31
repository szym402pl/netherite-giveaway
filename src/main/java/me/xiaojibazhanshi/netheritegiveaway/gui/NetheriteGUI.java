package me.xiaojibazhanshi.netheritegiveaway.gui;

import me.xiaojibazhanshi.netheritegiveaway.managers.ConfigManager;
import me.xiaojibazhanshi.netheritegiveaway.managers.ItemStackManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class NetheriteGUI {

    private final ConfigManager configManager;
    private final ItemStackManager itemStackManager;
    public static ArrayList<UUID> playersWithGUIOpened;

    public NetheriteGUI(ConfigManager configManager, ItemStackManager itemStackManager) {
        this.configManager = configManager;
        this.itemStackManager = itemStackManager;
        playersWithGUIOpened = new ArrayList<>();
    }

    public Inventory getNetheriteGUI() {
        String name = configManager.getNetheriteGUIName();
        int size = configManager.getNetheriteGUISize();
        Inventory netheriteGUI = Bukkit.createInventory(null, size, name);

        Map<Integer, ItemStack> infoHeadsI = new HashMap<>();
        Map<String, ItemStack> infoHeadsA = new HashMap<>();

        for (Player target : Bukkit.getOnlinePlayers()) {
            int maxedItems = itemStackManager.maxedItemCount(target);

            if (maxedItems == 0) {
                continue;
            }

            List<String> headLore = configManager.getHeadLore().stream()
                    .map(line -> line.replace("{maxed-items}", String.valueOf(maxedItems))).toList();

            ItemStack infoHead = itemStackManager.createInfoSkull(target, headLore);

            infoHeadsI.put(maxedItems, infoHead);
            infoHeadsA.put(target.getName(), infoHead);
        }

        TreeMap<Integer, ItemStack> sortedInfoHeadsI = new TreeMap<>(infoHeadsI);
        TreeMap<String, ItemStack> sortedInfoHeadsA = new TreeMap<>(infoHeadsA);

        if (sortedInfoHeadsI.size() > size) { // Too many heads
            return null;
        }

        int slot = 0;

        if (configManager.isSortedByItemCount()) {
            for (Map.Entry<Integer, ItemStack> entry : sortedInfoHeadsI.entrySet()) {

                if (slot >= size) {
                    break;
                }

                netheriteGUI.setItem(slot, entry.getValue());
                slot++;
            }
        } else {
            for (Map.Entry<String, ItemStack> entry : sortedInfoHeadsA.entrySet()) {

                if (slot >= size) {
                    break;
                }

                netheriteGUI.setItem(slot, entry.getValue());
                slot++;
            }
        }

        for (int i = slot; i < size; i++) {
            netheriteGUI.setItem(i, itemStackManager.getFillerItem());
        }

        return netheriteGUI;
    }
}

