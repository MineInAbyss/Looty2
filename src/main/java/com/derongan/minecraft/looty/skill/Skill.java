package com.derongan.minecraft.looty.skill;


import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;

@AutoValue
public abstract class Skill {
    public abstract ImmutableSet<ActionEntityBuilder> getActionEntityBuilders();

    public static Builder builder() {
        return new AutoValue_Skill.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        protected abstract ImmutableSet.Builder<ActionEntityBuilder> actionEntityBuildersBuilder();

        public Builder addActionBuilder(ActionEntityBuilder actionEntityBuilder) {
            actionEntityBuildersBuilder().add(actionEntityBuilder);
            return this;
        }

        public abstract Skill build();
    }
}
