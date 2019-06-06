package com.derongan.minecraft.looty.Item;

import com.derongan.minecraft.looty.skill.Skill;
import com.derongan.minecraft.looty.skill.SkillTrigger;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableMap;
import org.bukkit.Material;

import java.util.List;
import java.util.Optional;

@AutoValue
public abstract class ItemType {
    public abstract short getDurability();

    public abstract Material getMaterial();

    public abstract String getName();

    public abstract Optional<List<String>> getLore();

    public abstract Optional<ItemRarity> getItemRarity();

    public abstract ImmutableMap<SkillTrigger, Skill> getSkillMap();

    public static Builder builder() {
        return new AutoValue_ItemType.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setDurability(short newDurability);

        public abstract Builder setMaterial(Material newMaterial);

        public abstract Builder setName(String newName);

        public abstract Builder setLore(List<String> newLore);

        public abstract Builder setItemRarity(ItemRarity newItemRarity);

        public abstract ItemType build();

        abstract ImmutableMap.Builder<SkillTrigger, Skill> skillMapBuilder();

        public Builder addSkillWithTrigger(SkillTrigger skillTrigger, Skill skill) {
            skillMapBuilder().put(skillTrigger, skill);
            return this;
        }
    }
}
