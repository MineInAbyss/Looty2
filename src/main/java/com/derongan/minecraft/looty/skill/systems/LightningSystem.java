package com.derongan.minecraft.looty.skill.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.derongan.minecraft.looty.skill.component.effective.Lightning;

import javax.inject.Inject;

/**
 * System that handles lightning bolts
 */
public class LightningSystem extends IteratingSystem {
    @Inject
    public LightningSystem() {
        super(Family.all(Lightning.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
    }
}
