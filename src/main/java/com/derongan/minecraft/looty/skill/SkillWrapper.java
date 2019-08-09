package com.derongan.minecraft.looty.skill;

import com.badlogic.ashley.core.Component;
import com.derongan.minecraft.looty.skill.proto.Skill;
import com.derongan.minecraft.looty.skill.proto.SkillTrigger;
import com.google.common.collect.ImmutableSet;

import java.util.List;
import java.util.Set;

import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Wraps a skill proto and provides a convenient way of getting all of its actions as real component lists
 */
public class SkillWrapper {
    private final Skill skill;
    private List<Set<Component>> actions;

    public SkillWrapper(Skill skill, List<Set<Component>> actions) {
        this.skill = skill;
        this.actions = actions.stream().map(ImmutableSet::copyOf).collect(toImmutableList());
    }

    public Skill getSkill() {
        return skill;
    }

    public Iterable<Set<Component>> getActions() {
        return actions;
    }

    public List<SkillTrigger> getSkillTriggers() {
        return skill.getTriggerList();
    }
}
