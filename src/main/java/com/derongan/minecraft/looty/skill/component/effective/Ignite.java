package com.derongan.minecraft.looty.skill.component.effective;

import com.derongan.minecraft.looty.skill.component.Component;
import com.derongan.minecraft.looty.skill.component.proto.IgniteInfo;

public class Ignite implements Component<IgniteInfo> {
    private IgniteInfo igniteInfo;

    public Ignite(IgniteInfo igniteInfo) {
        this.igniteInfo = igniteInfo;
    }

    @Override
    public IgniteInfo getInfo() {
        return igniteInfo;
    }

    @Override
    public void setInfo(IgniteInfo info) {
        igniteInfo = info;
    }
}
