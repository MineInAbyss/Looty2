package com.derongan.minecraft.looty.component.target;

import com.derongan.minecraft.looty.component.Component;

public class Radius implements Component {
    private final int radius;

    private Radius(int radius) {
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }

    public static Radius create(int radius) {
        return new Radius(radius);
    }
}
