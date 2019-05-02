package com.derongan.minecraft.looty;


import com.badlogic.ashley.core.Engine;
import com.derongan.minecraft.looty.systems.CleanupSystem;
import com.derongan.minecraft.looty.systems.DamageSystem;
import com.derongan.minecraft.looty.systems.IgniteSystem;
import com.derongan.minecraft.looty.systems.targeting.TargetingSystem;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
class EngineModule {

    // TODO there must be a better way of injecting these
    @Provides
    @Singleton
    Engine provideEngine(TargetingSystem targetingSystem, DamageSystem damageSystem, IgniteSystem igniteSystem, CleanupSystem cleanupSystem) {
        Engine engine = new Engine();

        engine.addSystem(targetingSystem);
        engine.addSystem(damageSystem);
        engine.addSystem(igniteSystem);
        engine.addSystem(cleanupSystem);

        return engine;
    }
}
