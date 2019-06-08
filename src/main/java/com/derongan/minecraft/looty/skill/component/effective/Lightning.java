package com.derongan.minecraft.looty.skill.component.effective;

import com.derongan.minecraft.looty.skill.component.Component;
import com.google.protobuf.Empty;

public class Lightning implements Component<Empty> {
    @Override
    public Empty getInfo() {
        return Empty.getDefaultInstance();
    }

    @Override
    public void setInfo(Empty info) {

    }
}
