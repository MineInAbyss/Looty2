package com.derongan.minecraft.looty.skill.component.target;

import com.derongan.minecraft.looty.skill.component.Component;
import com.derongan.minecraft.looty.skill.component.proto.RadiusInfo;

public class Radius implements Component<RadiusInfo> {
    private RadiusInfo radiusInfo;

    public Radius(RadiusInfo radiusInfo) {
        this.radiusInfo = radiusInfo;
    }

    @Override
    public RadiusInfo getInfo() {
        return radiusInfo;
    }

    @Override
    public void setInfo(RadiusInfo info) {
        radiusInfo = info;
    }
}
