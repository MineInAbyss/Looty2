package com.derongan.minecraft.looty.skill.component;

import com.derongan.minecraft.looty.skill.component.proto.TargetChooserInfo;

/**
 * Component that controls the targeting location.
 * <p>
 * The targeting location is computed based on the reference point, direction, magnitude, and offset vector.
 */
public class TargetChooser extends Component<TargetChooserInfo> {
    public TargetChooser(TargetChooserInfo info) {
        super(info);
    }
}
