package com.derongan.minecraft.looty.skill.systems.particle;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.derongan.minecraft.looty.skill.component.effective.Particle;
import com.derongan.minecraft.looty.skill.component.target.Radius;
import com.derongan.minecraft.looty.skill.systems.AbstractDelayAwareIteratingSystem;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ParticleSystem extends AbstractDelayAwareIteratingSystem {
    private ComponentMapper<Particle> particleComponentMapper = ComponentMapper.getFor(Particle.class);

    private final Logger logger;

    @Inject
    public ParticleSystem(Logger logger) {
        super(Family.all(Particle.class).get());
        this.logger = logger;
    }

    @Override
    protected void processFilteredEntity(Entity entity, float deltaTime) {
        Particle particle = particleComponentMapper.get(entity);

        List<Location> particleLocations = new ArrayList<>();

        switch (particle.getStyle()) {
            case ORIGIN:
                particleLocations.add(originComponentMapper.get(entity).dynamicLocation.getLocation());
                break;
            case TARGET:
                particleLocations.add(targetComponentMapper.get(entity).dynamicLocation.getLocation());
                break;
            case PATH:
                getPointsInLine(entity, particleLocations);
                break;
            case OUTLINE:
                if (radiusComponentMapper.has(entity) && hasPath(entity)) {
                    getPointsInOutline(entity, particleLocations);
                } else if (hasPath(entity)) {
                    getPointsInLine(entity, particleLocations);
                } else if (radiusComponentMapper.has(entity)) {
                    Location head = headComponentMapper.get(entity).location;

                    double radius = radiusComponentMapper.get(entity).radius;

                    for (double phi = 0; phi < Math.PI / 2.0; phi += Math.PI / 15.0) {
                        double y = radius * Math.cos(phi);
                        for (double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / 30) {
                            double x = radius * Math.cos(theta) * Math.sin(phi);
                            double z = radius * Math.sin(theta) * Math.sin(phi);

                            particleLocations.add(head.clone().add(x, y, z));
                            particleLocations.add(head.clone().subtract(x, y, z));
                        }
                    }
                }
                break;
            case RANDOM:
                Radius radius = radiusComponentMapper.get(entity);
                int count = particle.getParticle() == org.bukkit.Particle.LAVA || particle.getParticle() == org.bukkit.Particle.DRIP_LAVA ? 3 : 20;
                for (int i = 0; i < count; i++) {
                    Location head = headComponentMapper.get(entity).location;


                    Vector randomVec = Vector.getRandom().subtract(Vector.getRandom()).multiply(radius.radius);

                    particleLocations.add(head.clone().add(randomVec));
                }
                break;
        }

        particleLocations.forEach(loc -> spawnParticleAtLocation(particle.getParticle(), loc));
    }

    private void getPointsInOutline(Entity entity, List<Location> particleLocations) {
        Location head = headComponentMapper.get(entity).location;
        Location tail = tailComponentMapper.get(entity).location;
        double radius = radiusComponentMapper.get(entity).radius;

        Vector heading = tail.toVector().subtract(head.toVector()).normalize();

        Vector clockArm;

        if (heading.getX() == 0 && heading.getY() == 0) {
            if (heading.getZ() == 0) {
                logger.warning("Tried to spawn particle on beam of 0 length");
                return;
            } else {
                clockArm = heading.clone().crossProduct(new Vector(0, 1, 0)).normalize();
            }
        } else {
            clockArm = heading.clone().crossProduct(new Vector(1, 0, 0)).normalize();
        }

        clockArm.multiply(radius);

        double dist = head.distance(tail);

        int circ = (int) Math.ceil(2 * Math.PI * radius) * 5;

        for (int i = 0; i < dist * 2; i++) {
            Location loc = head.clone().add(heading.clone().multiply(.5 * i));

            for (int j = 0; j < circ; j++) {
                double angle = ((double) j / circ) * Math.PI * 2;

                Vector offset = clockArm.clone().rotateAroundAxis(heading, angle);

                Location armloc = loc.clone().add(offset);
                particleLocations.add(armloc);
            }
        }
    }

    private void getPointsInLine(Entity entity, List<Location> particleLocations) {
        Location head = headComponentMapper.get(entity).location;
        Location tail = tailComponentMapper.get(entity).location;

        Vector heading = tail.toVector().subtract(head.toVector()).normalize();

        double dist = head.distance(tail);

        for (int i = 0; i < dist * 2; i++) {
            Location loc = head.clone().add(heading.clone().multiply(.5 * i));

            particleLocations.add(loc);
        }
    }


    //        Particle particle = particleComponentMapper.get(entity);
//        Origins origins = (Origins) originsComponentMapper.get(entity);
//        TargetInfo targetInfo = (TargetInfo) targetInfoComponentMapper.get(entity);
//        Targets targets = targetsComponentMapper.get(entity);
//
//        List<Location> spawnLocations = new ArrayList<>();
//
//        boolean doubleIt = false;
//
//        switch (particle.getStyle()) {
//            case ORIGIN:
//                origins.getTargetOrigins()
//                        .forEach(origin -> spawnLocations.add(origin.getLocation()));
//                break;
//            case INITIATOR:
//                targetInfo.getInitiator()
//                        .ifPresent(initiator -> spawnLocations.add(initiator.getLocation()));
//                break;
//            case TARGET:
//                targets.getTargets()
//                        .forEach(target -> spawnLocations.add(target.getLocation()));
//                break;
//            case OUTLINE:
//                if (radiusComponentMapper.has(entity)) {
//                    double radius = radiusComponentMapper.get(entity).getRadius();
//
//                    double tau = 2.0 * Math.PI;
//
//                    origins.getTargetOrigins().forEach(origin -> {
//                        Vector center = origin.getLocation().toVector();
//                        Vector clockArm = new Vector(radius, 0, 0);
//
//                        World world = origin.getLocation().getWorld();
//
//                        int numParticles = (int) Math.pow(10, Math.log(radius + 1));
//
//                        for (int i = 0; i < numParticles; i++) {
//                            double angle = (((double) i) / numParticles) * tau;
//                            Vector vector = clockArm.clone().rotateAroundY(angle);
//                            spawnLocations.add(center.clone().add(vector).toLocation(world));
//                        }
//                    });
//                }
//                break;
//            case DOUBLE_SPIRAL:
//                doubleIt = true;
//            case SPIRAL:
//                double radius = radiusComponentMapper.get(entity).getRadius();
//                int length = beamComponentMapper.has(entity) ? beamComponentMapper.get(entity).getLength() : 4;
//                Vector direction = new Vector(0, 1, 0);
//
//                org.bukkit.entity.Entity initiator = ((TargetInfo) targetInfoComponentMapper.get(entity)).getInitiator()
//                        .get();
//
//                Location initiatorLocation = initiator instanceof Player ? ((Player) initiator).getEyeLocation() : initiator
//                        .getLocation()
//                        .clone()
//                        .add(0, initiator.getHeight() * .9, 0);
//
//                for (Origins.Origin targetOrigin : origins.getTargetOrigins()) {
//                    if (beamComponentMapper.has(entity)) {
//                        direction = targetOrigin.getLocation()
//                                .toVector()
//                                .clone()
//                                .subtract(initiatorLocation.toVector());
//                    }
//
//                    addParticlesInBeam(spawnLocations, initiatorLocation, direction, radius, length, true, doubleIt);
//                }
//        }
//
//        spawnLocations.forEach(sl -> spawnParticleAtLocation(particle.getParticle(), sl));
//
//    }
//
    private void spawnParticleAtLocation(org.bukkit.Particle particle, Location location) {
        World world = location.getWorld();
        world.spawnParticle(particle, location, 1, 0, 0, 0, .001, null, true);
    }
//
//    // TODO improve logic for outline vs spiral
//    private void addParticlesInBeam(List<Location> locations, Location initiatorLocation, Vector axis, double radius, int length, boolean spiral, boolean doubleIt) {
//        axis = axis.clone().normalize();
//        int numParticles = (int) Math.pow(10, Math.log(radius + 1));
//        if (spiral) {
//            numParticles = 1;
//        }
//
//        Vector clockArm;
//
//        if (axis.getX() == 0 && axis.getY() == 0) {
//            if (axis.getZ() == 0) {
//                logger.warning("Tried to spawn particle on beam of 0 length");
//                return;
//            } else {
//                clockArm = axis.clone().crossProduct(new Vector(0, 1, 0)).normalize();
//            }
//        } else {
//            clockArm = axis.clone().crossProduct(new Vector(1, 0, 0)).normalize();
//        }
//
//        clockArm.multiply(radius);
//
//        //TODO include end
//        for (double i = 0; i < length; i += .1) {
//            Vector centralPoint = axis.clone().multiply(i).add(initiatorLocation.toVector());
//
//            for (int j = 0; j < numParticles; j++) {
//                double angle = ((double) j / numParticles) * Math.PI * 2;
//
//                if (spiral) {
//                    angle = i * Math.PI;
//                }
//
//                Vector vector = clockArm.clone().rotateAroundAxis(axis, angle);
//                locations.add(centralPoint.clone().add(vector).toLocation(initiatorLocation.getWorld()));
//
//                if(doubleIt){
//                    angle = i * Math.PI + Math.PI;
//                    vector = clockArm.clone().rotateAroundAxis(axis, angle);
//                    locations.add(centralPoint.clone().add(vector).toLocation(initiatorLocation.getWorld()));
//                }
//            }
//        }
//    }
}
