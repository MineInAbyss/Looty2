package com.derongan.minecraft.looty.systems.targeting;

import com.derongan.minecraft.looty.EntityMockingUtils;
import com.google.common.collect.ImmutableList;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.junit.Test;

import java.util.Collection;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.*;

public class SphereFilterTest {

    @Test
    public void testSphereFilter() {
        World world = mock(World.class);
        Entity inside1 = EntityMockingUtils.mockEntity(5, 5, 0);
        Entity inside2 = EntityMockingUtils.mockEntity(5, 5, 5);
        Entity outside1 = EntityMockingUtils.mockEntity(11, 0, 0);
        Entity outside2 = EntityMockingUtils.mockEntity(10, 10, 0);
        Collection<Entity> allEntities = ImmutableList.of(inside1, inside2, outside1, outside2);
        when(world.getNearbyEntities(any(), anyDouble(), anyDouble(), anyDouble())).thenReturn(allEntities);
        Location target = new Location(world, 5, 5, 5);
        SphereEntityFilter sphereFilter = new SphereEntityFilter(target, 5);

        Collection<Entity> actual = sphereFilter.getTargets();

        assertThat(actual).containsExactly(inside1, inside2);
    }

}