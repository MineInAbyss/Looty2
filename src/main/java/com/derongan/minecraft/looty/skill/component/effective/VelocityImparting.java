package com.derongan.minecraft.looty.skill.component.effective;

public class VelocityImparting implements com.badlogic.ashley.core.Component {
    public enum Reference {
        INITIATOR,
        TARGET,
        NEAREST_ORIGIN,
        SKY,
        GROUND,
        AWAY
    }

    public VelocityImparting() {
    }
}
