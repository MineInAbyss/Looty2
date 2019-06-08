package com.derongan.minecraft.looty.skill.component;

import com.google.protobuf.MessageLite;

public interface Component<E extends MessageLite> extends com.badlogic.ashley.core.Component {
    E getInfo();

    void setInfo(E info);
}
