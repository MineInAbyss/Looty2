package com.derongan.minecraft.looty;

import com.derongan.minecraft.looty.item.ConfigItemRegister;
import com.derongan.minecraft.looty.item.NBTItemSkillCache;
import com.derongan.minecraft.looty.item.SkillHolderExtractor;
import com.google.common.collect.ImmutableSet;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ElementsIntoSet;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;

import javax.inject.Qualifier;
import java.io.File;
import java.util.Set;
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


    //TODO different module?
    @Provides
    @ElementsIntoSet
    Set<SkillHolderExtractor> providesSkillHolderTranslator(ConfigItemRegister configItemRegister,
                                                            NBTItemSkillCache nbtItemSkillCache) {
        return ImmutableSet.of(configItemRegister, nbtItemSkillCache);
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
