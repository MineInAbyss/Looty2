package com.derongan.minecraft.looty.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.derongan.minecraft.looty.component.effective.Particle;
import org.bukkit.Server;

import javax.inject.Inject;

public class ParticleSystem extends IteratingSystem {

    @Inject
    public ParticleSystem() {
        super(Family.all(Particle.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }
}
