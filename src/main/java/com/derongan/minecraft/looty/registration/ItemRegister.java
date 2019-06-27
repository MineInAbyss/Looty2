package com.derongan.minecraft.looty.registration;

import com.badlogic.ashley.core.Component;
import com.derongan.minecraft.looty.skill.SkillWrapper;
import com.derongan.minecraft.looty.skill.proto.ItemType;
import com.derongan.minecraft.looty.skill.proto.Skill;
import com.derongan.minecraft.looty.skill.proto.SkillTrigger;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;

import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Handles registration of looty items.
 */
@Singleton
public class ItemRegister {
    private final Set<ItemType> availableItemTypes;
    private final Map<SkillTrigger, Map<ItemIdentifier, SkillWrapper>> itemSkillMap;
    private final ActionToComponentsConverter actionToComponentsConverter;

    @Inject
    ItemRegister(ActionToComponentsConverter actionToComponentsConverter) {
        this.actionToComponentsConverter = actionToComponentsConverter;
        availableItemTypes = new HashSet<>();
        itemSkillMap = new HashMap<>();
    }

    public Optional<SkillWrapper> getSkill(SkillTrigger skillTrigger, ItemIdentifier itemIdentifier) {
        Map<ItemIdentifier, SkillWrapper> itemTypeSkillMap = itemSkillMap.get(skillTrigger);

        if (itemTypeSkillMap != null)
            return Optional.ofNullable(itemTypeSkillMap.get(itemIdentifier));
        return Optional.empty();
    }

    public void register(ItemType itemType) {
        availableItemTypes.add(itemType);

        for (Skill skill : itemType.getSkillList()) {
            for (SkillTrigger skillTrigger : skill.getTriggerList()) {
                itemSkillMap.computeIfAbsent(skillTrigger, trigger -> new HashMap<>())
                        .put(ItemIdentifier.fromItemType(itemType), getSkillWrapper(skill));
            }
        }
    }

    private SkillWrapper getSkillWrapper(Skill skill) {
        ImmutableList<Set<Component>> actions = skill.getActionList()
                .stream()
                .map(actionToComponentsConverter::convert)
                .collect(toImmutableList());

        return new SkillWrapper(skill, actions);
    }

    public void clear() {
        availableItemTypes.clear();
        itemSkillMap.clear();
    }

    public Collection<ItemType> getAllTypes() {
        return ImmutableSet.copyOf(availableItemTypes);
    }
}
