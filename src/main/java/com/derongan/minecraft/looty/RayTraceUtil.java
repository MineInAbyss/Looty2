package com.derongan.minecraft.looty;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

public class RayTraceUtil {
    static RayTraceResult getRayTraceResultOrDefault(Player player, double range) {
        RayTraceResult rayTraceResult = player
                .getWorld()
                .rayTrace(player.getEyeLocation(), player.getEyeLocation()
                        .getDirection(), range, FluidCollisionMode.NEVER, true, .5, (entity -> entity != player));


        if (rayTraceResult == null) {
            Location hitLocation = player.getEyeLocation()
                    .clone()
                    .add(player.getEyeLocation().getDirection().clone().normalize().multiply(range));
            rayTraceResult = new RayTraceResult(hitLocation.toVector());
        }

        return rayTraceResult;
    }
}
