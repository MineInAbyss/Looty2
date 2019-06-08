package com.derongan.minecraft.looty.skill.component.target;

import com.derongan.minecraft.looty.skill.component.Component;
import com.derongan.minecraft.looty.skill.component.proto.DurationInfo;

public class Linger implements Component<DurationInfo> {
    private DurationInfo durationInfo;

    public Linger(DurationInfo durationInfo) {
        this.durationInfo = durationInfo;
    }

    @Override
    public DurationInfo getInfo() {
        return durationInfo;
    }

    @Override
    public void setInfo(DurationInfo info) {
        this.durationInfo = info;
    }
}
