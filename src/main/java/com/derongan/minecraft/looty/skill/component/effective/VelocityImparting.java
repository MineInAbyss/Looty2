package com.derongan.minecraft.looty.skill.component.effective;

import com.derongan.minecraft.looty.skill.component.Component;

public class VelocityImparting implements Component {
    private double strength;
    private Reference from;
    private Reference to;
    private Target target;

    public enum Reference {
        INITIATOR,
        TARGET,
        NEAREST_ORIGIN,
        SKY,
        GROUND,
        AWAY
    }

    public enum Target {
        INITIATOR,
        TARGET
    }

    public VelocityImparting(double strength, Reference from, Reference to, Target target) {
        this.strength = strength;
        this.from = from;
        this.to = to;
        this.target = target;
    }

    public double getStrength() {
        return strength;
    }

    public Reference getFrom() {
        return from;
    }

    public Reference getTo() {
        return to;
    }

    public Target getTarget() {
        return target;
    }
}
