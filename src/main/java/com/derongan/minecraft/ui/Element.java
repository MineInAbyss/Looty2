package com.derongan.minecraft.ui;

public interface Element {
    Size getSize();

    void draw(GuiRenderer guiRenderer);

    default void onClick(ClickEvent clickEvent) {
        clickEvent.setCancelled(true);
    }
}
