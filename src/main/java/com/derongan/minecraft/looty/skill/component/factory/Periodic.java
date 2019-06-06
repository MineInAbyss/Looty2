package com.derongan.minecraft.looty.skill.component.factory;

import com.derongan.minecraft.looty.skill.component.Component;

public class Periodic implements Component {
    private final int period;
    private final int iterations;
    private final boolean sticky;

    private boolean targetsCalculated;

    private Periodic(int period, int iterations, boolean sticky) {
        this.period = period;
        this.iterations = iterations;
        this.sticky = sticky;
    }

    public boolean isSticky() {
        return sticky;
    }

    public boolean areTargetsCalculated() {
        return targetsCalculated;
    }

    public void setTargetsCalculated(boolean targetsCalculated) {
        this.targetsCalculated = targetsCalculated;
    }

    public int getPeriod() {
        return period;
    }

    public int getIterations() {
        return iterations;
    }

    public Periodic create(int period, int iterations) {
        return new Periodic(period, iterations, false);
    }

    public Periodic create(int period, int iterations, boolean sticky) {
        return new Periodic(period, iterations, sticky);
    }
}
