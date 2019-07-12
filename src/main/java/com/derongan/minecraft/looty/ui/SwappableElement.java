package com.derongan.minecraft.looty.ui;

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
    public void onPickup(ClickEvent clickEvent) {
        wrapped.onPickup(clickEvent);
    }

    @Override
    public void onPlace(ClickEvent clickEvent) {
        wrapped.onPlace(clickEvent);
    }

    @Override
    public void onSwap(ClickEvent clickEvent) {
        wrapped.onSwap(clickEvent);
    }

    public void swap(Element element) {
        this.wrapped = element;
    }

}
