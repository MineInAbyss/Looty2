package com.derongan.minecraft.ui;

import de.erethon.headlib.HeadLib;

public class VerticalScrollingElement implements Element {
    private final int height;
    private Element wrapped;
    private int scrollFactor = 0;

    public VerticalScrollingElement(int height, Element wrapped) {
        this.height = height;
        this.wrapped = wrapped;
    }

    @Override
    public Size getSize() {
        return Size.create(wrapped.getSize().getWidth() + 1, height);
    }

    @Override
    public void draw(GuiRenderer guiRenderer) {
        wrapped.draw((x, y, itemStack) -> guiRenderer.renderAt(x, y - scrollFactor, itemStack));

        guiRenderer.renderAt(getSize().getWidth() - 1, 0, HeadLib.WOODEN_ARROW_UP.toItemStack("Scroll Up"));
        guiRenderer.renderAt(getSize().getWidth() - 1, height - 1, HeadLib.WOODEN_ARROW_DOWN.toItemStack("Scroll Down"));
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        if (clickEvent.getX() == getSize().getWidth() - 1 && clickEvent.getY() == height - 1 && scrollFactor + height - 1 < wrapped
                .getSize()
                .getHeight()) {
            scrollFactor++;
        } else if (clickEvent.getX() == getSize().getWidth() - 1 && clickEvent.getY() == 0 && scrollFactor != 0) {
            scrollFactor--;
        } else if (clickEvent.getX() != getSize().getWidth() - 1 && clickEvent.getY() != getSize().getHeight() - 1) {
            wrapped.onClick(ClickEvent.offsetClickEvent(clickEvent, 0, -scrollFactor));
        }
    }
}
