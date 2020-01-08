package com.derongan.minecraft.looty;

import com.derongan.minecraft.looty.command.CommandModule;
import com.derongan.minecraft.looty.config.ConfigModule;
import com.derongan.minecraft.looty.skill.EngineModule;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {PluginModule.class, EngineModule.class, ConfigModule.class, CommandModule.class})
interface LootyComponent {
    Looty looty();
}
