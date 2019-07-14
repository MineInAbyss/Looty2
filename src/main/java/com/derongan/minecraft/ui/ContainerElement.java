package com.derongan.minecraft.ui;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.ItemTagType;
import org.bukkit.plugin.Plugin;

import java.util.Set;

import static com.derongan.minecraft.looty.ui.LootyEditorFactory.TYPE_KEY;

public class ContainerElement extends FillableElement {
    private final Set<String> allowedKeys;
    private Plugin plugin;

    public ContainerElement(int height, int width, Set<String> allowedKeys, Plugin plugin) {
        super(height, width);
        this.allowedKeys = allowedKeys;
        this.plugin = plugin;
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        if (allowedKeys == null) {
            clickEvent.setCancelled(false);
            setElement(clickEvent.getX(), clickEvent.getY(), Cell.forItemStack(clickEvent.getItemOnCursor().clone()));
            return;
        }

        clickEvent.setCancelled(false);
        if (clickEvent.getItemOnCursor().getType() == Material.AIR) {
            removeElement(clickEvent.getX(), clickEvent.getY());
            return;
        }

        ItemStack itemOnCursor = clickEvent.getItemOnCursor();

        if (!itemOnCursor.hasItemMeta()) {
            clickEvent.setCancelled(true);
            return;
        }

        if (!itemOnCursor.getItemMeta()
                .getCustomTagContainer()
                .hasCustomTag(new NamespacedKey(plugin, TYPE_KEY), ItemTagType.STRING)) {
            clickEvent.setCancelled(true);
            return;
        }

        ItemMeta meta = itemOnCursor.getItemMeta();

        String itemType = meta.getCustomTagContainer()
                .getCustomTag(new NamespacedKey(plugin, TYPE_KEY), ItemTagType.STRING);


        // TODO make sure byte is set to true
        boolean allowed = allowedKeys.contains(itemType);


        if (allowed) {
            setElement(clickEvent.getX(), clickEvent.getY(), Cell.forItemStack(itemOnCursor.clone()));
        } else {
            clickEvent.setCancelled(true);
        }
    }

}
