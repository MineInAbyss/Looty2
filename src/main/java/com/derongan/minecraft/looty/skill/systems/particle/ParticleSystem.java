package com.derongan.minecraft.looty.skill.systems.particle;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.derongan.minecraft.looty.skill.component.Particle;
import com.derongan.minecraft.looty.skill.component.components.Targets;
import com.derongan.minecraft.looty.skill.systems.AbstractDelayAwareIteratingSystem;

import javax.inject.Inject;
import java.util.logging.Logger;

public class ParticleSystem extends AbstractDelayAwareIteratingSystem {
    private ComponentMapper<Particle> particleComponentMapper = ComponentMapper.getFor(Particle.class);

    private final ParticleManager particleManager;

    @Inject
    public ParticleSystem(Logger logger, ParticleManager particleManager) {
        super(logger, Family.all(Particle.class).get());
        this.particleManager = particleManager;
    }

    @Override
    protected void processFilteredEntity(Entity entity, float deltaTime) {
        Particle particles = particleComponentMapper.get(entity);
        Targets targets = targetComponentMapper.get(entity);


        particles.getInfo().getParticlesList().forEach(particleData -> {
            org.bukkit.Particle mcParticle = org.bukkit.Particle.valueOf(particleData.getParticleName());
            switch (particleData.getParticleSpecCase()) {
                case SPHERE_SPEC:
                    new SphereParticleProvider().provide(targets, particleData.getSphereSpec())
                            .forEach(location -> particleManager.addParticle(mcParticle, location));
                    break;
                case PATH_SPEC:
                    new PathParticleProvider().provide(targets, particleData.getPathSpec())
                            .forEach(location -> particleManager.addParticle(mcParticle, location));
                    break;
                case POINT_SPEC:
                    new PointParticleProvider().provide(targets, particleData.getPointSpec())
                            .forEach(location -> particleManager.addParticle(mcParticle, location));
                    break;
                case CYLINDER_SPEC:
                    new CylinderParticleProvider().provide(targets, particleData.getCylinderSpec())
                            .forEach(location -> particleManager.addParticle(mcParticle, location));
            }
        });
    }

//        switch (particle.getInfo().getFillStyle()) {
//            case ORIGIN:
//                particleLocations.add(originComponentMapper.get(entity).dynamicLocation.getLocation());
//                break;
//            case TARGET:
//                particleLocations.add(targetComponentMapper.get(entity).dynamicLocation.getLocation());
//                break;
//            case PATH:
//                getPointsInLine(entity, particleLocations);
//                break;
//            case ENTITIES:
//                entityTargetsComponentMapper.get(entity).affectedEntities.forEach(e -> particleLocations.add(e.getLocation()));
//                break;
//            case OUTLINE:
//                if (volumeComponentMapper.has(entity) && hasPath(entity)) {
//                    getPointsInOutline(entity, particleLocations);
//                } else if (hasPath(entity)) {
//                    getPointsInLine(entity, particleLocations);
//                } else if (volumeComponentMapper.has(entity)) {
//                    Location head = headComponentMapper.get(entity).location;
//
//                    double radius = volumeComponentMapper.get(entity).getInfo().getRadius();
//
//                    for (double phi = 0; phi < Math.PI / 2.0; phi += Math.PI / 15.0) {
//                        double y = radius * Math.cos(phi);
//                        for (double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / 30) {
//                            double x = radius * Math.cos(theta) * Math.sin(phi);
//                            double z = radius * Math.sin(theta) * Math.sin(phi);
//
//                            particleLocations.add(head.clone().add(x, y, z));
//                            particleLocations.add(head.clone().subtract(x, y, z));
//                        }
//                    }
//                }
//                break;
//            case RANDOM:
//                Radius radius = volumeComponentMapper.get(entity);
//                int count = 2;
//                for (int i = 0; i < count; i++) {
//                    Location head = headComponentMapper.get(entity).location;
//
//
//                    Vector randomVec = Vector.getRandom()
//                            .subtract(Vector.getRandom())
//                            .multiply(radius.getInfo().getRadius());
//
//                    particleLocations.add(head.clone().add(randomVec));
//                }
//                break;
//        }
//
//        particleLocations.forEach(loc -> spawnParticleAtLocation(mcParticle, loc));
//    }
//
//    private void getPointsInOutline(Entity entity, List<Location> particleLocations) {
//        Location head = headComponentMapper.get(entity).location;
//        Location tail = tailComponentMapper.get(entity).location;
//        double radius = volumeComponentMapper.get(entity).getInfo().getRadius();
//
//        Vector heading = tail.toVector().subtract(head.toVector()).normalize();
//
//        Vector clockArm;
//
//        if (heading.getX() == 0 && heading.getY() == 0) {
//            if (heading.getZ() == 0) {
//                logger.warning("Tried to spawn particle on beam of 0 length");
//                return;
//            } else {
//                clockArm = heading.clone().crossProduct(new Vector(0, 1, 0)).normalize();
//            }
//        } else {
//            clockArm = heading.clone().crossProduct(new Vector(1, 0, 0)).normalize();
//        }
//
//        clockArm.multiply(radius);
//
//        double dist = head.distance(tail);
//
//        int circ = (int) Math.ceil(2 * Math.PI * radius) * 5;
//
//        for (int i = 0; i < dist * 2; i++) {
//            Location loc = head.clone().add(heading.clone().multiply(.5 * i));
//
//            for (int j = 0; j < circ; j++) {
//                double angle = ((double) j / circ) * Math.PI * 2;
//
//                Vector offset = clockArm.clone().rotateAroundAxis(heading, angle);
//
//                Location armloc = loc.clone().add(offset);
//                particleLocations.add(armloc);
//            }
//        }
//    }
//
//    private void getPointsInLine(Entity entity, List<Location> particleLocations) {
//        Location head = headComponentMapper.get(entity).location;
//        Location tail = tailComponentMapper.get(entity).location;
//
//        Vector heading = tail.toVector().subtract(head.toVector()).normalize();
//
//        double dist = head.distance(tail);
//
//        for (int i = 0; i < dist * 2; i++) {
//            Location loc = head.clone().add(heading.clone().multiply(.5 * i));
//
//            particleLocations.add(loc);
//        }
//    }
//
//    private void spawnParticleAtLocation(org.bukkit.Particle particle, Location location) {
//        particleManager.addParticle(particle, location);
//    }
}
