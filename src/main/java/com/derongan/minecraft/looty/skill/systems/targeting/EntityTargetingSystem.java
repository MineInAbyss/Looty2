package com.derongan.minecraft.looty.skill.systems.targeting;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.derongan.minecraft.looty.skill.component.target.EntityTargets;
import com.derongan.minecraft.looty.skill.systems.AbstractDelayAwareIteratingSystem;
import com.google.common.collect.ImmutableSet;

import javax.inject.Inject;
import java.util.stream.Collectors;

public class EntityTargetingSystem extends AbstractDelayAwareIteratingSystem {

    @Inject
    public EntityTargetingSystem() {
        super(Family.exclude().get());
    }

    @Override
    protected void processFilteredEntity(Entity entity, float deltaTime) {
        if (!entityTargetsComponentMapper.has(entity)) {
            EntityTargets entityTargets = new EntityTargets();
            entity.add(entityTargets);
        }

        EntityTargets entityTargets = entityTargetsComponentMapper.get(entity);

        if (headComponentMapper.has(entity) && !tailComponentMapper.has(entity)) {
            if (radiusComponentMapper.has(entity)) {
                SphereEntityFilter sphereEntityFilter = new SphereEntityFilter(headComponentMapper.get(entity).location, radiusComponentMapper
                        .get(entity).radius);
                entityTargets.affectedEntities = sphereEntityFilter.getTargets();
            } else if (actionAttributesComponentMapper.get(entity).impactEntity != null) {
                entityTargets.affectedEntities = ImmutableSet.of(actionAttributesComponentMapper.get(entity).impactEntity);
            }
        } else if (headComponentMapper.has(entity) && tailComponentMapper.has(entity)) {
            double radius = .1;
            if (radiusComponentMapper.has(entity)) {
                radius = radiusComponentMapper.get(entity).radius;
            }

            BeamEntityFilter beamEntityFilter = new BeamEntityFilter(headComponentMapper.get(entity).location, tailComponentMapper
                    .get(entity).location, radius);

            entityTargets.affectedEntities = beamEntityFilter.getTargets();
        }

        if (entityTargetLimitComponentMapper.has(entity)) {
            entityTargets.affectedEntities = entityTargets.affectedEntities.stream()
                    .limit(entityTargetLimitComponentMapper.get(entity).limit)
                    .collect(Collectors.toSet());
        }
    }
}
