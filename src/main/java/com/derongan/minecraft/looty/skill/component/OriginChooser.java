package com.derongan.minecraft.looty.skill.component;

import com.derongan.minecraft.looty.skill.component.proto.OriginChooserInfo;

/**
 * Component that controls the targeting location.
 * <p>
 * The targeting location is computed based on the reference point, direction, magnitude, and offset vector.
 */
public class OriginChooser extends Component<OriginChooserInfo> {
    public OriginChooser(OriginChooserInfo info) {
        super(info);
    }
}
