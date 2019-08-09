package com.derongan.minecraft.looty.registration;

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

    public abstract int getDurability();

    public static Builder builder() {
        return new AutoValue_ConfigItemIdentifier.Builder();
    }


    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setMaterial(Material newMaterial);

        public abstract Builder setDurability(int newDurability);

        public abstract ConfigItemIdentifier build();
    }

    public static ConfigItemIdentifier fromItemType(ItemType itemType) {
        return ConfigItemIdentifier.builder()
                .setDurability(itemType.getDurability())
                .setMaterial(Material.valueOf(itemType.getMaterial()))
                .build();
    }

    public static ConfigItemIdentifier fromItemStack(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        short durability = (meta == null) ? 0 : (short) ((Damageable) meta).getDamage();
        return ConfigItemIdentifier.builder()
                .setDurability(durability)
                .setMaterial(itemStack.getType())
                .build();
    }
}
