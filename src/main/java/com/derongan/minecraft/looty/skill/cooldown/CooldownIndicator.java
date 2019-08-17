package com.derongan.minecraft.looty.skill.cooldown;

import com.derongan.minecraft.looty.skill.proto.Skill;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * Responsible for displaying cooldowns to the player.
 */
public interface CooldownIndicator {
    void show(Player player);
    void remove();
    void update(Map<Skill, Integer> ticksRemainingBySkill);
}
