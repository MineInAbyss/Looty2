package com.derongan.minecraft.looty.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.derongan.minecraft.looty.component.effective.Particle;
import com.derongan.minecraft.looty.component.internal.Origins;
import com.derongan.minecraft.looty.component.internal.TargetInfo;
import com.derongan.minecraft.looty.component.internal.Targets;
import com.derongan.minecraft.looty.component.target.Beam;
import com.derongan.minecraft.looty.component.target.Radius;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
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
    private ComponentMapper<Beam> beamComponentMapper = ComponentMapper.getFor(Beam.class);

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

        List<Location> spawnLocations = new ArrayList<>();

        switch (particle.getStyle()) {
            case ORIGIN:
                origins.getTargetOrigins()
                        .forEach(origin -> spawnLocations.add(origin.getLocation()));
                break;
            case INITIATOR:
                targetInfo.getInitiator()
                        .ifPresent(initiator -> spawnLocations.add(initiator.getLocation()));
                break;
            case TARGET:
                targets.getTargets()
                        .forEach(target -> spawnLocations.add(target.getLocation()));
                break;
            case OUTLINE:
                if (radiusComponentMapper.has(entity)) {
                    double radius = radiusComponentMapper.get(entity).getRadius();

                    double tau = 2.0 * Math.PI;

                    origins.getTargetOrigins().forEach(origin -> {
                        Vector center = origin.getLocation().toVector();
                        Vector clockArm = new Vector(radius, 0, 0);

                        World world = origin.getLocation().getWorld();

                        int numParticles = (int) Math.pow(10, Math.log(radius + 1));

                        for (int i = 0; i < numParticles; i++) {
                            double angle = (((double) i) / numParticles) * tau;
                            Vector vector = clockArm.clone().rotateAroundY(angle);
                            spawnLocations.add(center.clone().add(vector).toLocation(world));
                        }
                    });
                }
                break;
            case SPIRAL:
                double radius = radiusComponentMapper.get(entity).getRadius();
                int length = beamComponentMapper.has(entity) ? beamComponentMapper.get(entity).getLength() : 4;
                Vector direction = new Vector(0, 1, 0);

                org.bukkit.entity.Entity initiator = ((TargetInfo) targetInfoComponentMapper.get(entity)).getInitiator()
                        .get();

                Location initiatorLocation = initiator instanceof Player ? ((Player) initiator).getEyeLocation() : initiator
                        .getLocation()
                        .clone()
                        .add(0, initiator.getHeight() * .9, 0);

                for (Origins.Origin targetOrigin : origins.getTargetOrigins()) {
                    if (beamComponentMapper.has(entity)) {
                        direction = targetOrigin.getLocation()
                                .toVector()
                                .clone()
                                .subtract(initiatorLocation.toVector());
                    }

                    addParticlesInBeam(spawnLocations, initiatorLocation, direction, radius, length, true);
                }
        }

        spawnLocations.forEach(sl -> spawnParticleAtLocation(particle.getParticle(), sl));

    }

    private void spawnParticleAtLocation(org.bukkit.Particle particle, Location location) {
        World world = location.getWorld();
        world.spawnParticle(particle, location, 1, 0, 0, 0, .001);
    }

    private void addParticlesInBeam(List<Location> locations, Location initiatorLocation, Vector axis, double radius, int length, boolean spiral) {
        int numParticles = (int) Math.pow(10, Math.log(radius + 1));
        if (spiral) {
            numParticles = 1;
        }

        Vector clockArm;

        if (axis.getX() == 0 && axis.getY() == 0) {
            if (axis.getZ() == 0) {
                logger.warning("Tried to spawn particle on beam of 0 length");
                return;
            } else {
                clockArm = axis.clone().crossProduct(new Vector(0, 1, 0)).normalize();
            }
        } else {
            clockArm = axis.clone().crossProduct(new Vector(1, 0, 0)).normalize();
        }

        clockArm.multiply(radius);

        for (double i = 0; i < length + 1; i += .05) {
            Vector centralPoint = axis.clone().multiply(i).add(initiatorLocation.toVector());

            for (int j = 0; j < numParticles; j++) {
                double angle = ((double) j / numParticles) * Math.PI * 2;

                if (spiral) {
                    angle = i * Math.PI * 2;
                }

                Vector vector = clockArm.clone().rotateAroundAxis(axis, angle);
                locations.add(centralPoint.clone().add(vector).toLocation(initiatorLocation.getWorld()));
            }
        }
    }
}
