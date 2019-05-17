package com.derongan.minecraft.looty.skill.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.derongan.minecraft.looty.skill.component.effective.Damage;
import com.derongan.minecraft.looty.skill.component.internal.Targets;
import org.bukkit.entity.Damageable;

import javax.inject.Inject;

public class DamageSystem extends IteratingSystem {
    private final ComponentMapper<Damage> damageComponentMapper = ComponentMapper.getFor(Damage.class);
    private final ComponentMapper<Targets> targetsComponentMapper = ComponentMapper.getFor(Targets.class);

    @Inject
    public DamageSystem() {
        super(Family.all(Damage.class, Targets.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        double damage = damageComponentMapper.get(entity).getDamage();
        targetsComponentMapper.get(entity).getTargets().forEach(a -> {
            if (a instanceof Damageable) {
                ((Damageable) a).damage(damage);
            }
        });
    }
}
