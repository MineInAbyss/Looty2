package com.derongan.minecraft.ui;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class Layout implements Element {
    private Map<Pair<Integer>, Element> elements;

    public Layout() {
        elements = new HashMap<>();
    }

    public void addElement(int x, int y, Element e) {
        elements.put(Pair.create(x, y), e);
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        findEffectedElements(clickEvent.getX(), clickEvent.getY())
                .forEach(child -> child.getValue()
                        .onClick(ClickEvent.offsetClickEvent(clickEvent, child.getKey().getFirst(), child.getKey()
                                .getSecond())));
    }

    @NotNull
    private Stream<Map.Entry<Pair<Integer>, Element>> findEffectedElements(int x, int y) {
        return elements.entrySet()
                .stream()
                .filter(child -> {
                    Pair<Integer> coords = child.getKey();
                    Size size = child.getValue().getSize();

                    int xl = coords.getFirst();
                    int xr = coords.getFirst() + size.getWidth() - 1;
                    int yt = coords.getSecond();
                    int yb = coords.getSecond() + size.getHeight() - 1;

                    return x <= xr && x >= xl && y <= yb && y >= yt;
                });
    }

    @Override
    public void draw(GuiRenderer guiRenderer) {
        elements.forEach((coord, element) -> {
            element.draw((x, y, inv) -> guiRenderer.renderAt(x + coord.getFirst(), y + coord.getSecond(), inv));
        });
    }

    @Override
    public Size getSize() {
        int width = 0;
        int height = 0;
        Set<Map.Entry<Pair<Integer>, Element>> entries = elements.entrySet();
        for (Map.Entry<Pair<Integer>, Element> entry : entries) {
            Pair<Integer> loc = entry.getKey();
            Size size = entry.getValue().getSize();

            width = Math.max(width, loc.getFirst() + size.getWidth());
            height = Math.max(height, loc.getSecond() + size.getHeight());
        }

        return Size.create(width, height);
    }


    @Override
    public String toString() {
        return String.format("Layout(%dx%d)", getSize().getHeight(), getSize().getWidth());
    }
}
