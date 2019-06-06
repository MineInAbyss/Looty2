package com.derongan.minecraft.looty.skill.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.derongan.minecraft.looty.skill.component.effective.Sound;

import javax.inject.Inject;

public class SoundSystem extends IteratingSystem {
    @Inject
    public SoundSystem() {
        super(Family.all(Sound.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
    }

}
