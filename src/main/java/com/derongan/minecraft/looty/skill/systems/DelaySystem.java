package com.derongan.minecraft.looty.skill.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.derongan.minecraft.looty.skill.component.Delay;

import javax.inject.Inject;

/**
 * System responsible for decrementing delay and removing the attribute once expired
 */
public class DelaySystem extends IteratingSystem {
    private ComponentMapper<Delay> delayComponentMapper = ComponentMapper.getFor(Delay.class);

    @Inject
    public DelaySystem() {
        super(Family.all(Delay.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float v) {
        Delay delay = delayComponentMapper.get(entity);

        if (delay.delay > 0) {
            delay.delay--;
        } else {
            entity.remove(Delay.class);
        }
    }
}
