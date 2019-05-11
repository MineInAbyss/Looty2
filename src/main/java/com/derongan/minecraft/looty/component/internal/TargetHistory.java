package com.derongan.minecraft.looty.component.internal;


import com.google.auto.value.AutoValue;

import java.util.Optional;

@AutoValue
public abstract class TargetHistory<E> {
    public abstract Optional<E> getCurrentTarget();
    public abstract Optional<E> getInitialTargetOrSource();
    public abstract Optional<E> getLastTargetOrSource();

    public static <E> Builder<E> builder() {
        return new AutoValue_TargetHistory.Builder<>();
    }


    @AutoValue.Builder
    public abstract static class Builder<E> {
        public abstract Builder<E> setCurrentTarget(E newCurrentTarget);

        public abstract Builder<E> setInitialTargetOrSource(E newInitialTargetOrSource);

        public abstract Builder<E> setLastTargetOrSource(E newLastTargetOrSource);

        public abstract TargetHistory<E> build();
    }
}
