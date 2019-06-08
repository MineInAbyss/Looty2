package com.derongan.minecraft.looty.skill.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.derongan.minecraft.looty.skill.component.EntityTargets;
import com.derongan.minecraft.looty.skill.component.Ignite;

import javax.inject.Inject;

public class IgniteSystem extends AbstractDelayAwareIteratingSystem {
    private ComponentMapper<Ignite> igniteComponentMapper = ComponentMapper.getFor(Ignite.class);
    @Inject
    public IgniteSystem() {
        super(Family.all(Ignite.class, EntityTargets.class).get());
    }

    @Override
    protected void processFilteredEntity(Entity entity, float deltaTime) {
        double strength = igniteComponentMapper.get(entity).getInfo().getStrength();
        entityTargetsComponentMapper.get(entity).affectedEntities.forEach(ent -> {
            ent.setFireTicks((int) strength);
        });
    }
}
