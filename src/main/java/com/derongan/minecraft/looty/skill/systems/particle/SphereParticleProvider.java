package com.derongan.minecraft.looty.skill.systems.particle;

import com.derongan.minecraft.looty.location.MovingDynamicLocation;
import com.derongan.minecraft.looty.skill.component.components.Targets;
import com.derongan.minecraft.looty.skill.component.proto.ParticleInfo;
import com.google.common.collect.ImmutableList;
import org.bukkit.Location;

import java.util.List;
import java.util.Optional;

public class SphereParticleProvider implements ParticleProvider<ParticleInfo.ParticleData.SphereSpec> {
    @Override
    public List<Location> provide(Targets targets, ParticleInfo.ParticleData.SphereSpec particleSpec) {
        ImmutableList.Builder<Location> locations = ImmutableList.builder();

        String nodeKey = particleSpec.getNode();

        Optional<MovingDynamicLocation> dynamicLocation = targets.getTarget(nodeKey);

        dynamicLocation.ifPresent(location -> {
            double radius = particleSpec.getRadius();

            for (double phi = 0; phi < Math.PI / 2.0; phi += Math.PI / 15.0) {
                double y = radius * Math.cos(phi);
                for (double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / 30) {
                    double x = radius * Math.cos(theta) * Math.sin(phi);
                    double z = radius * Math.sin(theta) * Math.sin(phi);

                    locations.add(location.getLocation().clone().add(x, y, z));
                    locations.add(location.getLocation().clone().subtract(x, y, z));
                }
            }
        });


        return locations.build();
    }
}
