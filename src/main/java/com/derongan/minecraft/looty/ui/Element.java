package com.derongan.minecraft.looty.ui;

import com.derongan.minecraft.looty.ui.cursor.Cursor;
import com.google.common.collect.ImmutableSet;

import java.util.Set;

public interface Element {
    Size getSize();

    void draw(GuiRenderer guiRenderer);

    default void onPickup(ClickEvent clickEvent) {
        clickEvent.setCancelled(true);
    }

    default void onPlace(ClickEvent clickEvent) {
        clickEvent.setCancelled(true);
    }

    default void onSwap(ClickEvent clickEvent) {
        clickEvent.setCancelled(true);
    }

    default Set<Cursor> accepts() {
        return ImmutableSet.of();
    }

    default void accept(Cursor cursor){
    }
}
