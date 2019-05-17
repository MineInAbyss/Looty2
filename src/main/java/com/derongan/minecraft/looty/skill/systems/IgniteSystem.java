package com.derongan.minecraft.looty.skill.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.derongan.minecraft.looty.skill.component.effective.Ignite;
import com.derongan.minecraft.looty.skill.component.internal.Targets;

import javax.inject.Inject;

public class IgniteSystem extends IteratingSystem {
    private final ComponentMapper<Ignite> igniteComponentMapper = ComponentMapper.getFor(Ignite.class);
    private final ComponentMapper<Targets> targetsComponentMapper = ComponentMapper.getFor(Targets.class);

    @Inject
    public IgniteSystem() {
        super(Family.all(Ignite.class, Targets.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Ignite ignite = igniteComponentMapper.get(entity);
        Targets  targets = targetsComponentMapper.get(entity);

        targets.getTargets().forEach(target->{
            target.setFireTicks(Math.max(target.getFireTicks(), ignite.getStrength()));
        });
    }
}
