package com.derongan.minecraft.looty.skill.systems;

import com.badlogic.ashley.core.Engine;
import org.bukkit.entity.Damageable;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.spy;

public class DamageSystemTest {
    private DamageSystem damageSystem;
    private Engine engine;

    @Before
    public void setUp() {
        damageSystem = spy(new DamageSystem(null));
        this.engine = new Engine();
        engine.addSystem(damageSystem);
    }

    @Test
    public void testDamageEntities() {
//        int damageAmount = 5;
//        Targets targets = new Targets();
//        DamageableEntity mockDamageableEntity = mock(DamageableEntity.class);
//        org.bukkit.entity.Entity mockNonDamageableEntity = mock(org.bukkit.entity.Entity.class);
//        targets.addTarget(mockDamageableEntity);
//        targets.addTarget(mockNonDamageableEntity);
//        Entity usableEntity = new Entity();
//        Damage damage = Damage.create(damageAmount);
//        usableEntity.add(targets);
//        usableEntity.add(damage);
//        Entity notUsableEntity = new Entity();
//        notUsableEntity.add(targets);
//        engine.addEntity(notUsableEntity);
//        engine.addEntity(usableEntity);
//
//        engine.update(1);
//
//        verify(mockDamageableEntity).damage(damageAmount);
//        verify(damageSystem).processEntity(usableEntity, 1);
//        verify(damageSystem, never()).processEntity(notUsableEntity, 1);
    }

    private interface DamageableEntity extends Damageable, org.bukkit.entity.Entity {
    }
}