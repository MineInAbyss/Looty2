package com.derongan.minecraft.looty.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.derongan.minecraft.looty.component.effective.Particle;
import com.derongan.minecraft.looty.component.internal.Origins;
import org.bukkit.Location;
import org.bukkit.World;

import javax.inject.Inject;

public class ParticleSystem extends IteratingSystem {

    private ComponentMapper<Particle> particleComponentMapper = ComponentMapper.getFor(Particle.class);
    private ComponentMapper originsComponentMapper = ComponentMapper.getFor(Origins.ORIGINS_CLASS);

    @Inject
    public ParticleSystem() {
        super(Family.all(Particle.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Particle particle = particleComponentMapper.get(entity);
        Origins origins = (Origins) originsComponentMapper.get(entity);

        switch (particle.getStyle()) {
            case ORIGIN:
                origins.getTargetOrigins()
                        .forEach(origin -> spawnParticleAtLocation(particle.getParticle(), origin.getLocation()));
                break;
        }

    }

    private void spawnParticleAtLocation(org.bukkit.Particle particle, Location location) {
        World world = location.getWorld();
        world.spawnParticle(particle, location, 1);
    }
}
