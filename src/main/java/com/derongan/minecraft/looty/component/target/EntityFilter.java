package com.derongan.minecraft.looty.component.target;

import com.derongan.minecraft.looty.component.Component;
import org.bukkit.entity.Entity;

import java.util.List;


public class EntityFilter implements Component {
    private final List<Class<Entity>> included;
    private final List<Class<Entity>> excluded;

    private EntityFilter(List<Class<Entity>> included, List<Class<Entity>> excluded) {
        this.included = included;
        this.excluded = excluded;
    }

    public List<Class<Entity>> getIncluded() {
        return included;
    }

    public List<Class<Entity>> getExcluded() {
        return excluded;
    }

    public EntityFilter create(List<Class<Entity>> included, List<Class<Entity>> excluded) {
        return new EntityFilter(included, excluded);
    }
}
