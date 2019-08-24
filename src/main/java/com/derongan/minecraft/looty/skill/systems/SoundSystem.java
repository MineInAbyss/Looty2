package com.derongan.minecraft.looty.skill.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.derongan.minecraft.looty.skill.component.Sound;
import com.derongan.minecraft.looty.skill.component.proto.SoundInfo;
import org.bukkit.Location;

import javax.inject.Inject;
import java.util.logging.Logger;

public class SoundSystem extends AbstractDelayAwareIteratingSystem {
    private final ComponentMapper<Sound> soundComponentMapper = ComponentMapper.getFor(Sound.class);

    @Inject
    public SoundSystem(Logger logger) {
        super(logger, Family.all(Sound.class).get());
    }

    @Override
    protected void processFilteredEntity(Entity entity, float deltaTime) {
        Sound sound = soundComponentMapper.get(entity);

        Location location;

        SoundInfo info = sound.getInfo();
        switch (info.getLocationReference()) {
            case ORIGIN:
                location = actionAttributesComponentMapper.get(entity).initiatorLocation.getLocation();
                break;
            case TARGET:
            default:
                location = actionAttributesComponentMapper.get(entity).impactLocation.getLocation();
                break;
        }

        org.bukkit.Sound mcSound = org.bukkit.Sound.valueOf(info.getSoundName());
        float volume = info.getVolume();
        float pitch = info.getPitch();

        location.getWorld().playSound(location, mcSound, volume, pitch);
    }
}
