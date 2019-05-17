package com.derongan.minecraft.looty.registration;

import com.derongan.minecraft.looty.Item.ItemType;
import com.derongan.minecraft.looty.skill.Skill;
import com.derongan.minecraft.looty.skill.SkillTrigger;
import com.google.common.collect.ImmutableSet;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;

/**
 * Handles registration of looty items.
 */
@Singleton
public class ItemRegistrar {
    private final Set<ItemType> availableItemTypes;
    //TODO maybe bin by player too
    // also what order makes more sense here? Or combine into single key? Think about this
    private final Map<SkillTrigger, Map<ItemIdentifier, Skill>> itemSkillMap;

    @Inject
    ItemRegistrar() {
        availableItemTypes = new HashSet<>();
        itemSkillMap = new HashMap<>();
    }

    public Optional<Skill> getSkill(SkillTrigger skillTrigger, ItemIdentifier itemIdentifier) {
        Map<ItemIdentifier, Skill> itemTypeSkillMap = itemSkillMap.get(skillTrigger);

        if (itemTypeSkillMap != null)
            return Optional.ofNullable(itemTypeSkillMap.get(itemIdentifier));
        return Optional.empty();
    }

    public void register(ItemType itemType) {
        availableItemTypes.add(itemType);

        itemType.getSkillMap().forEach((skillTrigger, skill) -> itemSkillMap.computeIfAbsent(skillTrigger, t -> new HashMap<>())
                .put(ItemIdentifier.fromItemType(itemType), skill));
    }

    public Collection<ItemType> getAllTypes() {
        return ImmutableSet.copyOf(availableItemTypes);
    }
}
