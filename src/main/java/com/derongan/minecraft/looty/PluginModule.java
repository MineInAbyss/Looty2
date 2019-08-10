package com.derongan.minecraft.looty;

import dagger.Module;
import dagger.Provides;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;

import javax.inject.Qualifier;
import java.io.File;
import java.util.logging.Logger;

@Module
public class PluginModule {
    private final LootyPlugin lootyPlugin;

    PluginModule(LootyPlugin lootyPlugin) {
        this.lootyPlugin = lootyPlugin;
    }

    @Provides
    Server provideServer() {
        return lootyPlugin.getServer();
    }

    @Provides
    Logger provideLogger() {
        return lootyPlugin.getLogger();
    }

    @Provides
    LootyPlugin providesLootyPlugin() {
        return lootyPlugin;
    }

    @Provides
    Plugin providesPlugin() {
        return lootyPlugin;
    }

    @Provides
    @DataFolder
    File providesDataFolder() {
        return lootyPlugin.getDataFolder();
    }

    @Qualifier
    public @interface DataFolder {
    }
}
