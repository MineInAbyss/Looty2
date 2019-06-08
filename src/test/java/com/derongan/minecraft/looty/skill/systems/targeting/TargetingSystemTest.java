package com.derongan.minecraft.looty.skill.systems.targeting;

import com.badlogic.ashley.core.Engine;
import org.junit.Before;

import static org.mockito.Mockito.spy;

public class TargetingSystemTest {
    private MovementTargetingSystem movementTargetingSystem;
    private Engine engine;

    @Before
    public void setUp() {
        movementTargetingSystem = spy(new MovementTargetingSystem(null));
        engine = new Engine();
        engine.addSystem(movementTargetingSystem);
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
//        verify(movementTargetingSystem).processEntity(entity, 1);
//        assertThat(entity.getComponent(Targets.class).getTargets()).containsExactly(spigotEntity);
//    }


}