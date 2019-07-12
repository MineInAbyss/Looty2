package com.derongan.minecraft.looty.ui.cursor;

import com.derongan.minecraft.looty.ui.Cell;
import com.derongan.minecraft.looty.ui.Element;
import com.derongan.minecraft.looty.ui.GUIModule;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.inventory.meta.tags.ItemTagType;

import javax.inject.Inject;

public class CursorItemFactory {

    private final NamespacedKey guiKey;

    @Inject
    public CursorItemFactory(@GUIModule.GuiKey NamespacedKey guiKey) {
        this.guiKey = guiKey;
    }

    public Element createCursor(Material material, String name) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = Bukkit.getItemFactory().getItemMeta(material);

        itemMeta.setDisplayName(name);

        CustomItemTagContainer tagContainer = itemMeta.getCustomTagContainer();

        tagContainer.setCustomTag(guiKey, ItemTagType.STRING, name);


        itemStack.setItemMeta(itemMeta);

        return Cell.forItemStack(itemStack);
    }
}
