package com.derongan.minecraft.ui;

import de.erethon.headlib.HeadLib;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

public class ScrollingPallet implements Element {
    private final int width;
    private final Layout innerLayout;
    private List<Element> tools = new ArrayList<>();

    private int origin = 0;

    public ScrollingPallet(int width) {
        checkArgument(width >= 3, "Width must be over 2 in order to include controls and tools");
        this.width = width;
        this.innerLayout = new Layout();

        innerLayout.addElement(0, 0, Cell.forItemStack(HeadLib.WOODEN_ARROW_LEFT.toItemStack("Left")));
        innerLayout.addElement(width - 1, 0, Cell.forItemStack(HeadLib.WOODEN_ARROW_RIGHT.toItemStack("Right")));
    }

    @Override
    public Size getSize() {
        return Size.create(width, 1);
    }

    @Override
    public void draw(GuiRenderer guiRenderer) {
        innerLayout.draw(guiRenderer);

        for (int i = 0; i < width - 2; i++) {
            int finalI = i;

            getToolAt(i).ifPresent(element -> element.draw((x, y, itemStack) -> guiRenderer.renderAt(1 + finalI, 0, itemStack)));
        }
    }

    public void addTool(Element cell) {
        tools.add(cell);
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        if (clickEvent.getX() == 0) {
            origin = Math.floorMod((origin + 1), Math.max(width - 2, tools.size()));
            return;
        }

        if (clickEvent.getX() == width - 1) {
            origin = Math.floorMod((origin - 1), Math.max(width - 2, tools.size()));
            return;
        }


        getToolAt(clickEvent.getX() - 1).ifPresent(clicked -> {
            clicked.onClick(ClickEvent.offsetClickEvent(clickEvent, clickEvent.getX(), clickEvent.getY()));
        });
    }

    private Optional<Element> getToolAt(int x) {
        int locationInTools = Math.floorMod(origin + x, Math.max(width - 2, tools.size()));

        if (locationInTools >= tools.size()) {
            return Optional.empty();
        } else {
            return Optional.of(tools.get(locationInTools));
        }
    }
}
