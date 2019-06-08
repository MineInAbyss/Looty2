package com.derongan.minecraft.looty.skill.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.derongan.minecraft.looty.skill.component.proto.DurationInfo;
import com.derongan.minecraft.looty.skill.component.target.Linger;

import javax.inject.Inject;

/**
 * System responsible for decrementing timers and setting entities as removed if the timer has expired
 */
public class TimingSystem extends AbstractDelayAwareIteratingSystem {
    private ComponentMapper<Linger> lingerComponentMapper = ComponentMapper.getFor(Linger.class);

    @Inject
    public TimingSystem() {
        super(Family.all(Linger.class).get());
    }

    @Override
    protected void processFilteredEntity(Entity entity, float v) {
        Linger linger = lingerComponentMapper.get(entity);

        DurationInfo duration = linger.getInfo();
        if (duration.getNumberOfTicks() > 0) {
            linger.setInfo(duration.toBuilder().setNumberOfTicks(duration.getNumberOfTicks() - 1).build());
        } else {
            entity.remove(Linger.class);
        }
    }
}
