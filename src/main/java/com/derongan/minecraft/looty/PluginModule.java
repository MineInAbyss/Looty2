package com.derongan.minecraft.looty;

import dagger.Module;
import dagger.Provides;
import org.bukkit.Server;

import java.util.logging.Logger;

@Module
class PluginModule {
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
}
