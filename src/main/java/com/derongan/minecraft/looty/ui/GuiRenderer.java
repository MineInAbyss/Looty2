package com.derongan.minecraft.looty.ui;

import org.bukkit.inventory.ItemStack;

@FunctionalInterface
public interface GuiRenderer {
    void renderAt(int x, int y, ItemStack itemStack);
}
