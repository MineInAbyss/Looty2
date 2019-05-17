package com.derongan.minecraft.looty.skill;


import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;

@AutoValue
public abstract class SkillTrigger {
    public abstract ImmutableSet<InputModifier> getModifiers();

    public abstract Hand getHand();

    public static Builder builder() {
        return new AutoValue_SkillTrigger.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        abstract ImmutableSet.Builder<InputModifier> modifiersBuilder();

        public Builder addModifier(InputModifier modifier) {
            modifiersBuilder().add(modifier);
            return this;
        }

        public abstract Builder setHand(Hand hand);

        public abstract SkillTrigger build();
    }

}
