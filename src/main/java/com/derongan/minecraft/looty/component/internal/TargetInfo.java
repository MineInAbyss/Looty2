package com.derongan.minecraft.looty.component.internal;


import com.derongan.minecraft.looty.component.Component;
import com.google.auto.value.AutoValue;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.Optional;


/**
 * Represents the initial state of the targeting system.
 */
@AutoValue
public abstract class TargetInfo implements Component {
    public static final Class<AutoValue_TargetInfo> TARGET_INFO_CLASS = AutoValue_TargetInfo.class;

    /**
     * Get the initiator of the action. Usually a player.
     */
    public abstract Optional<Entity> getInitiator();

    /**
     * Get the target Entity of the action.
     */
    public abstract Optional<Entity> getTargetEntity();

    /**
     * Get the entity that this action is spawning from. This is different from initiator, as an action
     * may propogate through entities.
     */
    public abstract Optional<Entity> getOrigin();

    /**
     * Get the target Location of the action.
     */
    public abstract Location getTargetLocation();

    public static Builder builder() {
        return new AutoValue_TargetInfo.Builder();
    }


    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setInitiator(Entity newInitiator);

        public abstract Builder setTargetEntity(Entity newTargetEntity);

        public abstract Builder setOrigin(Entity newOrigin);

        public abstract Builder setTargetLocation(Location newTargetLocation);

        public abstract TargetInfo build();
    }
}
