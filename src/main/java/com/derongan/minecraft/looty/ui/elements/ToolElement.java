package com.derongan.minecraft.looty.ui.elements;

import com.derongan.minecraft.guiy.gui.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.ItemTagType;
import org.bukkit.plugin.Plugin;

import static com.derongan.minecraft.guiy.GuiyKeys.TYPE_KEY;
import static com.derongan.minecraft.looty.ui.LootyEditorFactory.TOOL_TYPE_KEY;
import static com.derongan.minecraft.looty.ui.LootyEditorFactory.TOOL_VALUE;

public class ToolElement implements Element {

    private final Plugin plugin;
    private Element wrapped;

    public ToolElement(Material material, String name, String id, Plugin plugin) {
        this.plugin = plugin;

        this.wrapped = buildButton(material, name, id);
    }

    @Override
    public Size getSize() {
        return wrapped.getSize();
    }

    @Override
    public void draw(GuiRenderer guiRenderer) {
        wrapped.draw(guiRenderer);
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        wrapped.onClick(clickEvent);
    }

    private ClickableElement buildButton(Material material, String name, String id) {
        ItemStack itemStack = new ItemStack(material);

        ItemMeta meta = Bukkit.getItemFactory().getItemMeta(material);

        meta.getCustomTagContainer()
                .setCustomTag(new NamespacedKey(plugin, TYPE_KEY), ItemTagType.STRING, TOOL_VALUE);
        meta.getCustomTagContainer()
                .setCustomTag(new NamespacedKey(plugin, TOOL_TYPE_KEY), ItemTagType.STRING, id);

        itemStack.setItemMeta(meta);

        ClickableElement clickableElement = new ClickableElement(Cell.forItemStack(itemStack, name));

        clickableElement.setClickAction(clickEvent -> clickEvent.setCancelled(false));

        return clickableElement;
    }
}
