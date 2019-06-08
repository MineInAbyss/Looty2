package com.derongan.minecraft.looty.skill.component.target;

import com.derongan.minecraft.looty.skill.component.Component;
import com.derongan.minecraft.looty.skill.component.proto.OffsetInfo;

/**
 * Component that controls the targeting location.
 * <p>
 * The targeting location is computed based on the reference point, direction, magnitude, and offset vector.
 */
public class OriginChooser implements Component<OffsetInfo> {
    private OffsetInfo offsetInfo;

    public OriginChooser(OffsetInfo offsetInfo) {
        this.offsetInfo = offsetInfo;
    }

    @Override
    public OffsetInfo getInfo() {
        return offsetInfo;
    }

    @Override
    public void setInfo(OffsetInfo info) {
        this.offsetInfo = info;
    }
}
