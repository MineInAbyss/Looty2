package com.derongan.minecraft.looty.item;

import com.derongan.minecraft.looty.skill.proto.ItemType;
import com.google.auto.value.AutoValue;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Used to identify an item as a specific predefined item.
 */
@AutoValue
public abstract class ConfigItemIdentifier {
    public abstract Material getMaterial();

    public abstract int getModel();

    public static Builder builder() {
        return new AutoValue_ConfigItemIdentifier.Builder();
    }


    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setMaterial(Material newMaterial);

        public abstract Builder setModel(int newModel);

        public abstract ConfigItemIdentifier build();
    }

    public static ConfigItemIdentifier fromItemType(ItemType itemType) {
        return ConfigItemIdentifier.builder()
                .setModel(itemType.getModel())
                .setMaterial(Material.valueOf(itemType.getMaterial()))
                .build();
    }

    public static ConfigItemIdentifier fromItemStack(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        int model = meta.getCustomModelData();
        return ConfigItemIdentifier.builder()
                .setModel(model)
                .setMaterial(itemStack.getType())
                .build();
    }
}
