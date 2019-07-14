package com.derongan.minecraft.ui;

public class SwappableElement implements Element {
    private Element wrapped;

    public SwappableElement(Element wrapped) {
        this.wrapped = wrapped;
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

    public void swap(Element element) {
        this.wrapped = element;
    }
}
