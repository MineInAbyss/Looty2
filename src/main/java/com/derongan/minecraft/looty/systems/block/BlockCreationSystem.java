package com.derongan.minecraft.looty.systems.block;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.derongan.minecraft.looty.LootyPlugin;
import com.derongan.minecraft.looty.component.effective.Block;
import com.derongan.minecraft.looty.component.internal.Origins;
import com.derongan.minecraft.looty.component.target.Radius;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.util.Vector;

import javax.inject.Inject;

public class BlockCreationSystem extends IteratingSystem {
    private final Server server;
    private final LootyPlugin lootyPlugin;
    private ComponentMapper<Block> blockComponentMapper = ComponentMapper.getFor(Block.class);
    private ComponentMapper<Radius> radiusComponentMapper = ComponentMapper.getFor(Radius.class);
    private ComponentMapper originsComponentMapper = ComponentMapper.getFor(Origins.ORIGINS_CLASS);

    @Inject
    public BlockCreationSystem(Server server, LootyPlugin lootyPlugin) {
        super(Family.all(Block.class).get());
        this.server = server;
        this.lootyPlugin = lootyPlugin;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Block block = blockComponentMapper.get(entity);
        double radius = radiusComponentMapper.get(entity).getRadius();

        if (originsComponentMapper.has(entity)) {


            Origins origins = (Origins) originsComponentMapper.get(entity);


            origins.getTargetOrigins().forEach(a -> {
                Vector center = a.getLocation().toVector();

                for (int i = (int) -radius; i < radius+1; i++) {
                    for (int j = (int) -radius; j < radius+1; j++) {
                        for (int k = (int) -radius; k < radius+1; k++) {
                            Vector loc = center.clone().add(new Vector(i, j, k));
                            if (loc.isInSphere(center, radius)) {
                                if (loc.toLocation(a.getLocation().getWorld()).getBlock().getType() == Material.AIR) {
                                    loc.toLocation(a.getLocation().getWorld()).getBlock().setType(block.getMaterial());

                                    server.getScheduler().scheduleSyncDelayedTask(lootyPlugin, () -> {
                                        loc.toLocation(a.getLocation().getWorld()).getBlock().setType(Material.AIR);
                                    }, block.getTicks());
                                }
                            }
                        }
                    }
                }
            });
        }
    }
}
