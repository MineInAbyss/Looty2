package com.derongan.minecraft.looty.skill.component.effective;

import com.derongan.minecraft.looty.skill.component.Component;

public class Damage implements Component {
    private final double damage;

    private Damage(double damage) {
        this.damage = damage;
    }

    public double getDamage() {
        return damage;
    }

    public static Damage create(int damage) {
        return new Damage(damage);
    }
}
