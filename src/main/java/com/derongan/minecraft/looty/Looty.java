package com.derongan.minecraft.looty;

import com.derongan.minecraft.guiy.GuiListener;
import com.derongan.minecraft.looty.config.ConfigLoader;
import com.derongan.minecraft.looty.skill.SkillListener;
import com.derongan.minecraft.looty.ui.LootyEditorCommandExecutor;
import com.derongan.minecraft.looty.ui.LootyEditorListener;
import org.bukkit.Server;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.logging.Logger;

@Singleton
public
class Looty {
    private final LootyCommandExecutor lootyCommandExecutor;
    private final LootyEditorCommandExecutor LootyEditorCommandExecutor;
    private final TestListener testListener;
    private final LootyEditorListener lootyEditorListener;
    private final LootyPlugin lootyPlugin;
    private final GuiListener guiListener;
    private final Server server;
    private final ConfigLoader configLoader;
    private final UpdateTask updateRunnable;
    private final Logger logger;
    private final SkillListener skillListener;

    @Inject
    public Looty(LootyCommandExecutor lootyCommandExecutor,
                 LootyEditorCommandExecutor LootyEditorCommandExecutor,
                 TestListener testListener,
                 LootyEditorListener lootyEditorListener,
                 LootyPlugin lootyPlugin,
                 GuiListener guiListener,
                 Server server,
                 ConfigLoader configLoader,
                 UpdateTask updateRunnable,
                 Logger logger, SkillListener skillListener) {
        this.lootyCommandExecutor = lootyCommandExecutor;
        this.LootyEditorCommandExecutor = LootyEditorCommandExecutor;
        this.testListener = testListener;
        this.lootyEditorListener = lootyEditorListener;
        this.lootyPlugin = lootyPlugin;
        this.guiListener = guiListener;
        this.server = server;
        this.configLoader = configLoader;
        this.updateRunnable = updateRunnable;
        this.logger = logger;
        this.skillListener = skillListener;
    }

    void onEnable() {
        server.getPluginManager().registerEvents(lootyEditorListener, lootyPlugin);
//        server.getPluginManager().registerEvents(testListener, lootyPlugin);
        server.getPluginManager().registerEvents(skillListener, lootyPlugin);
        server.getPluginManager().registerEvents(guiListener, lootyPlugin);

        configLoader.reload();

        updateRunnable.runTaskTimer(lootyPlugin, 0, 1);

        lootyPlugin.getCommand("looty").setExecutor(lootyCommandExecutor);
        lootyPlugin.getCommand("looties").setExecutor(lootyCommandExecutor);
        lootyPlugin.getCommand("lootyreload").setExecutor(lootyCommandExecutor);
        lootyPlugin.getCommand("createskill").setExecutor(LootyEditorCommandExecutor);
        lootyPlugin.getCommand("createaction").setExecutor(LootyEditorCommandExecutor);
        logger.info("Loaded Looty");
    }

    void onDisable() {
        updateRunnable.cancel();
    }
}
