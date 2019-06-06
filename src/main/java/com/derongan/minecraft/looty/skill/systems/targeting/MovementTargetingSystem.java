package com.derongan.minecraft.looty.skill.systems.targeting;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.derongan.minecraft.looty.skill.component.target.*;
import com.derongan.minecraft.looty.skill.systems.AbstractDelayAwareIteratingSystem;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import javax.inject.Inject;
import java.util.logging.Logger;

/**
 * Targeting system that is in charge of creating and moving the target locations
 */
public class MovementTargetingSystem extends AbstractDelayAwareIteratingSystem {
    private final Logger logger;

    @Inject
    MovementTargetingSystem(Logger logger) {
        super(Family.one(Target.class, Origin.class).get());
        this.logger = logger;
    }

    @Override
    protected void processFilteredEntity(Entity entity, float deltaTime) {
        boolean hasOrigin = originComponentMapper.has(entity);
        boolean hasTarget = targetComponentMapper.has(entity);

        if (movementComponentMapper.has(entity)) {
            if (hasTarget && hasOrigin) {
                advanceSectionWithTail(entity);
            } else {

            }
        } else {
            Head head = headComponentMapper.get(entity);
            Tail tail = tailComponentMapper.get(entity);

            if (head == null) {
                head = new Head();
                entity.add(head);
            }

            if (hasOrigin && hasTarget) {
                if (tail == null) {
                    tail = new Tail();
                    entity.add(tail);
                }

                head.location = targetComponentMapper.get(entity).dynamicLocation.getLocation();
                tail.location = originComponentMapper.get(entity).dynamicLocation.getLocation();
            } else if (hasOrigin) {
                head.location = originComponentMapper.get(entity).dynamicLocation.getLocation();
            } else {
                head.location = targetComponentMapper.get(entity).dynamicLocation.getLocation();
            }
        }

    }

    private void advanceSectionWithOutTail(Entity entity) {
        Location targetLocation = targetComponentMapper.get(entity).dynamicLocation.getLocation();
        Location originLocation = originComponentMapper.get(entity).dynamicLocation.getLocation();

        Head head = headComponentMapper.get(entity);

        Movement movement = movementComponentMapper.get(entity);

        double distance = targetLocation.distance(originLocation);
        Vector direction = targetLocation.toVector().subtract(originLocation.toVector()).normalize();

        Vector headStep = direction.clone().multiply(movement.headSpeed);

        if (distance < movement.headSpeed) {
            movement.headSpeed = 0;
            headStep = headStep.normalize().multiply(distance);
        }

        if (distance < movement.headSpeed) {
            movement.headSpeed = 0;
            headStep = headStep.normalize().multiply(distance);
        }
        head.location.add(headStep);
    }

    private void advanceSectionWithTail(Entity entity) {
        Location targetLocation = targetComponentMapper.get(entity).dynamicLocation.getLocation();

        Head head = headComponentMapper.get(entity);
        Tail tail = tailComponentMapper.get(entity);

        if (head == null) {
            head = new Head();
            head.location = originComponentMapper.get(entity).dynamicLocation.getLocation();
            entity.add(head);
        }
        if (tail == null) {
            tail = new Tail();
            tail.location = originComponentMapper.get(entity).dynamicLocation.getLocation();
            entity.add(tail);
        }

        Movement movement = movementComponentMapper.get(entity);

        double headDistance = targetLocation.distance(head.location);
        double tailDistance = targetLocation.distance(tail.location);

        Vector headDirection = targetLocation.toVector().subtract(head.location.toVector()).normalize();
        Vector tailDirection = targetLocation.toVector().subtract(tail.location.toVector()).normalize();

        Vector headStep = headDirection.clone().multiply(movement.headSpeed);
        Vector tailStep = tailDirection.clone().multiply(movement.tailSpeed);

        if (headDistance <= movement.headSpeed) {
            movement.headSpeed = 0;
            headStep = headStep.normalize().multiply(headDistance);
        }

        if (tailDistance <= movement.tailSpeed) {
            movement.tailSpeed = 0;
            tailStep = tailStep.normalize().multiply(tailDistance);
        }

        if (movement.tailWait == 0) {
            tail.location.add(tailStep);
        } else {
            movement.tailWait--;
        }

        head.location.add(headStep);

        if (movement.headSpeed == 0 && movement.tailSpeed == 0) {
            entity.remove(Movement.class);
        }
    }
}
