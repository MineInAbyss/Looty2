package com.derongan.minecraft.looty;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class EntityMockingUtils {
    private static final World mockWorld = mock(World.class);

    public static World getMockWorld(){
        return mockWorld;
    }

    public static Entity mockEntity(double x, double y, double z) {
        Entity entity = mock(Entity.class);
        Location location = mock(Location.class);

        doReturn(location).when(entity).getLocation();

        doReturn(x).when(location).getX();
        doReturn(y).when(location).getY();
        doReturn(z).when(location).getZ();
        doReturn(mockWorld).when(location).getWorld();
        doReturn(new Vector(x, y, z)).when(location).toVector();

        return entity;
    }
}
