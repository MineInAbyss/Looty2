package com.derongan.minecraft.looty.skill.systems.particle;

import org.bukkit.Location;
import org.bukkit.Particle;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Random;

@Singleton
public class ParticleManager {
    public int maxParticles = 2000;
    private final ArrayList<ParticleHolder> holder;
    Random random = new Random();

    @Inject
    public ParticleManager() {
        holder = new ArrayList<>(maxParticles);
    }

    public void addParticle(Particle particle, Location location) {
        if (holder.size() == maxParticles) {
            holder.remove(random.nextInt(maxParticles));
        } else {
            holder.add(new ParticleHolder(particle, location));
        }
    }

    public void update(int tick) {
        holder.forEach(particleHolder -> particleHolder.location.getWorld()
                .spawnParticle(particleHolder.particle, particleHolder.location, 1, 0, 0, 0, .001, null, true));
        holder.clear();
    }

    public static class ParticleHolder {
        public ParticleHolder(Particle particle, Location location) {
            this.particle = particle;
            this.location = location;
        }

        Particle particle;
        Location location;
    }
}
