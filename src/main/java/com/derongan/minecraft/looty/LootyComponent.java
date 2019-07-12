package com.derongan.minecraft.looty;

import com.derongan.minecraft.looty.config.ConfigModule;
import com.derongan.minecraft.looty.skill.EngineModule;
import com.derongan.minecraft.looty.ui.GUIModule;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {PluginModule.class, EngineModule.class, ConfigModule.class, GUIModule.class})
interface LootyComponent {
    Looty looty();
}
