package com.derongan.minecraft.looty.skill;

import com.badlogic.ashley.core.Entity;
import com.derongan.minecraft.looty.skill.component.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * A builder that creates an Action Entity
 */
public class ActionEntityBuilder {
    private final List<Component.ComponentCreator> componentBuilders;

    public ActionEntityBuilder() {
        componentBuilders = new ArrayList<>();
    }

    public Entity build() {
        Entity entity = new Entity();
        componentBuilders.stream().map(Supplier::get).forEach(entity::add);

        return entity;
    }

    public ActionEntityBuilder addComponent(Component.ComponentCreator componentCreator) {
        componentBuilders.add(componentCreator);
        return this;
    }
}
