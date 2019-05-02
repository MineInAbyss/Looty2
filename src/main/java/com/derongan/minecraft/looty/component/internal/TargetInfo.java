package com.derongan.minecraft.looty.component.internal;

import com.derongan.minecraft.looty.component.Component;
import org.bukkit.entity.Entity;

import java.util.Optional;

/**
 * A special component that is created by the trigger system
 */
public class TargetInfo implements Component {
    private Entity initiator;
    private Entity originalInitiator;
    private Entity target;

    private TargetInfo() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public Optional<Entity> getInitiator() {
        return Optional.ofNullable(initiator);
    }

    public void setInitiator(Entity initiator) {
        this.initiator = initiator;
    }


    public Optional<Entity> getOriginalInitiator() {
        return Optional.ofNullable(originalInitiator);
    }

    public void setOriginalInitiator(Entity originalInitiator) {
        this.originalInitiator = originalInitiator;
    }


    public Optional<Entity> getTarget() {
        return Optional.ofNullable(target);
    }

    public void setTarget(Entity target) {
        this.target = target;
    }

    public static class Builder {
        private Entity initiator;
        private Entity originalInitiator;
        private Entity target;

        public Builder setInitiator(Entity initiator) {
            this.initiator = initiator;
            return this;
        }

        public Builder setOriginalInitiator(Entity originalInitiator) {
            this.originalInitiator = originalInitiator;
            return this;
        }

        public Builder setTarget(Entity target) {
            this.target = target;
            return this;
        }

        public TargetInfo build() {
            TargetInfo targetInfo = new TargetInfo();

            targetInfo.initiator = initiator;
            targetInfo.originalInitiator = originalInitiator;
            targetInfo.target = target;

            return targetInfo;
        }
    }
}
