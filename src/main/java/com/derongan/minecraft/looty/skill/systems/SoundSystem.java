package com.derongan.minecraft.looty.skill.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.derongan.minecraft.looty.skill.component.effective.Sound;
import com.derongan.minecraft.looty.skill.component.internal.Origins;
import com.derongan.minecraft.looty.skill.component.internal.TargetInfo;
import com.derongan.minecraft.looty.skill.component.internal.Targets;

import javax.inject.Inject;

public class SoundSystem extends IteratingSystem {
    private ComponentMapper<Sound> soundComponentMapper = ComponentMapper.getFor(Sound.class);
    private ComponentMapper originsComponentMapper = ComponentMapper.getFor(Origins.ORIGINS_CLASS);
    private ComponentMapper targetInfoComponentMapper = ComponentMapper.getFor(TargetInfo.TARGET_INFO_CLASS);
    private ComponentMapper<Targets> targetsComponentMapper = ComponentMapper.getFor(Targets.class);

    @Inject
    public SoundSystem() {
        super(Family.all(Sound.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Sound sound = soundComponentMapper.get(entity);
        Origins origins = (Origins) originsComponentMapper.get(entity);
        Targets targets = targetsComponentMapper.get(entity);


        switch (sound.getSoundLocation()) {
            case TARGET:
                targets.getTargets()
                        .forEach(t -> t.getWorld().playSound(t.getLocation(), sound.getSound(), sound.getVolume(), 1));
                break;
            case ORIGIN:
                origins.getTargetOrigins()
                        .forEach(t -> t.getLocation()
                                .getWorld()
                                .playSound(t.getLocation(), sound.getSound(), sound.getVolume(), 1));
                break;
        }
    }

}
