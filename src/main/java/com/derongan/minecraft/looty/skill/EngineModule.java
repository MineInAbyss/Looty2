package com.derongan.minecraft.looty.skill;


import com.badlogic.ashley.core.Engine;
import com.derongan.minecraft.looty.skill.systems.*;
import com.derongan.minecraft.looty.skill.systems.block.BlockCreationSystem;
import com.derongan.minecraft.looty.skill.systems.particle.ParticleSystem;
import com.derongan.minecraft.looty.skill.systems.projectile.ProjectileSystem;
import com.derongan.minecraft.looty.skill.systems.targeting.EntityTargetingSystem;
import com.derongan.minecraft.looty.skill.systems.targeting.MovementTargetingSystem;
import com.derongan.minecraft.looty.skill.systems.targeting.ReferenceLocationTargetingSystem;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public
class EngineModule {

    // TODO there must be a better way of injecting these
    @Provides
    @Singleton
    Engine provideEngine(ReferenceLocationTargetingSystem referenceLocationTargetingSystem,
                         MovementTargetingSystem movementTargetingSystem,
                         EntityTargetingSystem entityTargetingSystem,
                         VelocityImpartingSystem velocityImpartingSystem,
                         DamageSystem damageSystem,
                         IgniteSystem igniteSystem,
                         BlockCreationSystem blockCreationSystem,
                         ProjectileSystem projectileSystem,
                         LightningSystem lightningSystem,
                         ParticleSystem particleSystem,
                         SoundSystem soundSystem,
                         TimingSystem timingSystem,
                         DelaySystem delaySystem,
                         CleanupSystem cleanupSystem) {
        Engine engine = new Engine();

        engine.addSystem(referenceLocationTargetingSystem);
        engine.addSystem(movementTargetingSystem);
        engine.addSystem(entityTargetingSystem);
        engine.addSystem(velocityImpartingSystem);
        engine.addSystem(damageSystem);
        engine.addSystem(igniteSystem);
        engine.addSystem(blockCreationSystem);
        engine.addSystem(projectileSystem);
        engine.addSystem(lightningSystem);
        engine.addSystem(particleSystem);
        engine.addSystem(soundSystem);
        engine.addSystem(timingSystem);
        engine.addSystem(delaySystem);
        engine.addSystem(cleanupSystem);

        return engine;
    }
}
