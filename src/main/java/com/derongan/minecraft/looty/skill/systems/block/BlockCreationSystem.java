package com.derongan.minecraft.looty.skill.systems.block;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.derongan.minecraft.looty.LootyPlugin;
import com.derongan.minecraft.looty.skill.component.Block;
import org.bukkit.Server;

import javax.inject.Inject;

public class BlockCreationSystem extends IteratingSystem {
    private final Server server;
    private final LootyPlugin lootyPlugin;

    @Inject
    public BlockCreationSystem(Server server, LootyPlugin lootyPlugin) {
        super(Family.all(Block.class).get());
        this.server = server;
        this.lootyPlugin = lootyPlugin;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
    }
}
