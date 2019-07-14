package com.derongan.minecraft.ui;

import org.bukkit.inventory.ItemStack;

@FunctionalInterface
public interface GuiRenderer {
    void renderAt(int x, int y, ItemStack itemStack);
}
