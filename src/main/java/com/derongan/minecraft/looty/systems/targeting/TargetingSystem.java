package com.derongan.minecraft.looty.systems.targeting;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import com.derongan.minecraft.looty.component.Families;
import com.derongan.minecraft.looty.component.internal.Origins;
import com.derongan.minecraft.looty.component.internal.TargetInfo;
import com.derongan.minecraft.looty.component.internal.Targets;
import com.derongan.minecraft.looty.component.target.Beam;
import com.derongan.minecraft.looty.component.target.Radius;
import com.derongan.minecraft.looty.component.target.Sticky;

import javax.inject.Inject;

public class TargetingSystem extends IteratingSystem {
    private final ComponentMapper targetInfoComponentMapper = ComponentMapper.getFor(TargetInfo.TARGET_INFO_CLASS);
    private final ComponentMapper<Radius> radiusComponentMapper = ComponentMapper.getFor(Radius.class);
    private final ComponentMapper<Beam> beamComponentMapper = ComponentMapper.getFor(Beam.class);
    private final ComponentMapper<Targets> targetsComponentMapper = ComponentMapper.getFor(Targets.class);
    private final ComponentMapper<Sticky> stickyComponentMapper = ComponentMapper.getFor(Sticky.class);

    @Inject
    TargetingSystem() {
        super(Families.TARGETING_FAMILY);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TargetInfo targetInfo = (TargetInfo) targetInfoComponentMapper.get(entity);

        Targets targets;

        if (!targetsComponentMapper.has(entity)) {
            targets = new Targets();
            entity.add(targets);
        } else {
            targets = targetsComponentMapper.get(entity);
        }

        Origins.Builder originsBuilder = Origins.builder();

        if (stickyComponentMapper.has(entity) && targetInfo.getTargetEntity().isPresent()) {
            originsBuilder.addEntityTarget(targetInfo.getTargetEntity().get());
        } else {
            originsBuilder.addLocationTarget(targetInfo.getTargetLocation());
        }

        Origins origins = originsBuilder.build();
        entity.add(origins);

        if (radiusComponentMapper.has(entity)) {
            origins.getTargetOrigins().forEach(origin -> addTargetsForOrigin(origin, entity, targetInfo, targets));
        } else {
            targetInfo.getTargetEntity().ifPresent(targets::addTarget);
        }
    }

    private void addTargetsForOrigin(Origins.Origin origin, Entity entity, TargetInfo targetInfo, Targets targets) {
        int radius = radiusComponentMapper.get(entity).getRadius();

        if (beamComponentMapper.has(entity) && targetInfo.getInitiator().isPresent()) {
            new BeamEntityFilter(targetInfo.getInitiator()
                    .get()
                    .getLocation(), origin.getLocation(), radius, beamComponentMapper.get(entity)
                    .getLength()).getTargets()
                    .forEach(targets::addTarget);
        } else {
            new SphereEntityFilter(origin.getLocation(), radius).getTargets()
                    .forEach(targets::addTarget);
        }
    }
}
