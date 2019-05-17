package com.derongan.minecraft.looty.registration;

import com.derongan.minecraft.looty.Item.ItemType;
import com.google.auto.value.AutoValue;
import org.bukkit.Material;

@AutoValue
public abstract class ItemIdentifier {
    public abstract Material getMaterial();

    public abstract Short getDurability();

    public static Builder builder() {
        return new AutoValue_ItemIdentifier.Builder();
    }


    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setMaterial(Material newMaterial);

        public abstract Builder setDurability(Short newDurability);

        public abstract ItemIdentifier build();
    }

    public static ItemIdentifier fromItemType(ItemType itemType) {
        return ItemIdentifier.builder()
                .setDurability(itemType.getDurability())
                .setMaterial(itemType.getMaterial())
                .build();
    }
}
