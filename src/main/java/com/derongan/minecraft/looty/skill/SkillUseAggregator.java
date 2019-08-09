package com.derongan.minecraft.looty.skill;

import com.derongan.minecraft.looty.skill.proto.SkillTrigger;
import com.google.common.collect.ImmutableMap;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Aggregates skill use in a single tick into discreet skill use events.
 * <p>
 * For example in survival mode {@link org.bukkit.event.player.PlayerAnimationEvent} may fire multiple times
 * in one tick. Similarly {@link org.bukkit.event.player.PlayerInteractEvent} may fire
 * alongside {@link org.bukkit.event.player.PlayerInteractEvent}. In both these cases the player truly only performed
 * one action, so only the appropriate skills for that action should fire.
 */
@Singleton
public class SkillUseAggregator {
    private Map<UUID, SkillTrigger.Trigger> eventsTakenThisTick;

    @Inject
    public SkillUseAggregator() {
        eventsTakenThisTick = new HashMap<>();
    }

    public void addEvent(Player player, SkillTrigger.Trigger triggerHint) {
        eventsTakenThisTick.compute(player.getUniqueId(), (uuid, oldHint) -> {
            if (oldHint == SkillTrigger.Trigger.RIGHT_CLICK || triggerHint == SkillTrigger.Trigger.RIGHT_CLICK) {
                return SkillTrigger.Trigger.RIGHT_CLICK;
            } else {
                return SkillTrigger.Trigger.LEFT_CLICK;
            }
        });
    }

    public Map<UUID, SkillTrigger.Trigger> getEventsTakenThisTick() {
        return ImmutableMap.copyOf(eventsTakenThisTick);
    }

    public void reset() {
        eventsTakenThisTick.clear();
    }
}
