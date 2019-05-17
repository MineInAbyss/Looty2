package com.derongan.minecraft.looty.component.effective;

import com.derongan.minecraft.looty.component.Component;
import org.bukkit.Material;

public class Block implements Component {
    private Material material;
    private int ticks;

    public Block(Material material, int ticks) {
        this.material = material;
        this.ticks = ticks;
    }

    public Material getMaterial() {
        return material;
    }

    public int getTicks() {
        return ticks;
    }

    public static Block create(Material material, int ticks) {
        return new Block(material, ticks);
    }
}
