package com.derongan.minecraft.looty.component.target;

import com.derongan.minecraft.looty.component.Component;

public class Beam implements Component {
    private final int length;

    private Beam(int length) {
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    public static Beam create(int length) {
        return new Beam(length);
    }
}