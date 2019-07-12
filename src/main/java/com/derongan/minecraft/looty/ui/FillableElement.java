package com.derongan.minecraft.looty.ui;

public class FillableElement implements Element {
    private final int height;
    private final int width;

    private Element[][] elements;

    // TODO make this work with fixed size
    public FillableElement(int height, int width) {
        this.height = height;
        this.width = width;

        elements = new Element[width][height];
    }

    @Override
    public Size getSize() {
        return Size.create(width, height);
    }

    @Override
    public void draw(GuiRenderer guiRenderer) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                final int xc = x;
                final int yc = y;

                if (elements[x][y] != null) {
                    elements[x][y].draw((x1, y1, mat) -> guiRenderer.renderAt(x1 + xc, y1 + yc, mat));
                }
            }
        }
    }

    public void addElement(Element element) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (elements[x][y] == null) {
                    elements[x][y] = element;
                    return;
                }
            }
        }
    }

    public void setElement(int x, int y, Element element) {
        elements[x][y] = element;
    }

    public void removeElement(int x, int y) {
        elements[x][y] = null;
    }

    public void clear() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                elements[x][y] = null;
            }
        }
    }


    // TODO decide these
    @Override
    public void onPickup(ClickEvent clickEvent) {
    }

    @Override
    public void onPlace(ClickEvent clickEvent) {

    }

    @Override
    public void onSwap(ClickEvent clickEvent) {

    }
}
