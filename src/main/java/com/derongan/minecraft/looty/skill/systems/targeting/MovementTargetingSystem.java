package com.derongan.minecraft.looty.skill.systems.targeting;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.derongan.minecraft.looty.skill.component.Movement;
import com.derongan.minecraft.looty.skill.component.components.Head;
import com.derongan.minecraft.looty.skill.component.components.Origin;
import com.derongan.minecraft.looty.skill.component.components.Tail;
import com.derongan.minecraft.looty.skill.component.components.Target;
import com.derongan.minecraft.looty.skill.component.proto.MovementInfo;
import com.derongan.minecraft.looty.skill.systems.AbstractDelayAwareIteratingSystem;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import javax.inject.Inject;
import java.util.logging.Logger;

/**
 * Targeting system that is in charge of creating and moving the target locations
 */
// TODO movement info should probably create mutable components to hold the actual state
public class MovementTargetingSystem extends AbstractDelayAwareIteratingSystem {
    @Inject
    MovementTargetingSystem(Logger logger) {
        super(logger, Family.one(Target.class, Origin.class).get());
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

    //TODO there is a lot of logic that can be cleaned up here
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
            tail.wait = movementComponentMapper.get(entity).getInfo().getTailWait();
            tail.location = originComponentMapper.get(entity).dynamicLocation.getLocation();
            entity.add(tail);
        }

        Movement movement = movementComponentMapper.get(entity);
        MovementInfo mvInfo = movement.getInfo();

        double headDistance = targetLocation.distance(head.location);
        double tailDistance = targetLocation.distance(tail.location);

        Vector headDirection = targetLocation.toVector().subtract(head.location.toVector()).normalize();
        Vector tailDirection = targetLocation.toVector().subtract(tail.location.toVector()).normalize();

        Vector headStep = headDirection.clone().multiply(mvInfo.getHeadSpeed());
        Vector tailStep = tailDirection.clone().multiply(mvInfo.getTailSpeed());

        if (headDistance <= mvInfo.getHeadSpeed()) {
            headStep = headStep.normalize().multiply(headDistance);
        }

        if (tailDistance <= mvInfo.getTailSpeed()) {
            tailStep = tailStep.normalize().multiply(tailDistance);
        }

        if (tail.wait == 0) {
            if (tailDistance != 0) {
                tail.location.add(tailStep);
            }
        } else {
            tail.wait--;
        }

        if (headDistance != 0) {
            head.location.add(headStep);
        }

        if (headDistance == 0 && tailDistance == 0) {
            entity.remove(Movement.class);
        }
    }
}
