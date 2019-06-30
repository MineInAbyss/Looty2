package com.derongan.minecraft.looty.skill.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.derongan.minecraft.looty.skill.component.Delay;
import com.derongan.minecraft.looty.skill.component.DelayInternal;
import com.derongan.minecraft.looty.skill.component.proto.DelayInfo;

import javax.inject.Inject;

/**
 * System responsible for decrementing delay and removing the attribute once expired
 */
public class DelaySystem extends IteratingSystem {
    private ComponentMapper<Delay> delayComponentMapper = ComponentMapper.getFor(Delay.class);
    private ComponentMapper<DelayInternal> delayInternalComponentMapper = ComponentMapper.getFor(DelayInternal.class);

    @Inject
    public DelaySystem() {
        super(Family.one(Delay.class, DelayInternal.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float v) {
        if (delayInternalComponentMapper.has(entity)) {
            DelayInternal delayInternalingerInternal = delayInternalComponentMapper.get(entity);
            delayInternalingerInternal.ticks--;
            if (delayInternalingerInternal.ticks < 0) {
                entity.remove(DelayInternal.class);
            }
        } else {
            Delay linger = delayComponentMapper.get(entity);

            DelayInfo duration = linger.getInfo();

            DelayInternal delayInternal = new DelayInternal();
            delayInternal.ticks = duration.getNumberOfTicks();

            entity.add(delayInternal);
            entity.remove(Delay.class);
        }
    }
}
