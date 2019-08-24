package com.derongan.minecraft.looty.skill.systems.targeting;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.derongan.minecraft.looty.location.MovingDynamicLocation;
import com.derongan.minecraft.looty.skill.component.Movement;
import com.derongan.minecraft.looty.skill.component.components.Targets;
import com.derongan.minecraft.looty.skill.component.proto.MovementInfo;
import com.derongan.minecraft.looty.skill.systems.AbstractDelayAwareIteratingSystem;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import javax.inject.Inject;
import java.util.logging.Logger;

import static com.derongan.minecraft.looty.skill.systems.targeting.TargetingUtils.getTargetOrThrow;

/**
 * Targeting system that is in charge of creating and moving the target locations
 */
public class MovementTargetingSystem extends AbstractDelayAwareIteratingSystem {
    @Inject
    MovementTargetingSystem(Logger logger) {
        super(logger, Family.all(Movement.class).get());
    }

    @Override
    protected void processFilteredEntity(Entity entity, float deltaTime) {
        Movement movement = movementComponentMapper.get(entity);

        movement.getInfo().getMovementList().forEach(movementData -> updateTarget(movementData, entity));
    }

    private void updateTarget(MovementInfo.MovementData movementData, Entity entity) {
        Targets targets = targetComponentMapper.get(entity);

        MovingDynamicLocation movingDynamicLocation = getTargetOrThrow(targets, movementData.getNode());

        switch (movementData.getVelocitySpecCase()) {
            case VELOCITY_DIRECTION:
                Vector toApply = getVectorForDirection(movementData.getSpeed(), movementData.getVelocityDirection(), targets);
                movingDynamicLocation.setInitialVelocity(toApply);
                break;
            case VELOCITY_DESTINATION:
                break;
        }

        switch (movementData.getAccelerationSpecCase()) {
            case ACCELERATION_DIRECTION:
                Vector toApply = getVectorForDirection(movementData.getAcceleration(), movementData.getAccelerationDirection(), targets);
                movingDynamicLocation.getVelocity().add(toApply);
                break;
            case ACCELERATION_DESTINATION:
                break;
        }

        movingDynamicLocation.update();
    }

    private Vector getVectorForDirection(double magnitude, MovementInfo.DirectionSpec directionSpec, Targets targets) {
        String from = directionSpec.getFrom();
        String to = directionSpec.getTo();

        Location fromLoc = getTargetOrThrow(targets, from).getLocation();
        Location toLoc = getTargetOrThrow(targets, to).getLocation();

        Vector direction = toLoc.toVector().subtract(fromLoc.toVector());

        if (direction.lengthSquared() == 0) {
            return new Vector();
        }

        return direction.normalize().multiply(magnitude);
    }
}
