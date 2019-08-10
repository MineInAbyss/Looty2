package com.derongan.minecraft.looty.item;

import com.derongan.minecraft.looty.registration.ConfigItemIdentifier;
import com.derongan.minecraft.looty.registration.ConfigItemRegister;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.tags.ItemTagType;
import org.bukkit.plugin.Plugin;

import javax.inject.Inject;

public class LootyItemDetector {
    private final ConfigItemRegister configItemRegister;
    private final NamespacedKey uuidKey;

    @Inject
    public LootyItemDetector(ConfigItemRegister configItemRegister, Plugin plugin) {
        this.configItemRegister = configItemRegister;

        uuidKey = new NamespacedKey(plugin, "uuid");
    }

    public boolean isLootyItem(ItemStack itemStack) {
        return isConfigBasedLootyItem(itemStack) || isNBTBasedLootyItem(itemStack);
    }

    public boolean isConfigBasedLootyItem(ItemStack itemStack) {
        return configItemRegister.getConfigItemType(ConfigItemIdentifier.fromItemStack(itemStack))
                .isPresent();
    }

    public boolean isNBTBasedLootyItem(ItemStack itemStack) {
        return itemStack.hasItemMeta() && itemStack.getItemMeta()
                .getCustomTagContainer()
                .hasCustomTag(uuidKey, ItemTagType.BYTE_ARRAY);
    }
}
