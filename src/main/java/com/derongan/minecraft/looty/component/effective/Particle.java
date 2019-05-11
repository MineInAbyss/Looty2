package com.derongan.minecraft.looty.component.effective;

import com.derongan.minecraft.looty.component.Component;

public class Particle implements Component {
    public enum ParticleStyle {
        INITIATOR,
        ORIGIN,
        TARGET,
        CIRCUMFRENCE
    }

    private org.bukkit.Particle particle;
    private ParticleStyle style;

    public org.bukkit.Particle getParticle() {
        return particle;
    }

    public ParticleStyle getStyle() {
        return style;
    }

    private Particle(org.bukkit.Particle particle, ParticleStyle style) {
        this.particle = particle;
        this.style = style;
    }

    public static Particle create(org.bukkit.Particle particle, ParticleStyle style) {
        return new Particle(particle, style);
    }

}
