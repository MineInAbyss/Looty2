package com.derongan.minecraft.looty.registration;

import com.derongan.minecraft.looty.skill.proto.ItemType;
import com.google.auto.value.AutoValue;
import org.bukkit.Material;

@AutoValue
public abstract class ItemIdentifier {
    public abstract Material getMaterial();

    public abstract int getDurability();

    public static Builder builder() {
        return new AutoValue_ItemIdentifier.Builder();
    }


    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setMaterial(Material newMaterial);

        public abstract Builder setDurability(int newDurability);

        public abstract ItemIdentifier build();
    }

    public static ItemIdentifier fromItemType(ItemType itemType) {
        return ItemIdentifier.builder()
                .setDurability(itemType.getDurability())
                .setMaterial(Material.valueOf(itemType.getMaterial()))
                .build();
    }
}
