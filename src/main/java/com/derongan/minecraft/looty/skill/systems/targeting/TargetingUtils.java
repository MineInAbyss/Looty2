package com.derongan.minecraft.looty.skill.systems.targeting;

import com.derongan.minecraft.looty.location.MovingDynamicLocation;
import com.derongan.minecraft.looty.skill.component.components.Targets;

public class TargetingUtils {
    public static MovingDynamicLocation getTargetOrThrow(Targets targets, String target) {
        //TODO scope to skill
        return targets.getTarget(target).orElseThrow(() -> new RuntimeException("%s is not a valid target"));
    }
}
