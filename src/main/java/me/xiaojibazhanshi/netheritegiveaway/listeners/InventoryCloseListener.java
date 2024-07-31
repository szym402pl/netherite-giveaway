package me.xiaojibazhanshi.netheritegiveaway.listeners;

import me.xiaojibazhanshi.netheritegiveaway.gui.NetheriteGUI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryCloseListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {

        NetheriteGUI.playersWithGUIOpened.remove(event.getPlayer().getUniqueId());

    }
}
