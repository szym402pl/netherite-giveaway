package me.xiaojibazhanshi.netheritegiveaway.managers;

import me.xiaojibazhanshi.netheritegiveaway.NetheriteGiveaway;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ItemStackManager {

    //private final NetheriteGiveaway main;
    private final ConfigManager configManager;
    private final NamespacedKey persistentDataKey;

    public ItemStackManager(NetheriteGiveaway main, ConfigManager configManager) {
        //this.main = main;
        this.configManager = configManager;
        this.persistentDataKey = new NamespacedKey(main, "maxed_item");
    }

    public ArrayList<ItemStack> getItemsFromConfig() {
        ArrayList<ItemStack> items = new ArrayList<>();
        ConfigurationSection itemsSection = configManager.getItemsSection();

        for (String key : itemsSection.getKeys(false)) {
            ConfigurationSection itemSection = itemsSection.getConfigurationSection(key);

            if (itemSection != null) {
                ItemStack itemStack = createItemStack(itemSection);
                items.add(itemStack);
            }
        }

        return items;
    }

    public ItemStack getFillerItem() {
        ItemStack filler = new ItemStack(configManager.getFillerMaterial());
        ItemMeta fillerMeta = filler.getItemMeta();

        fillerMeta.setDisplayName(" ");
        fillerMeta.setHideTooltip(true);
        fillerMeta.setLore(Arrays.stream(new String[]{""}).toList());

        filler.setItemMeta(fillerMeta);

        return filler;
    }

    private ItemStack createItemStack(ConfigurationSection section) {
        String materialName = section.getKeys(false).iterator().next();
        Material material = Material.valueOf(materialName);
        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();

        if (meta != null) {

            if (section.contains("display-name")) {
                meta.setDisplayName(ChatColor.translateAlternateColorCodes
                        ('&', section.getString("display-name")));
            }

            if (section.contains("lore")) {
                List<String> lore = section.getStringList("lore").stream()
                        .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                        .collect(Collectors.toList());

                meta.setLore(lore);
            }

            if (section.contains("enchants")) {
                ConfigurationSection enchantsSection = section.getConfigurationSection("enchants");

                for (String enchant : enchantsSection.getKeys(false)) {
                    meta.addEnchant(Enchantment.getByName(enchant), enchantsSection.getInt(enchant), true);
                }
            }

            PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
            dataContainer.set(persistentDataKey, PersistentDataType.STRING, "maxed_item_data");

            itemStack.setItemMeta(meta);
        }

        return itemStack;
    }

    public int maxedItemCount(Player player) {
        Inventory inv = player.getInventory();

        return (int) Arrays.stream(inv.getContents())
                .filter(item -> item != null && item.hasItemMeta())
                .map(ItemStack::getItemMeta).filter(Objects::nonNull)
                .map(ItemMeta::getPersistentDataContainer)
                .filter(dataContainer -> dataContainer.has(persistentDataKey, PersistentDataType.STRING))
                .count();
    }

    public ItemStack createInfoSkull(Player player, List<String> lore) {
        ItemStack infoSkull = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta skullMeta = (SkullMeta) infoSkull.getItemMeta();
        assert skullMeta != null;

        skullMeta.setOwningPlayer(player);
        skullMeta.setLore(lore);

        infoSkull.setItemMeta(skullMeta);

        return infoSkull;
    }
}

