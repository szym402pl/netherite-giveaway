package me.xiaojibazhanshi.netheritegiveaway.listeners;

import me.xiaojibazhanshi.netheritegiveaway.gui.NetheriteGUI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUIClickListener implements Listener {

    @EventHandler
    public void onGUIClick(InventoryClickEvent event) {

        if (NetheriteGUI.playersWithGUIOpened.contains(event.getWhoClicked().getUniqueId())) {
            event.setCancelled(true);
        }

    }

}
