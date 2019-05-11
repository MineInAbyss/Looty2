package com.derongan.minecraft.looty.component.internal;

import com.derongan.minecraft.looty.component.Component;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

/**
 * A special component that is created by the trigger system
 */
@AutoValue
public abstract class Origins implements Component {
    public static final Class<AutoValue_Origins> ORIGINS_CLASS = AutoValue_Origins.class;

    public abstract ImmutableList<Origin> getTargetOrigins();

    public static Builder builder() {
        return new AutoValue_Origins.Builder();
    }

    @FunctionalInterface
    public interface Origin {
        Location getLocation();
    }


    @AutoValue.Builder
    public abstract static class Builder {
        abstract ImmutableList.Builder<Origin> targetOriginsBuilder();

        public Builder addEntityTarget(Entity entity) {
            targetOriginsBuilder().add(entity::getLocation);
            return this;
        }

        public Builder addLocationTarget(Location location) {
            targetOriginsBuilder().add(() -> location);
            return this;
        }

        public abstract Origins build();
    }
}
