package com.derongan.minecraft.looty.skill.component;

import com.badlogic.ashley.core.Family;
import com.derongan.minecraft.looty.skill.component.target.Linger;
import com.derongan.minecraft.looty.skill.component.target.Movement;

public class Families {
    public static final Family ALL_ENTITIES = Family.exclude().get();

    public static final Family REMOVABLE = Family.exclude(Linger.class, Movement.class).get();
}
