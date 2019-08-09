package com.derongan.minecraft.looty.item;

import com.derongan.minecraft.looty.registration.ConfigItemIdentifier;
import com.derongan.minecraft.looty.registration.ConfigItemRegister;
import org.bukkit.inventory.ItemStack;

import javax.inject.Inject;

public class LootyItemDetector {
    private final ConfigItemRegister configItemRegister;

    @Inject
    public LootyItemDetector(ConfigItemRegister configItemRegister) {
        this.configItemRegister = configItemRegister;
    }

    public boolean isLootyItem(ItemStack itemStack) {
        return configItemRegister.getConfigItemType(ConfigItemIdentifier.fromItemStack(itemStack)).isPresent();
    }
//
//    public boolean isMetaBasedLootyItem(ItemStack itemStack) {
//        return itemStack.hasItemMeta() && itemStack.getItemMeta()
//                .getCustomTagContainer()
//                .getCustomTag(uuidKey, ItemTagType.BYTE_ARRAY);
//    }
}
