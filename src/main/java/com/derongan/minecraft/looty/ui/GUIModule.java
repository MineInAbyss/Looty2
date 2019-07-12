package com.derongan.minecraft.looty.ui;

import com.derongan.minecraft.looty.LootyPlugin;
import dagger.Module;
import dagger.Provides;
import org.bukkit.NamespacedKey;

import javax.inject.Qualifier;

@Module
public class GUIModule {
    @Provides

    @GuiKey
    public NamespacedKey providesNamespacedKey(LootyPlugin lootyPlugin) {
        return new NamespacedKey(lootyPlugin, "cursor");
    }

    @Qualifier
    public @interface GuiKey {
    }
}
