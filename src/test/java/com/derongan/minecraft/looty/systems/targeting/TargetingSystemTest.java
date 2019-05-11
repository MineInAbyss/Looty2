package com.derongan.minecraft.looty.systems.targeting;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.derongan.minecraft.looty.EntityMockingUtils;
import com.derongan.minecraft.looty.component.internal.Origins;
import com.derongan.minecraft.looty.component.internal.TargetHistory;
import com.derongan.minecraft.looty.component.internal.Targets;
import com.derongan.minecraft.looty.component.target.Radius;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.*;

public class TargetingSystemTest {
    private TargetingSystem targetingSystem;
    private Engine engine;

    @Before
    public void setUp() {
        targetingSystem = spy(new TargetingSystem());
        engine = new Engine();
        engine.addSystem(targetingSystem);
    }

    // TODO right now these tests are crap. But its useful to have something.
//    @Test
//    public void testAddTargets() {
//        Radius radius = Radius.create(5);
//        org.bukkit.entity.Entity spigotEntity = EntityMockingUtils.mockEntity(0, 0, 0);
//        Origins origins = Origins.builder()
//                .setEntityTargetHistory(TargetHistory.<org.bukkit.entity.Entity>builder().addTarget(spigotEntity)
//                        .build()).build();
//        Entity entity = new Entity();
//        entity.add(radius);
//        entity.add(origins);
//        engine.addEntity(entity);
//        when(EntityMockingUtils.getMockWorld()
//                .getNearbyEntities(any(), anyDouble(), anyDouble(), anyDouble())).thenReturn(ImmutableList.of(spigotEntity));
//
//        engine.update(1);
//
//        verify(targetingSystem).processEntity(entity, 1);
//        assertThat(entity.getComponent(Targets.class).getTargets()).containsExactly(spigotEntity);
//    }


}