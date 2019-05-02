package com.derongan.minecraft.looty;

import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {PluginModule.class, EngineModule.class})
interface LootyComponent {
    Looty looty();
}
