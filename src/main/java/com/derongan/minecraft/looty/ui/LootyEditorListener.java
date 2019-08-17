package com.derongan.minecraft.looty.ui;

import com.derongan.minecraft.guiy.gui.GuiHolder;
import com.derongan.minecraft.guiy.gui.inputs.MessageFormater;
import com.derongan.minecraft.looty.LootyPlugin;
import com.derongan.minecraft.looty.skill.component.ComponentRegister;
import com.derongan.minecraft.looty.skill.proto.Action;
import com.derongan.minecraft.looty.skill.proto.Action.Builder;
import com.derongan.minecraft.looty.skill.proto.ItemType;
import com.derongan.minecraft.looty.skill.proto.Skill;
import com.derongan.minecraft.looty.skill.proto.SkillTrigger;
import com.derongan.minecraft.looty.ui.elements.ComponentEditorView;
import com.derongan.minecraft.looty.ui.elements.TriggerEditorView;
import com.google.common.collect.ImmutableList;
import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.ItemTagType;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.*;

import static com.derongan.minecraft.guiy.GuiyKeys.TYPE_KEY;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK;

@Singleton
public class LootyEditorListener implements Listener {
    private final LootyPlugin plugin;
    private final ComponentRegister componentRegister;
    private final NamespacedKey uuidKey;
    private final NamespacedKey proto_bytes;
    private final NamespacedKey typeKey;
    private final NamespacedKey dirty;

    @Inject
    public LootyEditorListener(LootyPlugin plugin, ComponentRegister componentRegister) {
        this.plugin = plugin;
        this.componentRegister = componentRegister;
        typeKey = new NamespacedKey(plugin, TYPE_KEY);
        uuidKey = new NamespacedKey(plugin, "uuid");
        proto_bytes = new NamespacedKey(plugin, "proto_bytes");
        dirty = new NamespacedKey(plugin, "dirty");
    }


    private static final UUID ORANGE_THING_UUID = UUID.fromString("c68424f8-db02-452e-b55a-2cfad4acdff3");
    private static final UUID DEVICE_UUID = UUID.fromString("6ce84ae3-53e0-43f1-99df-6f680850a43e");
    private static final UUID CAULDRON_UUID = UUID.fromString("99b37c01-949d-4c97-a17f-cbef633deb8a");


    private Set<UUID> skip = new HashSet<>();

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent inventoryClickEvent) {
        if (skip.contains(inventoryClickEvent.getWhoClicked().getUniqueId())) {
            return;
        }

        ItemStack placed = inventoryClickEvent.getCursor();
        ItemStack consumer = inventoryClickEvent.getCurrentItem();

        if (placed == null || consumer == null) {
            return;
        }

        if (!placed.hasItemMeta() || !consumer.hasItemMeta()) {
            return;
        }

        ItemMeta placedMeta = placed.getItemMeta();
        ItemMeta consumerMeta = consumer.getItemMeta();

        String placedType = placedMeta.getCustomTagContainer().getCustomTag(typeKey, ItemTagType.STRING);
        String consumerType = consumerMeta.getCustomTagContainer().getCustomTag(typeKey, ItemTagType.STRING);

        if (placedType == null || consumerType == null) {
            return;
        }

        if (placedType.equals("component") && consumerType.equals("action")) {
            consumeComponent(inventoryClickEvent, placed, consumer, placedMeta, consumerMeta);
            skip.add(inventoryClickEvent.getWhoClicked().getUniqueId());
            Bukkit.getScheduler()
                    .scheduleSyncDelayedTask(plugin, () -> skip.remove(inventoryClickEvent.getWhoClicked()
                            .getUniqueId()), 5);
        } else if (placedType.equals("action") && consumerType.equals("skill")) {
            consumeAction(inventoryClickEvent, placed, consumer, placedMeta, consumerMeta);
            skip.add(inventoryClickEvent.getWhoClicked().getUniqueId());
            Bukkit.getScheduler()
                    .scheduleSyncDelayedTask(plugin, () -> skip.remove(inventoryClickEvent.getWhoClicked()
                            .getUniqueId()), 5);
        } else if (placedType.equals("skill") && consumerType.equals("looty")) {
            consumeSkill(inventoryClickEvent, placed, consumer, placedMeta, consumerMeta);
            Bukkit.getScheduler()
                    .scheduleSyncDelayedTask(plugin, () -> skip.remove(inventoryClickEvent.getWhoClicked()
                            .getUniqueId()), 5);
            skip.add(inventoryClickEvent.getWhoClicked().getUniqueId());
        } else if (placedType.equals("trigger") && consumerType.equals("skill")) {
            consumeTrigger(inventoryClickEvent, placed, consumer, placedMeta, consumerMeta);
            Bukkit.getScheduler()
                    .scheduleSyncDelayedTask(plugin, () -> skip.remove(inventoryClickEvent.getWhoClicked()
                            .getUniqueId()), 5);
            skip.add(inventoryClickEvent.getWhoClicked().getUniqueId());
        }
    }


    private void consumeTrigger(InventoryClickEvent inventoryClickEvent,
                                ItemStack placed,
                                ItemStack consumer,
                                ItemMeta placedMeta, ItemMeta consumerMeta) {
        byte[] triggerBytes = placedMeta.getCustomTagContainer()
                .getCustomTag(this.proto_bytes, ItemTagType.BYTE_ARRAY);

        byte[] skillBytes = consumerMeta.getCustomTagContainer()
                .getCustomTag(this.proto_bytes, ItemTagType.BYTE_ARRAY);

        SkillTrigger.Builder triggerBuilder = SkillTrigger.newBuilder();
        Skill.Builder skillBuilder = Skill.newBuilder();
        try {
            triggerBuilder.mergeFrom(triggerBytes);
            skillBuilder.mergeFrom(skillBytes);

            skillBuilder.addTrigger(triggerBuilder);

            consumerMeta.getCustomTagContainer()
                    .setCustomTag(this.proto_bytes, ItemTagType.BYTE_ARRAY, skillBuilder.build().toByteArray());

            consumer.setItemMeta(consumerMeta);

            ItemMeta removeMe = Bukkit.getItemFactory().getItemMeta(placed.getType());

            removeMe.setDisplayName("Remove Me");

            placed.setItemMeta(removeMe);

            inventoryClickEvent.setCancelled(true);

            Bukkit.getScheduler()
                    .scheduleSyncDelayedTask(plugin, () -> inventoryClickEvent.getWhoClicked()
                            .setItemOnCursor(null), 1);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    private void consumeSkill(InventoryClickEvent inventoryClickEvent,
                              ItemStack placed,
                              ItemStack consumer,
                              ItemMeta placedMeta, ItemMeta consumerMeta) {
        byte[] skillBytes = placedMeta.getCustomTagContainer()
                .getCustomTag(this.proto_bytes, ItemTagType.BYTE_ARRAY);

        byte[] lootyBytes = consumerMeta.getCustomTagContainer()
                .getCustomTag(this.proto_bytes, ItemTagType.BYTE_ARRAY);

        Skill.Builder skillBuilder = Skill.newBuilder();
        ItemType.Builder lootyBuilder = ItemType.newBuilder();
        try {
            skillBuilder.mergeFrom(skillBytes);
            lootyBuilder.mergeFrom(lootyBytes);

            lootyBuilder.addSkill(skillBuilder);

            consumerMeta.getCustomTagContainer()
                    .setCustomTag(this.proto_bytes, ItemTagType.BYTE_ARRAY, lootyBuilder.build().toByteArray());

            consumerMeta.getCustomTagContainer()
                    .setCustomTag(this.dirty, ItemTagType.BYTE, (byte) 1);

            List<String> currentLore = consumerMeta.getLore();

            List<String> newLore = ImmutableList.<String>builder().addAll(currentLore != null ? currentLore : ImmutableList
                    .of())
                    .add("- " + placedMeta.getDisplayName() + ":")
                    .addAll(placedMeta.getLore() != null ? placedMeta.getLore()
                            .stream()
                            .map(a -> "  " + a)
                            .collect(toImmutableList()) : ImmutableList.of())
                    .build();

            consumerMeta.setLore(newLore);

            consumer.setItemMeta(consumerMeta);
            ItemMeta removeMe = Bukkit.getItemFactory().getItemMeta(placed.getType());
            removeMe.setDisplayName("Remove Me");
            placed.setItemMeta(removeMe);
            inventoryClickEvent.setCancelled(true);

            Bukkit.getScheduler()
                    .scheduleSyncDelayedTask(plugin, () -> inventoryClickEvent.getWhoClicked()
                            .setItemOnCursor(null), 1);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    private void consumeAction(InventoryClickEvent inventoryClickEvent,
                               ItemStack placed,
                               ItemStack consumer,
                               ItemMeta placedMeta, ItemMeta consumerMeta) {
        byte[] actionBytes = placedMeta.getCustomTagContainer()
                .getCustomTag(this.proto_bytes, ItemTagType.BYTE_ARRAY);

        byte[] skillBytes = consumerMeta.getCustomTagContainer()
                .getCustomTag(this.proto_bytes, ItemTagType.BYTE_ARRAY);

        Builder actionBuilder = Action.newBuilder();
        Skill.Builder skillBuilder = Skill.newBuilder();
        try {
            actionBuilder.mergeFrom(actionBytes);
            skillBuilder.mergeFrom(skillBytes);

            skillBuilder.addAction(actionBuilder);

            consumerMeta.getCustomTagContainer()
                    .setCustomTag(this.proto_bytes, ItemTagType.BYTE_ARRAY, skillBuilder.build().toByteArray());


            List<String> currentLore = consumerMeta.getLore();

            List<String> newLore = ImmutableList.<String>builder().addAll(currentLore != null ? currentLore : ImmutableList
                    .of())
                    .add("- " + placedMeta.getDisplayName() + ":")
                    .addAll(placedMeta.getLore() != null ? placedMeta.getLore()
                            .stream()
                            .map(a -> "  " + a)
                            .collect(toImmutableList()) : ImmutableList.of())
                    .build();

            consumerMeta.setLore(newLore);

            consumer.setItemMeta(consumerMeta);

            ItemMeta removeMe = Bukkit.getItemFactory().getItemMeta(placed.getType());

            removeMe.setDisplayName("Remove Me");

            placed.setItemMeta(removeMe);

            inventoryClickEvent.setCancelled(true);

            Bukkit.getScheduler()
                    .scheduleSyncDelayedTask(plugin, () -> inventoryClickEvent.getWhoClicked()
                            .setItemOnCursor(null), 1);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }


    private void consumeComponent(InventoryClickEvent inventoryClickEvent,
                                  ItemStack placed,
                                  ItemStack consumer,
                                  ItemMeta placedMeta, ItemMeta consumerMeta) {
        byte[] componentBytes = placedMeta.getCustomTagContainer()
                .getCustomTag(this.proto_bytes, ItemTagType.BYTE_ARRAY);

        byte[] actionBytes = consumerMeta.getCustomTagContainer()
                .getCustomTag(this.proto_bytes, ItemTagType.BYTE_ARRAY);

        String componentKey = placedMeta.getCustomTagContainer()
                .getCustomTag(new NamespacedKey(plugin, "proto_type"), ItemTagType.STRING);

        Class<? extends Message> clazz = componentRegister.getMessageForString(componentKey);

        try {
            Method builderMethod = clazz.getMethod("newBuilder");
            Message.Builder componentBuilder = (Message.Builder) builderMethod.invoke(null);

            componentBuilder.mergeFrom(componentBytes);


            Builder actionBuilder = Action
                    .newBuilder();

            actionBuilder.mergeFrom(actionBytes);

            Any pack = Any.pack(componentBuilder.build());

            for (int i = 0; i < actionBuilder.getComponentList().size(); i++) {
                if (actionBuilder.getComponentList().get(i).getTypeUrl().equals(pack.getTypeUrl())) {
                    inventoryClickEvent.getWhoClicked()
                            .sendMessage(String.format("Overwriting existing %s", componentBuilder.getClass()
                                    .getSimpleName()));


                    actionBuilder.removeComponent(i);
                    break;
                }
            }

            actionBuilder.addComponent(pack);

            consumerMeta.getCustomTagContainer()
                    .setCustomTag(this.proto_bytes, ItemTagType.BYTE_ARRAY, actionBuilder.build().toByteArray());


            List<String> currentLore = consumerMeta.getLore();

            List<String> newLore = ImmutableList.<String>builder().addAll(currentLore != null ? currentLore : ImmutableList
                    .of())
                    .add("- " + WordUtils.capitalize(componentKey))
                    .addAll(MessageFormater.format(componentBuilder.build(), 1))
                    .build();

            consumerMeta.setLore(newLore);

            consumer.setItemMeta(consumerMeta);

            ItemMeta removeMe = Bukkit.getItemFactory().getItemMeta(placed.getType());

            removeMe.setDisplayName("Remove Me");

            placed.setItemMeta(removeMe);

            inventoryClickEvent.setCancelled(true);

            Bukkit.getScheduler()
                    .scheduleSyncDelayedTask(plugin, () -> inventoryClickEvent.getWhoClicked()
                            .setItemOnCursor(null), 1);

        } catch (NoSuchMethodException | IllegalAccessException | InvalidProtocolBufferException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent blockPlaceEvent) {
        if (blockPlaceEvent.getItemInHand().hasItemMeta() && blockPlaceEvent.getItemInHand()
                .getItemMeta()
                .getCustomTagContainer()
                .hasCustomTag(typeKey, ItemTagType.STRING)) {
            blockPlaceEvent.setCancelled(true);
        }
    }


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent playerInteractEvent) {
        if (playerInteractEvent.hasBlock()) {
            Block clickedBlock = playerInteractEvent.getClickedBlock();

            if (clickedBlock.getType() == Material.PLAYER_HEAD && playerInteractEvent.getAction() == RIGHT_CLICK_BLOCK && playerInteractEvent
                    .getHand() == EquipmentSlot.HAND) {

                UUID uniqueId = ((Skull) clickedBlock.getState()).getOwningPlayer().getUniqueId();
                if (uniqueId.equals(ORANGE_THING_UUID)) {
                    openComponentCrafting(playerInteractEvent.getPlayer());
                    playerInteractEvent.setCancelled(true);
                } else if (uniqueId.equals(DEVICE_UUID)) {
                    openTriggerCrafting(playerInteractEvent.getPlayer());
                    playerInteractEvent.setCancelled(true);
                } else if (uniqueId.equals(CAULDRON_UUID)) {
                    createLootyItem(playerInteractEvent.getPlayer());
                    playerInteractEvent.setCancelled(true);
                }
            }
        }
    }

    private Map<UUID, GuiHolder> componentMap = new HashMap<>();
    private Map<UUID, GuiHolder> triggerMap = new HashMap<>();

    private void openComponentCrafting(Player player) {
        componentMap.computeIfAbsent(player.getUniqueId(), uuid -> new GuiHolder(6, "Component Creator", new ComponentEditorView(plugin, componentRegister), plugin))
                .show(player);
    }

    private void openTriggerCrafting(Player player) {
        triggerMap.computeIfAbsent(player.getUniqueId(), uuid -> new GuiHolder(6, "Trigger Creator", new TriggerEditorView(plugin), plugin))
                .show(player);
    }

    private void createLootyItem(Player player) {
        ItemStack doused = player.getInventory().getItemInMainHand();

        if (doused.getType() == Material.AIR) {
            return;
        }

        String name;

        if (doused.hasItemMeta() && doused.getItemMeta().hasDisplayName()) {
            name = doused.getItemMeta().getDisplayName();
        } else {
            name = WordUtils.capitalizeFully(doused.getType().name().replace("_", " ").toLowerCase());
        }

        ItemMeta itemMeta;
        if (!doused.hasItemMeta()) {
            itemMeta = Bukkit.getItemFactory().getItemMeta(doused.getType());
        } else {
            itemMeta = doused.getItemMeta();
        }

        if (!itemMeta.getCustomTagContainer().hasCustomTag(this.typeKey, ItemTagType.STRING)) {
            itemMeta.getCustomTagContainer()
                    .setCustomTag(this.uuidKey, ItemTagType.BYTE_ARRAY, getBytesFromUUID(UUID.randomUUID()));

            itemMeta.getCustomTagContainer()
                    .setCustomTag(this.proto_bytes, ItemTagType.BYTE_ARRAY, ItemType.getDefaultInstance()
                            .toByteArray());


            itemMeta.getCustomTagContainer()
                    .setCustomTag(this.typeKey, ItemTagType.STRING, "looty");

            itemMeta.setDisplayName(ChatColor.DARK_PURPLE + name);

            itemMeta.setUnbreakable(true);

            player.sendMessage(String.format(ChatColor.LIGHT_PURPLE + "You douse your " + ChatColor.AQUA + "%s" + ChatColor.LIGHT_PURPLE + " in the powerful elixir", name));
        } else {
            String type = itemMeta.getCustomTagContainer().getCustomTag(this.typeKey, ItemTagType.STRING);
            if (type.equals("looty")) {
                player.sendMessage(String.format(ChatColor.LIGHT_PURPLE + "Your " + ChatColor.DARK_PURPLE + "%s" + ChatColor.LIGHT_PURPLE + " is already imbued", name));
            } else {
                player.sendMessage(String.format(ChatColor.LIGHT_PURPLE + "Your " + ChatColor.AQUA + "%s" + ChatColor.LIGHT_PURPLE + " is a %s and cannot be imbued", name, type));
            }
        }

        doused.setItemMeta(itemMeta);
    }

    public static byte[] getBytesFromUUID(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());

        return bb.array();
    }
}
