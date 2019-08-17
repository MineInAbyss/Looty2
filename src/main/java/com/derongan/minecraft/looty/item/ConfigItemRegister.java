package com.derongan.minecraft.looty.item;

import com.derongan.minecraft.looty.skill.proto.ItemType;
import org.bukkit.inventory.ItemStack;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Handles registration of configuration based items.
 */
@Singleton
public class ConfigItemRegister implements SkillHolderExtractor {
    private final Map<ConfigItemIdentifier, ConfigItemTypeWrapperFactory.ConfigItemTypeWrapper> identifierToConfigItemMap;
    private final ConfigItemTypeWrapperFactory configItemTypeWrapperFactory;

    @Inject
    ConfigItemRegister(ConfigItemTypeWrapperFactory configItemTypeWrapperFactory) {
        this.configItemTypeWrapperFactory = configItemTypeWrapperFactory;
        identifierToConfigItemMap = new HashMap<>();
    }

    @Override
    public Optional<SkillHolder> getSkillHolder(ItemStack itemStack) {
        return Optional.ofNullable(identifierToConfigItemMap.get(ConfigItemIdentifier.fromItemStack(itemStack)));
    }

    public void register(ItemType itemType) {
        identifierToConfigItemMap.put(ConfigItemIdentifier.fromItemType(itemType), configItemTypeWrapperFactory.createConfigItemTypeWrapper(itemType));
    }

    public void clear() {
        identifierToConfigItemMap.clear();
    }

    public Collection<ItemType> getAllTypes() {
        return identifierToConfigItemMap.values()
                .stream()
                .map(ConfigItemTypeWrapperFactory.ConfigItemTypeWrapper::getItemType)
                .collect(toImmutableSet());
    }
}
