package com.derongan.minecraft.looty.registration;

import com.derongan.minecraft.looty.item.ConfigItemTypeWrapperFactory;
import com.derongan.minecraft.looty.item.SkillHolder;
import com.derongan.minecraft.looty.skill.proto.ItemType;

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
public class ConfigItemRegister {
    private final Map<ConfigItemIdentifier, ConfigItemTypeWrapperFactory.ConfigItemTypeWrapper> identifierToConfigItemMap;
    private final ConfigItemTypeWrapperFactory configItemTypeWrapperFactory;

    @Inject
    ConfigItemRegister(ConfigItemTypeWrapperFactory configItemTypeWrapperFactory) {
        this.configItemTypeWrapperFactory = configItemTypeWrapperFactory;
        identifierToConfigItemMap = new HashMap<>();
    }

    public Optional<SkillHolder> getConfigItemType(ConfigItemIdentifier configItemIdentifier) {
        return Optional.ofNullable(identifierToConfigItemMap.get(configItemIdentifier));
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