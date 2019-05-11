package com.derongan.minecraft.looty.component.internal;

import com.derongan.minecraft.looty.component.Component;
import com.google.auto.value.AutoValue;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.Optional;

/**
 * A special component that is created by the trigger system
 */
@AutoValue
public abstract class TargetInfo implements Component {
    public abstract TargetHistory<Entity> getEntityTargetHistory();
    public abstract TargetHistory<Location> getLocationTargetHistory();

    public static Builder builder() {
        return new AutoValue_TargetInfo.Builder();
    }


    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setEntityTargetHistory(TargetHistory<Entity> newEntityTargetHistory);

        public abstract Builder setLocationTargetHistory(TargetHistory<Location> newLocationTargetHistory);

        public abstract TargetInfo build();
    }
}
