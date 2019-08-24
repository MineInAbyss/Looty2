package com.derongan.minecraft.looty.skill.systems.particle;

import com.derongan.minecraft.looty.location.DynamicLocation;
import com.derongan.minecraft.looty.skill.component.components.Targets;
import com.derongan.minecraft.looty.skill.component.proto.ParticleInfo;
import com.google.common.collect.ImmutableList;
import org.bukkit.Location;

import java.util.List;

public class PointParticleProvider implements ParticleProvider<ParticleInfo.ParticleData.PointSpec> {
    @Override
    public List<Location> provide(Targets targets, ParticleInfo.ParticleData.PointSpec particleSpec) {
        return targets.getTarget(particleSpec.getNode())
                .map(DynamicLocation::getLocation)
                .map(ImmutableList::of)
                .orElse(ImmutableList.of());
    }
}
