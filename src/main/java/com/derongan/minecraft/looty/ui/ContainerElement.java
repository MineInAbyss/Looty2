package com.derongan.minecraft.looty.ui;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.inventory.meta.tags.ItemTagType;
import org.bukkit.plugin.Plugin;

import java.util.Set;

public class ContainerElement extends FillableElement {
    private final Set<NamespacedKey> allowedKeys;
    private Plugin plugin;

    public ContainerElement(int height, int width, Set<NamespacedKey> allowedKeys, Plugin plugin) {
        super(height, width);
        this.allowedKeys = allowedKeys;
        this.plugin = plugin;
    }

    @Override
    public void onPickup(ClickEvent clickEvent) {
        clickEvent.setCancelled(false);
        removeElement(clickEvent.getX(), clickEvent.getY());
    }

    @Override
    public void onPlace(ClickEvent clickEvent) {
        CustomItemTagContainer customTagContainer = clickEvent.getItemOnCursor().getItemMeta().getCustomTagContainer();

        boolean anyMatch = allowedKeys.stream()
                .anyMatch(key -> customTagContainer.hasCustomTag(key, ItemTagType.STRING));

        if (!anyMatch) {
            clickEvent.setCancelled(true);
        } else {
            clickEvent.setCancelled(true);
            setElement(clickEvent.getX(), clickEvent.getY(), Cell.forItemStack(clickEvent.getItemOnCursor()));

            Bukkit.getScheduler()
                    .scheduleSyncDelayedTask(plugin, () -> clickEvent.getRawEvent()
                            .getWhoClicked()
                            .setItemOnCursor(null));
        }
    }

    @Override
    public void onSwap(ClickEvent clickEvent) {
        CustomItemTagContainer customTagContainer = clickEvent.getItemOnCursor().getItemMeta().getCustomTagContainer();

        boolean anyMatch = allowedKeys.stream()
                .anyMatch(key -> customTagContainer.hasCustomTag(key, ItemTagType.STRING));

        if (!anyMatch) {
            clickEvent.setCancelled(true);
        } else {
            clickEvent.setCancelled(false);
            setElement(clickEvent.getX(), clickEvent.getY(), Cell.forItemStack(clickEvent.getItemOnCursor()));
        }
    }
}
