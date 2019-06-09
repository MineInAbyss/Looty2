package com.derongan.minecraft.looty.skill.component;

import com.google.protobuf.Message;

public abstract class Component<E extends Message> implements com.badlogic.ashley.core.Component {
    private E info;

    public Component(E info) {
        this.info = info;
    }

    public E getInfo() {
        return info;
    }

    public void setInfo(E info) {
        this.info = info;
    }
}
