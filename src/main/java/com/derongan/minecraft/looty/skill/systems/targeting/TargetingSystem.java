package com.derongan.minecraft.looty.skill.systems.targeting;

import com.badlogic.ashley.core.Entity;
import com.derongan.minecraft.looty.DynamicLocation;
import com.derongan.minecraft.looty.OffsetDynamicLocation;
import com.derongan.minecraft.looty.skill.DirectionType;
import com.derongan.minecraft.looty.skill.LocationReferenceType;
import com.derongan.minecraft.looty.skill.component.Families;
import com.derongan.minecraft.looty.skill.component.target.*;
import com.derongan.minecraft.looty.skill.systems.AbstractIteratingSystem;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import javax.inject.Inject;
import java.util.logging.Logger;

public class TargetingSystem extends AbstractIteratingSystem {
    private final Logger logger;

    @Inject
    TargetingSystem(Logger logger) {
        super(Families.ALL_ENTITIES);
        this.logger = logger;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        setupOriginAndTarget(entity);
        if (speedComponentMapper.has(entity)) {
            advanceSection(entity);
        }
    }

    private void advanceSection(Entity entity) {
        Location targetLocation = targetComponentMapper.get(entity).dynamicLocation.getLocation();
        Location originLocation = originComponentMapper.get(entity).dynamicLocation.getLocation();

        Head head = headComponentMapper.get(entity);
        Tail tail = tailComponentMapper.get(entity);
        if (head == null || tail == null) {
            if (head == null) {
                head = new Head();
                head.location = originLocation.clone();
                entity.add(head);

            }
            if (tail == null) {
                tail = new Tail();
                tail.location = originLocation.clone();
                entity.add(tail);
            }
        }

        if (speedComponentMapper.has(entity)) {
            Speed speed = speedComponentMapper.get(entity);

            Vector direction = targetLocation.toVector().subtract(originLocation.toVector()).normalize();

            Vector headStep = direction.clone().multiply(speed.headSpeed);
            Vector tailStep = direction.clone().multiply(speed.tailSpeed);

            head.location.add(headStep);

            if (speed.tailWait == 0) {
                tail.location.add(tailStep);
            } else {
                speed.tailWait--;
            }

        } else {
            head.location = targetLocation;
            tail.location = originLocation;
        }
    }


    private void setupOriginAndTarget(Entity entity) {
        ActionAttributes actionAttributes = actionAttributesComponentMapper.get(entity);
        if (targetChooserComponentMapper.has(entity) && !targetComponentMapper.has(entity)) {
            Target target = new Target();
            TargetChooser targetChooser = targetChooserComponentMapper.get(entity);
            target.dynamicLocation = getOffsetLocation(actionAttributes, targetChooser);
            entity.add(target);
        }

        if (originChooserComponentMapper.has(entity) && !originComponentMapper.has(entity)) {
            Origin origin = new Origin();
            OriginChooser originChooser = originChooserComponentMapper.get(entity);
            origin.dynamicLocation = getOffsetLocation(actionAttributes, originChooser);
            entity.add(origin);
        } else {
            Origin origin = new Origin();
            origin.dynamicLocation = actionAttributes.initiatorLocation;
            entity.add(origin);
        }
    }

    private DynamicLocation getOffsetLocation(ActionAttributes actionAttributes, Offsetable offsetableChooser) {
        DynamicLocation referenceDynamicLocation = getReferenceLocation(actionAttributes, offsetableChooser.getLocationReferenceType());
        Vector referenceDirection = getReferenceDirection(offsetableChooser.getDirectionType(), actionAttributes.referenceHeading)
                .normalize();

        return new OffsetDynamicLocation(referenceDynamicLocation, referenceDirection.clone()
                .multiply(offsetableChooser.getMagnitude()));
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
}
