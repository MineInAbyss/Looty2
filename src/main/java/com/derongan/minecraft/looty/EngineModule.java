package com.derongan.minecraft.looty;


import com.badlogic.ashley.core.Engine;
import com.derongan.minecraft.looty.systems.*;
import com.derongan.minecraft.looty.systems.targeting.TargetingSystem;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
class EngineModule {

    // TODO there must be a better way of injecting these
    @Provides
    @Singleton
    Engine provideEngine(TargetingSystem targetingSystem, DamageSystem damageSystem, IgniteSystem igniteSystem, ParticleSystem particleSystem, SoundSystem soundSystem, CleanupSystem cleanupSystem) {
        Engine engine = new Engine();

        engine.addSystem(targetingSystem);
        engine.addSystem(damageSystem);
        engine.addSystem(igniteSystem);
        engine.addSystem(particleSystem);
        engine.addSystem(soundSystem);
        engine.addSystem(cleanupSystem);

        return engine;
    }
}
