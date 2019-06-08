package com.derongan.minecraft.looty.skill.component;

import com.derongan.minecraft.looty.skill.component.proto.DurationInfo;

public class Delay implements Component<DurationInfo> {
    private DurationInfo durationInfo;

    public Delay(DurationInfo durationInfo) {
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
