package com.derongan.minecraft.looty.skill.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.derongan.minecraft.looty.skill.component.Damage;
import com.derongan.minecraft.looty.skill.component.components.EntityTargets;
import org.bukkit.entity.Damageable;

import javax.inject.Inject;
import java.util.logging.Logger;

public class DamageSystem extends AbstractDelayAwareIteratingSystem {
    private ComponentMapper<Damage> damageComponentMapper = ComponentMapper.getFor(Damage.class);

    @Inject
    public DamageSystem(Logger logger) {
        super(logger, Family.all(Damage.class, EntityTargets.class).get());
    }

    @Override
    protected void processFilteredEntity(Entity entity, float deltaTime) {
        double damage = damageComponentMapper.get(entity).getInfo().getDamage();
        entityTargetsComponentMapper.get(entity).affectedEntities.forEach(ent -> {
            if (ent instanceof Damageable) {
                ((Damageable) ent).damage(damage);
            }
        });
    }
}
