package com.derongan.minecraft.looty.skill.component.target;

import com.derongan.minecraft.looty.skill.component.Component;
import com.google.protobuf.Empty;

/**
 * Forces reference points to be on the ground
 */
public class Grounded implements Component<Empty> {
    @Override
    public Empty getInfo() {
        return Empty.getDefaultInstance();
    }

    @Override
    public void setInfo(Empty info) {
    }
}
