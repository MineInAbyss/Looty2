package com.derongan.minecraft.looty.item;

import com.badlogic.ashley.core.Component;
import com.derongan.minecraft.looty.registration.ActionToComponentsMapper;
import com.derongan.minecraft.looty.skill.SkillWrapper;
import com.derongan.minecraft.looty.skill.proto.ItemType;
import com.derongan.minecraft.looty.skill.proto.Skill;
import com.google.common.collect.ImmutableList;

import javax.inject.Inject;
import java.util.Set;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableSet.toImmutableSet;


public class ConfigItemTypeWrapperFactory {
    private ActionToComponentsMapper actionToComponentsMapper;

    @Inject
    public ConfigItemTypeWrapperFactory(ActionToComponentsMapper actionToComponentsMapper) {
        this.actionToComponentsMapper = actionToComponentsMapper;
    }

    public ConfigItemTypeWrapper createConfigItemTypeWrapper(ItemType itemType) {
        return new ConfigItemTypeWrapper(itemType, itemType.getSkillList()
                .stream()
                .map(this::getSkillWrapper)
                .collect(toImmutableSet()));
    }

    private SkillWrapper getSkillWrapper(Skill skill) {
        ImmutableList<Set<Component>> actions = skill.getActionList()
                .stream()
                .map(actionToComponentsMapper::convert)
                .collect(toImmutableList());

        return new SkillWrapper(skill, actions);
    }

    public static class ConfigItemTypeWrapper implements SkillHolder {
        private ItemType itemType;
        private Set<SkillWrapper> skills;

        private ConfigItemTypeWrapper(ItemType itemType, Set<SkillWrapper> skills) {
            this.itemType = itemType;
            this.skills = skills;
        }

        public ItemType getItemType() {
            return itemType;
        }

        @Override
        public Set<SkillWrapper> getSkills() {
            return skills;
        }
    }
}
