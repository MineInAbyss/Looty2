package com.derongan.minecraft.looty.component.target;

import com.derongan.minecraft.looty.component.Component;

public class Radius implements Component {
    private final double radius;

    private Radius(double radius) {
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public static Radius create(double radius) {
        return new Radius(radius);
    }
}
