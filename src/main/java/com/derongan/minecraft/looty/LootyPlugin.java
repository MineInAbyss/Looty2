package com.derongan.minecraft.looty;

import org.bukkit.plugin.java.JavaPlugin;

public class LootyPlugin extends JavaPlugin {
    private Looty looty;

    @Override
    public void onEnable() {
        LootyComponent lootyComponent = DaggerLootyComponent.builder()
                .pluginModule(new PluginModule(this))
                .build();

        looty = lootyComponent.looty();
        looty.onEnable();
    }

    @Override
    public void onDisable() {
        looty.onDisable();
    }
}
