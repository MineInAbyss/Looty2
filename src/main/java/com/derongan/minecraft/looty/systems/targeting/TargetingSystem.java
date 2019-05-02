package com.derongan.minecraft.looty.systems.targeting;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import com.derongan.minecraft.looty.component.Families;
import com.derongan.minecraft.looty.component.internal.TargetInfo;
import com.derongan.minecraft.looty.component.internal.Targets;
import com.derongan.minecraft.looty.component.target.Beam;
import com.derongan.minecraft.looty.component.target.Radius;

import javax.inject.Inject;

public class TargetingSystem extends IteratingSystem {
    private final ComponentMapper<TargetInfo> targetInfoComponentMapper = ComponentMapper.getFor(TargetInfo.class);
    private final ComponentMapper<Radius> radiusComponentMapper = ComponentMapper.getFor(Radius.class);
    private final ComponentMapper<Beam> beamComponentMapper = ComponentMapper.getFor(Beam.class);
    private final ComponentMapper<Targets> targetsComponentMapper = ComponentMapper.getFor(Targets.class);

    @Inject
    TargetingSystem() {
        super(Families.TARGETING_FAMILY);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TargetInfo targetInfo = targetInfoComponentMapper.get(entity);

        Targets targets;

        if (!targetsComponentMapper.has(entity)) {
            targets = new Targets();
            entity.add(targets);
        } else {
            targets = targetsComponentMapper.get(entity);
        }

        if (radiusComponentMapper.has(entity) && targetInfo.getTarget().isPresent()) {
            int radius = radiusComponentMapper.get(entity).getRadius();

            if (beamComponentMapper.has(entity) && targetInfo.getTarget().isPresent() && targetInfo.getInitiator()
                    .isPresent()) {
                new BeamFilter(targetInfo.getInitiator().get().getLocation(), targetInfo.getTarget()
                        .get()
                        .getLocation(), radius, beamComponentMapper.get(entity).getLength()).getTargets()
                        .forEach(targets::addTarget);
            } else {
                new SphereFilter(targetInfo.getTarget().get().getLocation(), radius).getTargets()
                        .forEach(targets::addTarget);
            }
        }
    }
}
