package com.derongan.minecraft.looty.skill.component.effective;

import com.derongan.minecraft.looty.skill.component.Component;

public class VelocityImparting implements Component {
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
