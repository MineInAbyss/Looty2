package com.derongan.minecraft.looty.skill.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.derongan.minecraft.looty.skill.component.Linger;
import com.derongan.minecraft.looty.skill.component.LingerInternal;
import com.derongan.minecraft.looty.skill.component.proto.LingerInfo;

import javax.inject.Inject;
import java.util.logging.Logger;

/**
 * System responsible for decrementing timers and setting entities as removed if the timer has expired
 */
public class TimingSystem extends AbstractDelayAwareIteratingSystem {
    private ComponentMapper<Linger> lingerComponentMapper = ComponentMapper.getFor(Linger.class);

    @Inject
    public TimingSystem(Logger logger) {
        super(logger, Family.one(Linger.class, LingerInternal.class).get());
    }

    @Override
    protected void processFilteredEntity(Entity entity, float v) {
        if (persistComponentMapper.has(entity)) {
            LingerInternal lingerInternal = persistComponentMapper.get(entity);
            lingerInternal.ticks--;
            if (lingerInternal.ticks < 0) {
                entity.remove(LingerInternal.class);
            }
        } else {
            Linger linger = lingerComponentMapper.get(entity);

            LingerInfo duration = linger.getInfo();

            LingerInternal lingerInternal = new LingerInternal();
            lingerInternal.ticks = duration.getNumberOfTicks();

            entity.add(lingerInternal);
            entity.remove(Linger.class);
        }
    }
}
