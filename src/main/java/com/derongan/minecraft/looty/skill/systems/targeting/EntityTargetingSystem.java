package com.derongan.minecraft.looty.skill.systems.targeting;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.derongan.minecraft.looty.skill.component.Self;
import com.derongan.minecraft.looty.skill.component.components.EntityTargets;
import com.derongan.minecraft.looty.skill.component.proto.VolumeInfo;
import com.derongan.minecraft.looty.skill.systems.AbstractDelayAwareIteratingSystem;
import com.google.common.collect.ImmutableSet;

import javax.inject.Inject;
import java.util.logging.Logger;

import static com.derongan.minecraft.looty.skill.systems.targeting.ReferenceLocationTargetingSystem.DEFAULT_TARGET;

public class EntityTargetingSystem extends AbstractDelayAwareIteratingSystem {

    private ComponentMapper<Self> selfComponentMapper = ComponentMapper.getFor(Self.class);

    @Inject
    public EntityTargetingSystem(Logger logger) {
        super(logger, Family.exclude().get());
    }

    @Override
    protected void processFilteredEntity(Entity entity, float deltaTime) {
        if (!entityTargetsComponentMapper.has(entity)) {
            EntityTargets entityTargets = new EntityTargets();
            entity.add(entityTargets);
        }

        EntityTargets entityTargets = entityTargetsComponentMapper.get(entity);

        if (selfComponentMapper.has(entity)) {
            if (selfComponentMapper.get(entity).getInfo().getAlwaysInclude()) {
                entityTargets.affectedEntities.add(actionAttributesComponentMapper.get(entity).initiatorEntity);
            }
        }

        org.bukkit.entity.Entity impactEntity = actionAttributesComponentMapper.get(entity).impactEntity;
        EntityTargetFilter entityTargetFilter = impactEntity == null ? ImmutableSet::of : () -> ImmutableSet.of(impactEntity);
        if (volumeComponentMapper.has(entity)) {

            VolumeInfo volumeInfo = volumeComponentMapper
                    .get(entity).getInfo();
            switch (volumeInfo.getVolumeCase()) {
                case SPHERE:
                    entityTargetFilter = new SphereEntityFilter(targetComponentMapper.get(entity)
                            .getTarget(DEFAULT_TARGET).get().getLocation(), volumeInfo.getSphere().getRadius());
                    break;
                case CYLINDER:
                    entityTargetFilter = new BeamEntityFilter(targetComponentMapper.get(entity)
                            .getTarget(volumeInfo.getCylinder().getFrom())
                            .get()
                            .getLocation(), targetComponentMapper.get(entity)
                            .getTarget(DEFAULT_TARGET)
                            .get()
                            .getLocation(), volumeInfo.getCylinder().getRadius());
                    break;
            }
        }
        entityTargets.affectedEntities = entityTargetFilter.getTargets();
    }
}
