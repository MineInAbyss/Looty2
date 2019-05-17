package com.derongan.minecraft.looty.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.derongan.minecraft.looty.component.effective.VelocityImparting;
import com.derongan.minecraft.looty.component.internal.Origins;
import com.derongan.minecraft.looty.component.internal.TargetInfo;
import com.derongan.minecraft.looty.component.internal.Targets;
import com.derongan.minecraft.looty.component.target.Beam;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;

//TODO lots of NPE here. Also the AWAY is not working
public class VelocityImpartingSystem extends IteratingSystem {
    public final ComponentMapper<VelocityImparting> velocityImpartingComponentMapper = ComponentMapper.getFor(VelocityImparting.class);
    public final ComponentMapper<Beam> beamComponentMapper = ComponentMapper.getFor(Beam.class);
    private ComponentMapper originsComponentMapper = ComponentMapper.getFor(Origins.ORIGINS_CLASS);
    private ComponentMapper targetInfoComponentMapper = ComponentMapper.getFor(TargetInfo.TARGET_INFO_CLASS);
    private ComponentMapper<Targets> targetsComponentMapper = ComponentMapper.getFor(Targets.class);

    @Inject
    public VelocityImpartingSystem() {
        super(Family.all(VelocityImparting.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        VelocityImparting velocityImparting = velocityImpartingComponentMapper.get(entity);
        TargetInfo targetInfo = (TargetInfo) targetInfoComponentMapper.get(entity);


        double strength = velocityImparting.getStrength();

        Collection<org.bukkit.entity.Entity> entitiesToPush = new ArrayList<>();

        switch (velocityImparting.getTarget()) {
            case INITIATOR:
                targetInfo.getInitiator().ifPresent(entitiesToPush::add);
                break;
            case TARGET:
                entitiesToPush.addAll(targetsComponentMapper.get(entity).getTargets());
                break;
        }


        entitiesToPush.forEach(pushee -> {
            Location from = parseLocationFromReference(velocityImparting.getFrom(), entity, pushee.getLocation());
            Location to = parseLocationFromReference(velocityImparting.getTo(), entity, pushee.getLocation());
            if (to == null || from == null) {
                return;
            }

            Vector directionVector = to.toVector().subtract(from.toVector()).normalize();
            Vector pushVector = directionVector.multiply(strength);

            pushee.setVelocity(pushee.getVelocity().add(pushVector));
        });
    }

    private Location parseLocationFromReference(VelocityImparting.Reference reference,
                                                Entity entity,
                                                Location targetLoc) {
        TargetInfo targetInfo = (TargetInfo) targetInfoComponentMapper.get(entity);
        Origins origins = (Origins) originsComponentMapper.get(entity);

        switch (reference) {
            case TARGET:
                return targetLoc;
            case SKY:
                return targetLoc.clone().add(new Vector(0, 1, 0));
            case GROUND:
                return targetLoc.clone().add(new Vector(0, -1, 0));
            case AWAY:
                if (beamComponentMapper.has(entity)) {
                    Vector start = targetInfo.getInitiator().get().getLocation().toVector();
                    Vector end = origins.getTargetOrigins().get(0).getLocation().toVector();

                    Vector unitDir = end.clone().subtract(start).normalize();

                    Vector pil = start.clone()
                            .add(unitDir.clone().multiply(targetLoc.toVector().subtract(start).dot(unitDir)));

                    return targetLoc.clone().subtract(pil);
                }
            case INITIATOR:
            default:
                return targetInfo.getInitiator().get().getLocation();
        }
    }
}
