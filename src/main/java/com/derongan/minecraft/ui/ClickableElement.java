package com.derongan.minecraft.ui;

import java.util.function.Consumer;

public class ClickableElement implements Element {
    private Element wrapped;
    private Consumer<ClickEvent> pickupAction;

    public ClickableElement(Element wrapped) {
        this.wrapped = wrapped;
        this.pickupAction = a -> a.setCancelled(true);
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
        pickupAction.accept(clickEvent);
    }

    public void setClickAction(Consumer<ClickEvent> pickupAction) {
        this.pickupAction = pickupAction;
    }
}
