package com.derongan.minecraft.looty.skill.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.derongan.minecraft.looty.skill.component.effective.Damage;
import com.derongan.minecraft.looty.skill.component.target.EntityTargets;
import org.bukkit.entity.Damageable;

import javax.inject.Inject;

public class DamageSystem extends AbstractDelayAwareIteratingSystem {
    private ComponentMapper<Damage> damageComponentMapper = ComponentMapper.getFor(Damage.class);

    @Inject
    public DamageSystem() {
        super(Family.all(Damage.class, EntityTargets.class).get());
    }

    @Override
    protected void processFilteredEntity(Entity entity, float deltaTime) {
        double damage = damageComponentMapper.get(entity).getDamage();
        entityTargetsComponentMapper.get(entity).affectedEntities.forEach(ent -> {
            if (ent instanceof Damageable) {
                ((Damageable) ent).damage(damage);
            }
        });
    }
}
