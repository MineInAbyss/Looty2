package com.derongan.minecraft.looty.skill.systems.particle;

import com.derongan.minecraft.looty.skill.component.components.Targets;
import com.derongan.minecraft.looty.skill.component.proto.ParticleInfo;
import com.google.common.collect.ImmutableList;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class PathParticleProvider implements ParticleProvider<ParticleInfo.ParticleData.PathSpec> {
    @Override
    public List<Location> provide(Targets targets, ParticleInfo.ParticleData.PathSpec particleSpec) {
        ImmutableList.Builder<Location> locations = ImmutableList.builder();

        List<String> nodeKeys = particleSpec.getNodesList();

        if (nodeKeys.size() < 2) {
            return ImmutableList.of();
        } else {
            String prev = nodeKeys.get(0);

            for (int i = 1; i < nodeKeys.size(); i++) {
                String current = nodeKeys.get(i);

                Location start = targets.getTarget(current).get().getLocation();
                Location end = targets.getTarget(prev).get().getLocation();


                locations.addAll(getBetweenLocations(start, end));

                prev = current;
            }
        }

        return locations.build();
    }


    private List<Location> getBetweenLocations(Location start, Location end) {
        Vector heading = end.toVector().subtract(start.toVector()).normalize();


        List<Location> list = new ArrayList<>();
        double dist = start.distance(end);

        for (int i = 0; i < dist * 2; i++) {
            Location loc = start.clone().add(heading.clone().multiply(.5 * i));
            list.add(loc);
        }

        return list;
    }
}
