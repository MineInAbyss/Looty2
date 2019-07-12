package com.derongan.minecraft.looty.ui;

import java.util.function.Consumer;

public class ClickableElement implements Element {
    private Element wrapped;
    private Consumer<ClickEvent> pickupAction;
    private Consumer<ClickEvent> dropAction;
    private Consumer<ClickEvent> swapAction;

    public ClickableElement(Element wrapped) {
        this.wrapped = wrapped;
        Consumer<ClickEvent> cancelConsumer = (a) -> a.setCancelled(true);
        this.pickupAction = cancelConsumer;
        this.dropAction = cancelConsumer;
        this.swapAction = cancelConsumer;
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
        pickupAction.accept(clickEvent);
    }

    @Override
    public void onPlace(ClickEvent clickEvent) {
        dropAction.accept(clickEvent);
    }

    @Override
    public void onSwap(ClickEvent clickEvent) {
        swapAction.accept(clickEvent);
    }

    public void setPickupAction(Consumer<ClickEvent> pickupAction) {
        this.pickupAction = pickupAction;
    }

    public void setPlaceAction(Consumer<ClickEvent> dropAction) {
        this.dropAction = dropAction;
    }

    public void setSwapAction(Consumer<ClickEvent> swapAction) {
        this.swapAction = swapAction;
    }
}
