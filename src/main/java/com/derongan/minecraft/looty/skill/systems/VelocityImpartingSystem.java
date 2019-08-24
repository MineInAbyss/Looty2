package com.derongan.minecraft.looty.skill.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.derongan.minecraft.looty.skill.component.Velocity;
import com.derongan.minecraft.looty.skill.component.components.ActionAttributes;
import com.derongan.minecraft.looty.skill.component.components.EntityTargets;
import com.derongan.minecraft.looty.skill.component.proto.VelocityInfo;
import org.bukkit.util.Vector;

import javax.inject.Inject;
import java.util.logging.Logger;

public class VelocityImpartingSystem extends AbstractDelayAwareIteratingSystem {
    ComponentMapper<Velocity> velocityComponentMapper = ComponentMapper.getFor(Velocity.class);

    @Inject
    public VelocityImpartingSystem(Logger logger) {
        super(logger, Family.all(Velocity.class).get());
    }

    @Override
    protected void processFilteredEntity(Entity entity, float deltaTime) {
        VelocityInfo velocity = velocityComponentMapper.get(entity).getInfo();
        ActionAttributes actionAttributes = actionAttributesComponentMapper.get(entity);

        if (entityTargetsComponentMapper.has(entity)) {
            EntityTargets entityTargets = entityTargetsComponentMapper.get(entity);


            entityTargets.affectedEntities.forEach(a -> {
                Vector vector = new Vector();
                if (velocity.hasDirection()) {
                    vector = applyVelocityDirection(a, actionAttributes, velocity.getDirection());
                } else if (velocity.hasBetween()) {
                    vector = applyVelocityBetween(entity, velocity.getBetween(), a);
                }

                vector = vector.multiply(velocity.getMagnitude());

                if (velocity.getAdd()) {
                    vector = vector.add(a.getVelocity());
                }

                a.setVelocity(vector);
            });
        }
    }


    private Vector applyVelocityBetween(Entity entity,
                                        VelocityInfo.Between between,
                                        org.bukkit.entity.Entity mob) {
        Vector from = targetComponentMapper.get(entity).getTarget(between.getFrom()).get().getLocation().toVector();
        Vector to = targetComponentMapper.get(entity).getTarget(between.getTo()).get().getLocation().toVector();

        if (!from.subtract(to).equals(new Vector())) {
            return to.subtract(from).normalize();
        } else {
            return new Vector();
        }

    }

    private Vector applyVelocityDirection(org.bukkit.entity.Entity entity,
                                          ActionAttributes actionAttributes,
                                          VelocityInfo.Direction direction) {
        Vector vector;
        switch (direction.getDirectionType()) {
            case UP:
                vector = new Vector(0, 1, 0);
                break;
            case DOWN:
                vector = new Vector(0, -1, 0);
                break;
            case LEFT:
                vector = actionAttributes.referenceHeading.clone().setY(0).rotateAroundY(-90).normalize();
                break;
            case RIGHT:
                vector = actionAttributes.referenceHeading.clone().setY(0).rotateAroundY(90).normalize();
                break;
            case FORWARD:
                vector = actionAttributes.referenceHeading.clone().setY(0).normalize();
                break;
            case BACKWARD:
                vector = actionAttributes.referenceHeading.clone().setY(0).normalize().multiply(-1);
                break;
            case HEADING:
                vector = actionAttributes.referenceHeading.clone().normalize();
                break;
            default:
                vector = new Vector(0, 0, 0);
        }

        return vector;
    }
}
