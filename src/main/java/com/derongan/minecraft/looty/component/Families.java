package com.derongan.minecraft.looty.component;

import com.badlogic.ashley.core.Family;
import com.derongan.minecraft.looty.component.target.Beam;
import com.derongan.minecraft.looty.component.target.Filter;
import com.derongan.minecraft.looty.component.target.Points;
import com.derongan.minecraft.looty.component.target.Radius;

public class Families {
    public static final Family TARGETING_FAMILY = Family.one(Radius.class,
            Points.class,
            Beam.class,
            Filter.class
    ).get();
}
