package com.derongan.minecraft.looty.component.target;

import com.derongan.minecraft.looty.component.Component;
import com.derongan.minecraft.looty.systems.targeting.Target;

import java.util.List;

public class TargetType implements Component {
    private final List<Target> targets;

    private TargetType(List<Target> targets) {
        this.targets = targets;
    }

    public TargetType create(List<Target> targets){
        return new TargetType(targets);
    }
}
