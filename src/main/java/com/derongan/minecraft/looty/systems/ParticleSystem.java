package com.derongan.minecraft.looty.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.derongan.minecraft.looty.component.effective.Particle;
import com.derongan.minecraft.looty.component.internal.Origins;
import com.derongan.minecraft.looty.component.internal.TargetInfo;
import com.derongan.minecraft.looty.component.internal.Targets;
import com.derongan.minecraft.looty.component.target.Radius;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ParticleSystem extends IteratingSystem {

    private ComponentMapper<Particle> particleComponentMapper = ComponentMapper.getFor(Particle.class);
    private ComponentMapper originsComponentMapper = ComponentMapper.getFor(Origins.ORIGINS_CLASS);
    private ComponentMapper targetInfoComponentMapper = ComponentMapper.getFor(TargetInfo.TARGET_INFO_CLASS);
    private ComponentMapper<Targets> targetsComponentMapper = ComponentMapper.getFor(Targets.class);
    private ComponentMapper<Radius> radiusComponentMapper = ComponentMapper.getFor(Radius.class);

    private final Logger logger;

    @Inject
    public ParticleSystem(Logger logger) {
        super(Family.all(Particle.class).get());
        this.logger = logger;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Particle particle = particleComponentMapper.get(entity);
        Origins origins = (Origins) originsComponentMapper.get(entity);
        TargetInfo targetInfo = (TargetInfo) targetInfoComponentMapper.get(entity);
        Targets targets = targetsComponentMapper.get(entity);

        logger.info("Spawning particle");

        List<Location> spawnLocations = new ArrayList<>();

        switch (particle.getStyle()) {
            case ORIGIN:
                origins.getTargetOrigins()
                        .forEach(origin -> spawnLocations.add(origin.getLocation()));
                break;
            case INITIATOR:
                targetInfo.getInitiator()
                        .ifPresent(initiator -> spawnLocations.add(initiator.getLocation()));
            case TARGET:
                targets.getTargets()
                        .forEach(target -> spawnLocations.add(target.getLocation()));
            case CIRCUMFRENCE:
                if (radiusComponentMapper.has(entity)) {
                    int radius = radiusComponentMapper.get(entity).getRadius();

                    double tau = 2.0 * Math.PI;

                    origins.getTargetOrigins().forEach(origin -> {
                        Vector center = origin.getLocation().toVector();
                        Vector clockArm = new Vector(radius, 0, 0);

                        World world = origin.getLocation().getWorld();

                        int numParticles = (int) Math.pow(10, Math.log(radius));

                        for (int i = 0; i < numParticles; i++) {
                            double angle = (((double) i) / numParticles) * tau;
                            Vector vector = clockArm.clone().rotateAroundY(angle);
                            logger.info("Spawning particle at angle %d" + angle);
                            spawnLocations.add(center.clone().add(vector).toLocation(world));
                        }
                    });
                }
        }

        spawnLocations.forEach(sl -> spawnParticleAtLocation(particle.getParticle(), sl));

    }

    private void spawnParticleAtLocation(org.bukkit.Particle particle, Location location) {
        World world = location.getWorld();
        world.spawnParticle(particle, location, 1);
    }
}
