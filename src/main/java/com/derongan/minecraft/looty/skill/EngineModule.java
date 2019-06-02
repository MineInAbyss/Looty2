package com.derongan.minecraft.looty.skill;


import com.badlogic.ashley.core.Engine;
import com.derongan.minecraft.looty.skill.systems.*;
import com.derongan.minecraft.looty.skill.systems.block.BlockCreationSystem;
import com.derongan.minecraft.looty.skill.systems.particle.ParticleSystem;
import com.derongan.minecraft.looty.skill.systems.targeting.TargetingSystem;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public
class EngineModule {

    // TODO there must be a better way of injecting these
    @Provides
    @Singleton
    Engine provideEngine(TargetingSystem targetingSystem,
                         VelocityImpartingSystem velocityImpartingSystem,
                         DamageSystem damageSystem,
                         IgniteSystem igniteSystem,
                         BlockCreationSystem blockCreationSystem,
                         LightningSystem lightningSystem,
                         ParticleSystem particleSystem,
                         SoundSystem soundSystem,
                         CleanupSystem cleanupSystem) {
        Engine engine = new Engine();

        engine.addSystem(targetingSystem);
        engine.addSystem(velocityImpartingSystem);
        engine.addSystem(damageSystem);
        engine.addSystem(igniteSystem);
        engine.addSystem(blockCreationSystem);
        engine.addSystem(lightningSystem);
        engine.addSystem(particleSystem);
        engine.addSystem(soundSystem);
        engine.addSystem(cleanupSystem);

        return engine;
    }
}
