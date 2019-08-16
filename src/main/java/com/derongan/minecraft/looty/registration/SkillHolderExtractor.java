package com.derongan.minecraft.looty.registration;

import com.derongan.minecraft.looty.item.SkillHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

/**
 * Interface for any class that can convert an {@link org.bukkit.inventory.ItemStack} to a {@link com.derongan.minecraft.looty.item.SkillHolder}
 *
 *
 */
public interface SkillHolderExtractor {
    Optional<SkillHolder> getSkillHolder(ItemStack itemStack);
}
