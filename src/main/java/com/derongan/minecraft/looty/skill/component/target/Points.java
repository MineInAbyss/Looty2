package com.derongan.minecraft.looty.skill.component.target;

import com.derongan.minecraft.looty.skill.component.Component;
import org.bukkit.util.Vector;

import java.util.List;

public class Points implements Component {
    private final List<Vector> offsets;

    private Points(List<Vector> offsets) {
        this.offsets = offsets;
    }

    public List<Vector> getOffsets() {
        return offsets;
    }

    public Points create(List<Vector> points) {
        return new Points(points);
    }
}
