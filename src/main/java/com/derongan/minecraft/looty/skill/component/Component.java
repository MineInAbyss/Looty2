package com.derongan.minecraft.looty.skill.component;

//TODO each of these components should require a builder with a shared interface. That way we can
// more easily create ActionBuilders that create NEW components on the fly. (This is required because
// Ashley doesn't like registering the same component twice. Its also just a good idea)

import java.util.function.Supplier;

//TODO all components should be mutable. Part of a successful ECS is having a system update a component
// based on other components.
public interface Component extends com.badlogic.ashley.core.Component {

    @FunctionalInterface
    interface ComponentCreator extends Supplier<Component> {
    }
}
