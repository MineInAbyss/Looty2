package com.derongan.minecraft.looty.skill.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.derongan.minecraft.looty.skill.component.effective.Lightning;
import com.derongan.minecraft.looty.skill.component.internal.Origins;
import com.derongan.minecraft.looty.skill.component.internal.Targets;

import javax.inject.Inject;

/**
 * System that handles lightning bolts
 */
public class LightningSystem extends IteratingSystem {
    private final ComponentMapper<Lightning> lightningComponentMapper = ComponentMapper.getFor(Lightning.class);
    private ComponentMapper originsComponentMapper = ComponentMapper.getFor(Origins.ORIGINS_CLASS);
    private final ComponentMapper<Targets> targetsComponentMapper = ComponentMapper.getFor(Targets.class);

    @Inject
    public LightningSystem() {
        super(Family.all(Lightning.class).get());
    }

    //TODO spawn without sound
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        targetsComponentMapper.get(entity).getTargets().forEach(e -> e.getWorld().strikeLightningEffect(e.getLocation()));
    }
}
