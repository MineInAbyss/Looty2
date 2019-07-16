package com.derongan.minecraft.looty.registration;

import com.badlogic.ashley.core.Component;
import com.derongan.minecraft.looty.LootyPlugin;
import com.derongan.minecraft.looty.skill.SkillWrapper;
import com.derongan.minecraft.looty.skill.proto.ItemType;
import com.derongan.minecraft.looty.skill.proto.Skill;
import com.derongan.minecraft.looty.skill.proto.SkillTrigger;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.protobuf.InvalidProtocolBufferException;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.ItemTagType;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.nio.ByteBuffer;
import java.util.*;

import static com.derongan.minecraft.looty.ui.LootyEditorFactory.TYPE_KEY;
import static com.google.common.collect.ImmutableList.toImmutableList;


@Singleton
public class SingleItemSkillRegister {
    private final NamespacedKey dirty;
    private Map<UUID, Multimap<SkillTrigger, SkillWrapper>> map;

    private final NamespacedKey uuidKey;
    private final NamespacedKey proto_bytes;
    private NamespacedKey typeKey;
    private ActionToComponentsConverter actionToComponentsConverter;

    @Inject
    public SingleItemSkillRegister(LootyPlugin plugin,
                                   ActionToComponentsConverter actionToComponentsConverter) {
        this.actionToComponentsConverter = actionToComponentsConverter;
        this.map = new HashMap<>();

        dirty = new NamespacedKey(plugin, "dirty");

        typeKey = new NamespacedKey(plugin, TYPE_KEY);
        uuidKey = new NamespacedKey(plugin, "uuid");
        proto_bytes = new NamespacedKey(plugin, "proto_bytes");
    }

    public Collection<SkillWrapper> getSkill(SkillTrigger skillTrigger, ItemStack itemStack) {
        if (itemStack.hasItemMeta()) {
            byte[] uuidBytes = itemStack.getItemMeta()
                    .getCustomTagContainer()
                    .getCustomTag(uuidKey, ItemTagType.BYTE_ARRAY);

            if (uuidBytes == null) {
                return ImmutableList.of();
            }

            UUID uuid = getUUIDFromBytes(uuidBytes);

            byte isDirty = itemStack.getItemMeta().getCustomTagContainer().getCustomTag(dirty, ItemTagType.BYTE);

            if (isDirty == 1) {
                map.compute(uuid, (id, old) -> compute(itemStack)).get(skillTrigger);

                ItemMeta itemMeta = itemStack.getItemMeta();

                itemMeta.getCustomTagContainer().setCustomTag(dirty, ItemTagType.BYTE, (byte) 0);

                itemStack.setItemMeta(itemMeta);
            }

            return map.computeIfAbsent(uuid, id -> compute(itemStack)).get(skillTrigger);
        }

        return ImmutableList.of();
    }

    private Multimap<SkillTrigger, SkillWrapper> compute(ItemStack itemStack) {
        byte[] protoBytes = itemStack.getItemMeta()
                .getCustomTagContainer()
                .getCustomTag(proto_bytes, ItemTagType.BYTE_ARRAY);

        ItemType.Builder builder = ItemType.newBuilder();

        try {
            builder.mergeFrom(protoBytes);


            List<SkillWrapper> wrappers = builder.getSkillList()
                    .stream()
                    .map(this::getSkillWrapper)
                    .collect(toImmutableList());

            ImmutableMultimap.Builder<SkillTrigger, SkillWrapper> multimapBuilder = ImmutableMultimap.builder();

            for (SkillWrapper wrapper : wrappers) {
                wrapper.getSkill().getTriggerList().forEach(trigger -> multimapBuilder.put(trigger, wrapper));
            }

            return multimapBuilder.build();

        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }

    private SkillWrapper getSkillWrapper(Skill skill) {
        ImmutableList<Set<Component>> actions = skill.getActionList()
                .stream()
                .map(actionToComponentsConverter::convert)
                .collect(toImmutableList());

        return new SkillWrapper(skill, actions);
    }

    public static UUID getUUIDFromBytes(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        Long high = byteBuffer.getLong();
        Long low = byteBuffer.getLong();

        return new UUID(high, low);
    }
}
