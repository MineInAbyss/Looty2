package com.derongan.minecraft.looty.skill.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.derongan.minecraft.looty.skill.component.effective.Damage;

import javax.inject.Inject;

public class DamageSystem extends IteratingSystem {
    @Inject
    public DamageSystem() {
        super(Family.all(Damage.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
    }
}
