package com.derongan.minecraft.looty.skill.component.internal;


import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

import java.util.Optional;

@AutoValue
public abstract class TargetHistory<E> {
    public abstract ImmutableList<E> getTargetHistory();

    public Optional<E> getCurrentTarget() {
        if (getTargetHistory().size() > 0) {
            return Optional.ofNullable(getTargetHistory().get(getTargetHistory().size() - 1));
        }
        return Optional.empty();
    }

    public abstract Optional<E> getInitiator();

    public abstract Builder<E> toBuilder();

    public static <E> Builder<E> builder() {
        return new AutoValue_TargetHistory.Builder<>();
    }


    @AutoValue.Builder
    public abstract static class Builder<E> {
        abstract ImmutableList.Builder<E> targetHistoryBuilder();

        public Builder<E> addTarget(E nextTarget) {
            targetHistoryBuilder().add(nextTarget);
            return this;
        }

        public abstract Builder<E> setInitiator(E newOrigin);

        public abstract TargetHistory<E> build();
    }
}
