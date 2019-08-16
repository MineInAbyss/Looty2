package com.derongan.minecraft.looty.registration;

import com.derongan.minecraft.looty.item.SkillHolder;
import org.bukkit.inventory.ItemStack;

import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;

/**
 * Register that pulls from both NBT and Config registered items
 */
public class CompoundSkillHolderExtractor implements SkillHolderExtractor {
    private Set<SkillHolderExtractor> skillHolderExtractorSet;

    @Inject
    public CompoundSkillHolderExtractor(Set<SkillHolderExtractor> skillHolderExtractorSet) {
        this.skillHolderExtractorSet = skillHolderExtractorSet;
    }

    @Override
    public Optional<SkillHolder> getSkillHolder(ItemStack itemStack) {
        for (SkillHolderExtractor skillHolderExtractor : skillHolderExtractorSet) {
            Optional<SkillHolder> skillHolder = skillHolderExtractor.getSkillHolder(itemStack);
            if (skillHolder.isPresent()) {
                return skillHolder;
            }
        }

        return Optional.empty();
    }
}
