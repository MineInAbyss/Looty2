package com.derongan.minecraft.looty.skill.systems.particle;

import com.derongan.minecraft.looty.skill.component.components.Targets;
import org.bukkit.Location;

import java.util.List;

public interface ParticleProvider<T> {
    List<Location> provide(Targets targets, T particleSpec);
}
