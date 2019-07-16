package com.derongan.minecraft.ui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Cell implements Element {
    public ItemStack itemStack;
    private boolean dirty = true;

    public Cell(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public static Element forMaterial(Material material) {
        return forMaterial(material, "");
    }

    public static Element forMaterial(Material material, String name) {
        ItemStack itemStack = new ItemStack(material);

        ItemMeta meta = Bukkit.getItemFactory().getItemMeta(material);

        meta.setDisplayName(name);

        itemStack.setItemMeta(meta);

        return new Cell(itemStack);
    }

    public static Element forItemStack(ItemStack itemStack) {
        return new Cell(itemStack);
    }

    public static Element forItemStack(ItemStack itemStack, String name) {
        ItemMeta meta = itemStack.getItemMeta();

        meta.setDisplayName(name);

        itemStack.setItemMeta(meta);

        return new Cell(itemStack);
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        clickEvent.setCancelled(true);
    }

    @Override
    public Size getSize() {
        return Size.create(1, 1);
    }

    @Override
    public void draw(GuiRenderer guiRenderer) {
        guiRenderer.renderAt(0, 0, itemStack);
        dirty = false;
    }
}
