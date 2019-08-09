package com.derongan.minecraft.looty;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import javax.inject.Inject;
import java.util.logging.Logger;

public class TestListener implements Listener {
    private final Logger logger;

    @Inject
    public TestListener(Logger logger) {
        this.logger = logger;
    }

    @EventHandler
    public void onPlayerAnimationEvent(PlayerAnimationEvent playerAnimationEvent) {
        logger.info("Player Animate");
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEntityEvent playerInteractEntityEvent) {
        logger.info(String.format("Player Interact Entity: %s", playerInteractEntityEvent.getClass().getSimpleName()));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent playerInteractEntityEvent) {
        logger.info(String.format("Player Interact: %s", playerInteractEntityEvent.getAction().toString()));
    }
}
