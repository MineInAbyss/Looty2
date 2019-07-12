package com.derongan.minecraft.looty.ui;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

import javax.inject.Inject;

public class GUIListener implements Listener {
    @Inject
    public GUIListener() {
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {
        InventoryHolder holder = inventoryClickEvent.getClickedInventory().getHolder();
        if (holder instanceof GUIHolder) {
            switch (inventoryClickEvent.getAction()) {
                case PICKUP_ALL:
                case PICKUP_HALF:
                case PICKUP_SOME:
                case PICKUP_ONE:
                    ((GUIHolder) holder).onPickup(ClickEvent.createClickEvent(inventoryClickEvent));
                    break;
                case SWAP_WITH_CURSOR:
                    ((GUIHolder) holder).onSwap(ClickEvent.createClickEvent(inventoryClickEvent));
                    break;
                case PLACE_ALL:
                case PLACE_ONE:
                case PLACE_SOME:
                    ((GUIHolder) holder).onPlace(ClickEvent.createClickEvent(inventoryClickEvent));
                    break;
            }
        }
    }
}
