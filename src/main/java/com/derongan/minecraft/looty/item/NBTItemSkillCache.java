package com.derongan.minecraft.looty.item;

import com.badlogic.ashley.core.Component;
import com.derongan.minecraft.looty.LootyPlugin;
import com.derongan.minecraft.looty.skill.SkillWrapper;
import com.derongan.minecraft.looty.skill.component.ActionToComponentsMapper;
import com.derongan.minecraft.looty.skill.proto.ItemType;
import com.derongan.minecraft.looty.skill.proto.Skill;
import com.google.common.collect.ImmutableList;
import com.google.protobuf.InvalidProtocolBufferException;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.ItemTagType;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.nio.ByteBuffer;
import java.util.*;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableSet.toImmutableSet;


/**
 * Caches actions for existing NBT based items
 */
@Singleton
public class NBTItemSkillCache implements SkillHolderExtractor {
    private final NamespacedKey dirty;
    private Map<UUID, SkillHolder> map;

    private final NamespacedKey uuidKey;
    private final NamespacedKey proto_bytes;
    private ActionToComponentsMapper actionToComponentsMapper;

    @Inject
    NBTItemSkillCache(LootyPlugin plugin,
                      ActionToComponentsMapper actionToComponentsMapper) {
        this.actionToComponentsMapper = actionToComponentsMapper;
        this.map = new HashMap<>();

        dirty = new NamespacedKey(plugin, "dirty");

        uuidKey = new NamespacedKey(plugin, "uuid");
        proto_bytes = new NamespacedKey(plugin, "proto_bytes");
    }

    public Optional<SkillHolder> getSkillHolder(ItemStack itemStack) {
        if (itemStack.hasItemMeta()) {
            byte[] uuidBytes = itemStack.getItemMeta()
                    .getCustomTagContainer()
                    .getCustomTag(uuidKey, ItemTagType.BYTE_ARRAY);

            if (uuidBytes == null) {
                return Optional.empty();
            }

            UUID uuid = getUUIDFromBytes(uuidBytes);

            byte isDirty = itemStack.getItemMeta().getCustomTagContainer().getCustomTag(dirty, ItemTagType.BYTE);

            if (isDirty == 1) {
                map.compute(uuid, (id, old) -> compute(itemStack));

                ItemMeta itemMeta = itemStack.getItemMeta();

                itemMeta.getCustomTagContainer().setCustomTag(dirty, ItemTagType.BYTE, (byte) 0);

                itemStack.setItemMeta(itemMeta);
            }

            return Optional.of(map.computeIfAbsent(uuid, id -> compute(itemStack)));
        }

        return Optional.empty();
    }

    private SkillHolder compute(ItemStack itemStack) {
        byte[] protoBytes = itemStack.getItemMeta()
                .getCustomTagContainer()
                .getCustomTag(proto_bytes, ItemTagType.BYTE_ARRAY);

        ItemType.Builder builder = ItemType.newBuilder();

        try {
            builder.mergeFrom(protoBytes);


            Set<SkillWrapper> wrappers = builder.getSkillList()
                    .stream()
                    .map(this::getSkillWrapper)
                    .collect(toImmutableSet());

            return () -> wrappers;

        } catch (InvalidProtocolBufferException e) {
            throw new InvalidSkillNBTException(e);
        }
    }

    private SkillWrapper getSkillWrapper(Skill skill) {
        ImmutableList<Set<Component>> actions = skill.getActionList()
                .stream()
                .map(actionToComponentsMapper::convert)
                .collect(toImmutableList());

        return new SkillWrapper(skill, actions);
    }

    private static UUID getUUIDFromBytes(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        Long high = byteBuffer.getLong();
        Long low = byteBuffer.getLong();

        return new UUID(high, low);
    }

    public static class InvalidSkillNBTException extends RuntimeException{
        InvalidSkillNBTException(Throwable throwable) {
            super(throwable);
        }
    }
}
