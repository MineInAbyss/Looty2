package com.derongan.minecraft.looty.skill.component.target;

import com.derongan.minecraft.looty.skill.component.Component;
import com.derongan.minecraft.looty.skill.component.proto.MovementInfo;

public class Movement implements Component<MovementInfo> {
    private MovementInfo movementInfo;

    public Movement(MovementInfo movementInfo) {
        this.movementInfo = movementInfo;
    }

    @Override
    public MovementInfo getInfo() {
        return movementInfo;
    }

    @Override
    public void setInfo(MovementInfo info) {
        movementInfo = info;
    }
}
