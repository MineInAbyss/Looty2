package com.derongan.minecraft.ui;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.inventory.meta.tags.ItemTagType;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static com.derongan.minecraft.looty.ui.LootyEditorFactory.*;

public class ToolInterceptor implements Element {
    private ContainerElement wrapped;
    private final Plugin plugin;
    private Map<String, BiConsumer<ClickEvent, Element>> handlerMap = new HashMap<>();

    public ToolInterceptor(ContainerElement wrapped, Plugin plugin) {
        this.wrapped = wrapped;
        this.plugin = plugin;
    }

    @Override
    public Size getSize() {
        return wrapped.getSize();
    }

    @Override
    public void draw(GuiRenderer guiRenderer) {
        wrapped.draw(guiRenderer);
    }

    public void registerToolAction(String tool, BiConsumer<ClickEvent, Element> clickedElementConsumer) {
        handlerMap.put(tool, clickedElementConsumer);
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        ItemStack itemOnCursor = clickEvent.getItemOnCursor();
        if (itemOnCursor.hasItemMeta()) {
            CustomItemTagContainer customTagContainer = itemOnCursor.getItemMeta().getCustomTagContainer();
            if (customTagContainer
                    .hasCustomTag(new NamespacedKey(plugin, TYPE_KEY), ItemTagType.STRING)) {

                if (customTagContainer.getCustomTag(new NamespacedKey(plugin, TYPE_KEY), ItemTagType.STRING)
                        .equals(TOOL_VALUE)) {
                    Element element = wrapped.getElement(clickEvent.getX(), clickEvent.getY());

                    String toolType = customTagContainer.getCustomTag(new NamespacedKey(plugin, TOOL_TYPE_KEY), ItemTagType.STRING);

                    handlerMap.getOrDefault(toolType, (click, elem) -> wrapped.onClick(click))
                            .accept(clickEvent, element);

                    return;
                }
            }
        }

        wrapped.onClick(clickEvent);
    }
}
