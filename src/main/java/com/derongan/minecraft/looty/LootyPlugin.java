package com.derongan.minecraft.looty;

import org.bukkit.plugin.java.JavaPlugin;

class LootyPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        LootyComponent lootyComponent = DaggerLootyComponent.builder()
                .pluginModule(new PluginModule(this))
                .build();

        Looty looty = lootyComponent.looty();
        looty.onEnable();
    }
}
