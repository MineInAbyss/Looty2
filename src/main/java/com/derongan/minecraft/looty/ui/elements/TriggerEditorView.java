package com.derongan.minecraft.looty.ui.elements;

import com.derongan.minecraft.guiy.gui.*;
import com.derongan.minecraft.guiy.gui.inputs.MessageFormater;
import com.derongan.minecraft.guiy.gui.inputs.ProtobufInput;
import com.derongan.minecraft.looty.skill.proto.SkillTrigger;
import com.google.common.collect.ImmutableSet;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import de.erethon.headlib.HeadLib;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.ItemTagType;
import org.bukkit.plugin.Plugin;

import static com.derongan.minecraft.guiy.GuiyKeys.TYPE_KEY;

public class TriggerEditorView implements Element {
    private final Plugin plugin;
    private final SwappableElement swappableElement;
    private Layout layout;

    public TriggerEditorView(Plugin plugin) {
        this.plugin = plugin;
        this.layout = new Layout();

        this.swappableElement = new SwappableElement(layout);

        ContainerElement containerElement = new ContainerElement(6, 7, ImmutableSet.of("trigger"), plugin);

        ClickableElement createTriggerButton = new ClickableElement(Cell.forItemStack(HeadLib.WOODEN_PLUS.toItemStack(), "New Trigger"));

        ToolInterceptor toolInterceptor = new ToolInterceptor(containerElement, plugin);

        createTriggerButton.setClickAction(clickEvent -> {
            containerElement.addElement(createTriggerElement());
        });

        ToolElement editTool = new ToolElement(Material.WRITABLE_BOOK, "Edit", "edit", plugin);

        toolInterceptor.registerToolAction("edit", (clickEvent, element) -> {
            ItemStack currentItem = clickEvent.getRawEvent().getCurrentItem();

            if (currentItem == null || !currentItem.hasItemMeta()) {
                return;
            }

            try {
                byte[] protoBytes = currentItem.getItemMeta()
                        .getCustomTagContainer()
                        .getCustomTag(new NamespacedKey(plugin, "proto_bytes"), ItemTagType.BYTE_ARRAY);

                SkillTrigger.Builder triggerBuilder = SkillTrigger.newBuilder();
                triggerBuilder.mergeFrom(protoBytes);

                ProtobufInput<SkillTrigger> protobufInput = new ProtobufInput<>(triggerBuilder.build());

                Layout protoEditContainer = new Layout();

                ClickableElement submit = new ClickableElement(Cell.forItemStack(HeadLib.CHECKMARK.toItemStack("Confirm")));

                submit.setClickAction(clickEvent1 -> protobufInput.onSubmit());


                protobufInput.setSubmitAction(o -> {
                    swappableElement.swap(layout);

                    // TODO we need to get data onto this somehow
                    ItemStack itemStack = ((Cell) containerElement.getElement(clickEvent.getX(), clickEvent.getY())).itemStack;

                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.getCustomTagContainer()
                            .setCustomTag(new NamespacedKey(plugin, "proto_bytes"), ItemTagType.BYTE_ARRAY, ((Message) o)
                                    .toByteArray());

                    itemMeta.setLore(MessageFormater.format((Message) o));

                    itemStack.setItemMeta(itemMeta);
                });

                protoEditContainer.addElement(0, 0, protobufInput);
                protoEditContainer.addElement(4, 5, submit);

                swappableElement.swap(protoEditContainer);
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
        });

        this.layout.addElement(0, 0, editTool);
        this.layout.addElement(0, 1, createTriggerButton);
        this.layout.addElement(1, 0, toolInterceptor);
    }

    private Element createTriggerElement() {
        ItemStack itemStack = HeadLib.STONE_EXCLAMATION_MARK.toItemStack();
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.getCustomTagContainer()
                .setCustomTag(new NamespacedKey(plugin, TYPE_KEY), ItemTagType.STRING, "trigger");

        Message.Builder messageBuilder = SkillTrigger.newBuilder();

        itemMeta.getCustomTagContainer()
                .setCustomTag(new NamespacedKey(plugin, "proto_type"), ItemTagType.STRING, "trigger");
        itemMeta.getCustomTagContainer()
                .setCustomTag(new NamespacedKey(plugin, "proto_bytes"), ItemTagType.BYTE_ARRAY, messageBuilder.build()
                        .toByteArray());

        itemMeta.setLore(MessageFormater.format(messageBuilder.build()));


        itemStack.setItemMeta(itemMeta);

        return Cell.forItemStack(itemStack, "Trigger");
    }

    @Override
    public Size getSize() {
        return swappableElement.getSize();
    }

    @Override
    public void draw(GuiRenderer guiRenderer) {
        swappableElement.draw(guiRenderer);
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        swappableElement.onClick(clickEvent);
    }
}
