package com.derongan.minecraft.looty.skill.component.effective;

import com.derongan.minecraft.looty.skill.component.Component;

public class Ignite implements Component {
    private final int strength;

    private Ignite(int strength) {
        this.strength = strength;
    }

    public int getStrength() {
        return strength;
    }

    public static Ignite create(int strength) {
        return new Ignite(strength);
    }
}
