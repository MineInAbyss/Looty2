package com.derongan.minecraft.looty.skill.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.derongan.minecraft.looty.skill.component.effective.Ignite;

import javax.inject.Inject;

public class IgniteSystem extends IteratingSystem {
    @Inject
    public IgniteSystem() {
        super(Family.all(Ignite.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
    }
}
