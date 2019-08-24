package com.derongan.minecraft.looty.skill.systems.particle;

import com.derongan.minecraft.looty.skill.component.components.Targets;
import com.derongan.minecraft.looty.skill.component.proto.ParticleInfo;
import com.google.common.collect.ImmutableList;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class CylinderParticleProvider implements ParticleProvider<ParticleInfo.ParticleData.CylinderSpec> {
    @Override
    public List<Location> provide(Targets targets, ParticleInfo.ParticleData.CylinderSpec particleSpec) {
        Location head = targets.getTarget(particleSpec.getTo()).get().getLocation();
        Location tail = targets.getTarget(particleSpec.getFrom()).get().getLocation();
        double radius = particleSpec.getRadius();

        Vector heading = tail.toVector().subtract(head.toVector()).normalize();

        Vector clockArm;

        if (heading.getX() == 0 && heading.getY() == 0) {
            if (heading.getZ() == 0) {
                return ImmutableList.of();
            } else {
                clockArm = heading.clone().crossProduct(new Vector(0, 1, 0)).normalize();
            }
        } else {
            clockArm = heading.clone().crossProduct(new Vector(1, 0, 0)).normalize();
        }

        clockArm.multiply(radius);

        double dist = head.distance(tail);

        int circ = (int) Math.ceil(2 * Math.PI * radius) * 5;

        List<Location> particleLocations = new ArrayList<>();

        for (int i = 0; i < dist * 2; i++) {
            Location loc = head.clone().add(heading.clone().multiply(.5 * i));

            for (int j = 0; j < circ; j++) {
                double angle = ((double) j / circ) * Math.PI * 2;

                Vector offset = clockArm.clone().rotateAroundAxis(heading, angle);

                Location armloc = loc.clone().add(offset);
                particleLocations.add(armloc);
            }
        }

        return particleLocations;
    }
}
