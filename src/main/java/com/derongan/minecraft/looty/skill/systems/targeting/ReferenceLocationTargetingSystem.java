package com.derongan.minecraft.looty.skill.systems.targeting;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.derongan.minecraft.looty.DynamicLocation;
import com.derongan.minecraft.looty.OffsetDynamicLocation;
import com.derongan.minecraft.looty.skill.DirectionType;
import com.derongan.minecraft.looty.skill.LocationReferenceType;
import com.derongan.minecraft.looty.skill.component.target.*;
import com.derongan.minecraft.looty.skill.systems.AbstractIteratingSystem;
import org.bukkit.util.Vector;

import javax.inject.Inject;

/**
 * Targeting system that is in charge of preparing reference points.
 */
public class ReferenceLocationTargetingSystem extends AbstractIteratingSystem {
    @Inject
    public ReferenceLocationTargetingSystem() {
        super(Family.one(TargetChooser.class, OriginChooser.class).exclude(Target.class, Origin.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float v) {
        ActionAttributes actionAttributes = actionAttributesComponentMapper.get(entity);

        if (originChooserComponentMapper.has(entity)) {
            setupOrigin(entity, actionAttributes);
        }

        if (targetChooserComponentMapper.has(entity)) {
            setupTarget(entity, actionAttributes);
        }
    }

    private void setupOrigin(Entity entity, ActionAttributes actionAttributes) {
        OriginChooser originChooser = originChooserComponentMapper.get(entity);
        Origin origin = new Origin();
        origin.dynamicLocation = getOffsetLocation(actionAttributes, originChooser);
        entity.add(origin);
    }

    private void setupTarget(Entity entity, ActionAttributes actionAttributes) {
        TargetChooser targetChooser = targetChooserComponentMapper.get(entity);
        Target target = new Target();
        target.dynamicLocation = getOffsetLocation(actionAttributes, targetChooser);
        entity.add(target);
    }

    private DynamicLocation getReferenceLocation(ActionAttributes actionAttributes,
                                                 LocationReferenceType locationReferenceType) {
        switch (locationReferenceType) {
            case INITIATOR:
                return actionAttributes.initiatorLocation;
            case IMPACT:
                return actionAttributes.impactLocation;
            default:
                throw new IllegalStateException("LocationReferenceType must be INITIATOR or IMPACT");
        }
    }

    private Vector getReferenceDirection(DirectionType directionType, Vector referenceHeading) {
        switch (directionType) {
            case UP:
                return new Vector(0, 1, 0);
            case DOWN:
                return new Vector(0, -1, 0);
            case RIGHT:
                return referenceHeading.clone().setY(0).rotateAroundY(Math.PI / 2.0).multiply(-1);
            case LEFT:
                return referenceHeading.clone().setY(0).rotateAroundY(Math.PI / 2.0);
            case FORWARD:
                return referenceHeading.clone().setY(0);
            case BACKWARD:
                return referenceHeading.clone().setY(0).multiply(-1);
            case HEADING:
            default:
                return referenceHeading.clone();
        }
    }

    private DynamicLocation getOffsetLocation(ActionAttributes actionAttributes, Offsetable offsetableChooser) {
        DynamicLocation referenceDynamicLocation = getReferenceLocation(actionAttributes, offsetableChooser.getLocationReferenceType());
        Vector referenceDirection = getReferenceDirection(offsetableChooser.getDirectionType(), actionAttributes.referenceHeading)
                .normalize();

        return new OffsetDynamicLocation(referenceDynamicLocation, referenceDirection.clone()
                .multiply(offsetableChooser.getMagnitude()));
    }
}
