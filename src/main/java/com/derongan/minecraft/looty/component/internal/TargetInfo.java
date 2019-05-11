package com.derongan.minecraft.looty.component.internal;

import com.derongan.minecraft.looty.component.Component;
import com.google.auto.value.AutoValue;
import org.bukkit.entity.Entity;

/**
 * A special component that is created by the trigger system
 */
@AutoValue
public abstract class TargetInfo implements Component {
    public static final Class<AutoValue_TargetInfo> TARGET_INFO_CLASS = AutoValue_TargetInfo.class;

    public abstract TargetHistory<Entity> getEntityTargetHistory();

    public static Builder builder() {
        return new AutoValue_TargetInfo.Builder();
    }


    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setEntityTargetHistory(TargetHistory<Entity> newEntityTargetHistory);

        public abstract TargetInfo build();
    }
}
