package com.derongan.minecraft.looty.config;

import com.derongan.minecraft.looty.PluginModule;
import com.derongan.minecraft.looty.config.deserialization.ItemParser;
import com.derongan.minecraft.looty.item.ConfigItemRegister;
import com.google.common.io.Files;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

public class ConfigLoader {
    private final ConfigItemRegister itemRegistrar;
    private final File dataFolder;
    private final ItemParser itemParser;
    private final Logger logger;

    @Inject
    public ConfigLoader(ConfigItemRegister itemRegistrar,
                        @PluginModule.DataFolder File dataFolder,
                        ItemParser itemParser,
                        Logger logger) {
        this.itemRegistrar = itemRegistrar;
        this.dataFolder = dataFolder;
        this.itemParser = itemParser;
        this.logger = logger;
    }

    public void reload() {
        itemRegistrar.clear();

        if (!dataFolder.exists()) {
            boolean created = dataFolder.mkdir();
            if (!created) {
                throw new RuntimeException("Failed to create plugin directory");
            }
        }
        for (File file : dataFolder.listFiles()) {
            try (InputStream inputStream = Files.asByteSource(file).openStream()) {
                itemRegistrar.register(itemParser.fromConfig(inputStream));
            } catch (IOException e) {
                logger.warning(String.format("Failed to load config %s", file.getName()));
                logger.warning(e.getMessage());
            }
        }
    }
}
