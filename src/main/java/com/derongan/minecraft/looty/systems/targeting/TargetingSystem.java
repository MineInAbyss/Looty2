package com.derongan.minecraft.looty.systems.targeting;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import com.derongan.minecraft.looty.component.Families;
import com.derongan.minecraft.looty.component.internal.Origins;
import com.derongan.minecraft.looty.component.internal.TargetInfo;
import com.derongan.minecraft.looty.component.internal.Targets;
import com.derongan.minecraft.looty.component.target.Beam;
import com.derongan.minecraft.looty.component.target.Radius;
import com.derongan.minecraft.looty.component.target.Sticky;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.logging.Logger;

public class TargetingSystem extends IteratingSystem {
    private final ComponentMapper targetInfoComponentMapper = ComponentMapper.getFor(TargetInfo.TARGET_INFO_CLASS);
    private final ComponentMapper<Radius> radiusComponentMapper = ComponentMapper.getFor(Radius.class);
    private final ComponentMapper<Beam> beamComponentMapper = ComponentMapper.getFor(Beam.class);
    private final ComponentMapper<Targets> targetsComponentMapper = ComponentMapper.getFor(Targets.class);
    private final ComponentMapper<Sticky> stickyComponentMapper = ComponentMapper.getFor(Sticky.class);
    private final Logger logger;

    @Inject
    TargetingSystem(Logger logger) {
        super(Families.TARGETING_FAMILY);
        this.logger = logger;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TargetInfo targetInfo = (TargetInfo) targetInfoComponentMapper.get(entity);

        Targets targets;

        if (!targetsComponentMapper.has(entity)) {
            targets = new Targets();
            entity.add(targets);
        } else {
            targets = targetsComponentMapper.get(entity);
        }

        Origins.Builder originsBuilder = Origins.builder();

        if (stickyComponentMapper.has(entity) && targetInfo.getTargetEntity().isPresent()) {
            originsBuilder.addEntityTarget(targetInfo.getTargetEntity().get());
        } else {
            originsBuilder.addLocationTarget(targetInfo.getTargetLocation());
        }

        Origins origins = originsBuilder.build();
        entity.add(origins);

        if (radiusComponentMapper.has(entity)) {
            origins.getTargetOrigins().forEach(origin -> addTargetsForOrigin(origin, entity, targetInfo, targets));
        } else {
            targetInfo.getTargetEntity().ifPresent(targets::addTarget);
        }
    }

    private void addTargetsForOrigin(Origins.Origin origin, Entity entity, TargetInfo targetInfo, Targets targets) {
        double radius = radiusComponentMapper.get(entity).getRadius();

        if (beamComponentMapper.has(entity) && targetInfo.getInitiator().isPresent()) {
            logger.info("Attempting to fire beam");
            org.bukkit.entity.Entity initiator = targetInfo.getInitiator().get();
            Location initiatorLocation = initiator instanceof Player ? ((Player) initiator).getEyeLocation() : initiator
                    .getLocation()
                    .clone()
                    .add(0, initiator.getHeight() * .9, 0);
            new BeamEntityFilter(initiatorLocation, origin.getLocation(), radius, beamComponentMapper.get(entity)
                    .getLength()).getTargets()
                    .forEach(targets::addTarget);
        } else {
            new SphereEntityFilter(origin.getLocation(), radius).getTargets()
                    .forEach(targets::addTarget);
        }
    }
}
