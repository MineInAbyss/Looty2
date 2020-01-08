package com.derongan.minecraft.looty;

import com.derongan.minecraft.looty.command.LootyCommandExecutor;
import com.derongan.minecraft.looty.config.ConfigLoader;
import com.derongan.minecraft.looty.skill.SkillListener;
import com.derongan.minecraft.looty.skill.cooldown.CooldownManager;
import org.bukkit.Server;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.logging.Logger;

@Singleton
public
class Looty {
    private final LootyCommandExecutor lootyCommandExecutor;
    private final LootyPlugin lootyPlugin;
    private final CooldownManager cooldownManager;
    private final Server server;
    private final ConfigLoader configLoader;
    private final UpdateTask updateRunnable;
    private final Logger logger;
    private final SkillListener skillListener;

    @Inject
    public Looty(LootyCommandExecutor lootyCommandExecutor,
                 LootyPlugin lootyPlugin,
                 CooldownManager cooldownManager,
                 Server server,
                 ConfigLoader configLoader,
                 UpdateTask updateRunnable,
                 Logger logger, SkillListener skillListener) {
        this.lootyCommandExecutor = lootyCommandExecutor;
        this.lootyPlugin = lootyPlugin;
        this.cooldownManager = cooldownManager;
        this.server = server;
        this.configLoader = configLoader;
        this.updateRunnable = updateRunnable;
        this.logger = logger;
        this.skillListener = skillListener;
    }

    void onEnable() {
        server.getPluginManager().registerEvents(skillListener, lootyPlugin);

        configLoader.reload();

        updateRunnable.runTaskTimer(lootyPlugin, 0, 1);
        // Start after the update
        cooldownManager.start();

        lootyPlugin.getCommand("looty").setExecutor(lootyCommandExecutor);
        logger.info("Loaded Looty");
    }

    void onDisable() {
        cooldownManager.stop();
        updateRunnable.cancel();
    }
}
