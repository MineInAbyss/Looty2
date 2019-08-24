package com.derongan.minecraft.looty.skill.systems.targeting;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.derongan.minecraft.looty.location.*;
import com.derongan.minecraft.looty.skill.component.components.ActionAttributes;
import com.derongan.minecraft.looty.skill.component.components.Targets;
import com.derongan.minecraft.looty.skill.component.proto.DirectionType;
import com.derongan.minecraft.looty.skill.component.proto.LocationReferenceType;
import com.derongan.minecraft.looty.skill.component.proto.Offset;
import com.derongan.minecraft.looty.skill.systems.AbstractDelayAwareIteratingSystem;
import org.bukkit.util.Vector;

import javax.inject.Inject;
import java.util.logging.Logger;

/**
 * Targeting system that is in charge of preparing reference points.
 */
public class ReferenceLocationTargetingSystem extends AbstractDelayAwareIteratingSystem {

    public static final String DEFAULT_TARGET = "target";

    @Inject
    public ReferenceLocationTargetingSystem(Logger logger) {
        super(logger, Family.all().exclude(Targets.class).get());
    }

    @Override
    protected void processFilteredEntity(Entity entity, float v) {
        ActionAttributes actionAttributes = actionAttributesComponentMapper.get(entity);

        Targets targets = new Targets();

        if (targetingComponentMapper.has(entity)) {
            targetingComponentMapper.get(entity)
                    .getInfo()
                    .getOffsetMap()
                    .forEach((name, offset) -> targets.addTarget(name, createTarget(offset, actionAttributes)));
        }

        if (!targets.getTarget(DEFAULT_TARGET).isPresent()) {
            targets.addTarget(DEFAULT_TARGET, new MovingDynamicLocation(actionAttributes.impactLocation));
        }

        entity.add(targets);
    }

    private MovingDynamicLocation createTarget(Offset offset, ActionAttributes actionAttributes) {
        return new MovingDynamicLocation(getOffsetLocation(actionAttributes, offset, offset.getGrounded()));
    }

    private DynamicLocation getReferenceLocation(ActionAttributes actionAttributes,
                                                 LocationReferenceType locationReferenceType, boolean sticky) {
        switch (locationReferenceType) {
            case INITIATOR:
            case OWNER:
                return sticky && actionAttributes.initiatorEntity != null ? new StickyDynamicLocation(actionAttributes.initiatorEntity) : actionAttributes.initiatorLocation;
            case IMPACT:
                return sticky && actionAttributes.impactEntity != null ? new StickyDynamicLocation(actionAttributes.impactEntity) : actionAttributes.impactLocation;
            default:
                throw new IllegalStateException("LocationReferenceType must be INITIATOR, IMPACT or ORIGIN");
        }
    }

    private Vector getReferenceVector(DirectionType directionType, Vector referenceHeading) {
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

    private DynamicLocation getOffsetLocation(ActionAttributes actionAttributes,
                                              Offset offset,
                                              boolean grounded) {
        DynamicLocation referenceDynamicLocation = getReferenceLocation(actionAttributes, offset.getLocationReferenceType(), offset
                .getSticky());
        Vector referenceVector = getReferenceVector(offset.getDirectionType(), actionAttributes.referenceHeading)
                .normalize();

        if (grounded) {
            referenceDynamicLocation = new GroundingDynamicLocation(referenceDynamicLocation);
        }

        com.derongan.minecraft.looty.skill.component.proto.Vector modifierProtoVector = offset.getModifierVector();

        Vector modifierVector = new Vector(modifierProtoVector.getX(), modifierProtoVector.getY(), modifierProtoVector.getZ());

        //TODO the math here is bad
        double offsetAngleFromX = referenceVector.clone().setY(0).angle(new Vector(1, 0, 0));

        modifierVector.rotateAroundY(offsetAngleFromX);

        return new OffsetDynamicLocation(referenceDynamicLocation, referenceVector.clone()
                .multiply(offset.getMagnitude()), modifierVector);
    }
}
