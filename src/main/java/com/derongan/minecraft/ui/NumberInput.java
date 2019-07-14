package com.derongan.minecraft.ui;

import de.erethon.headlib.HeadLib;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class NumberInput implements Element {
    private ArrayList<String> inputs;

    public NumberInput() {
        inputs = new ArrayList<>(9);
    }

    @Override
    public Size getSize() {
        return Size.create(3, 6);
    }

    @Override
    public void draw(GuiRenderer guiRenderer) {
        for (int y = 1; y < 6; y++) {
            guiRenderer.renderAt(0, y, new ItemStack(Material.IRON_BARS));
            guiRenderer.renderAt(1, y, new ItemStack(Material.IRON_BARS));
            guiRenderer.renderAt(2, y, new ItemStack(Material.IRON_BARS));
            guiRenderer.renderAt(6, y, new ItemStack(Material.IRON_BARS));
            guiRenderer.renderAt(7, y, new ItemStack(Material.IRON_BARS));
            guiRenderer.renderAt(8, y, new ItemStack(Material.IRON_BARS));
        }

        for (int x = 0; x < 9; x++) {
            guiRenderer.renderAt(x, 1, new ItemStack(Material.IRON_BARS));
        }

        ItemStack decimal = HeadLib.WOODEN_DOT.toItemStack("Point");

        for (int i = 0; i < 9; i++) {
            guiRenderer.renderAt(3 + (i % 3), 2 + (i / 3), HeadLib.valueOf(String.format("WOODEN_%d", i + 1))
                    .toItemStack(String.valueOf(i + 1)));
        }

        guiRenderer.renderAt(3, 5, decimal);
        guiRenderer.renderAt(4, 5, HeadLib.WOODEN_0.toItemStack("0"));
        guiRenderer.renderAt(5, 5, HeadLib.WOODEN_ARROW_LEFT.toItemStack("Delete"));

        for (int i = 0; i < inputs.size(); i++) {
            guiRenderer.renderAt((9 - inputs.size()) + i, 0, HeadLib.valueOf(String.format("WOODEN_%s", inputs.get(i)))
                    .toItemStack(inputs.get(i)));
        }
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        if (clickEvent.getX() > 2 && clickEvent.getX() < 6) {
            if (clickEvent.getY() < 5 && clickEvent.getY() > 1) {
                int x = clickEvent.getX() - 3;
                int y = clickEvent.getY() - 2;

                int num = y * 3 + x + 1;

                inputs.add(String.valueOf(num));
            }
        }

        if (clickEvent.getX() == 5 && clickEvent.getY() == 5) {
            inputs.remove(inputs.size() - 1);
        }

        if (clickEvent.getX() == 3 && clickEvent.getY() == 5){
            inputs.add("DOT");
        }

        if (clickEvent.getX() == 4 && clickEvent.getY() == 5){
            inputs.add("0");
        }
    }
}
