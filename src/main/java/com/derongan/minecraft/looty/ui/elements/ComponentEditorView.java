package com.derongan.minecraft.looty.ui.elements;

import com.badlogic.ashley.core.Component;
import com.derongan.minecraft.guiy.gui.*;
import com.derongan.minecraft.guiy.gui.inputs.MessageFormater;
import com.derongan.minecraft.guiy.gui.inputs.ProtobufInput;
import com.derongan.minecraft.looty.LootyPlugin;
import com.derongan.minecraft.looty.skill.component.ComponentRegister;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import de.erethon.headlib.HeadLib;
import org.bukkit.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.ItemTagType;
import visual.VisualExtensions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.derongan.minecraft.guiy.GuiyKeys.TYPE_KEY;
import static com.google.common.collect.ImmutableList.toImmutableList;

public class ComponentEditorView implements Element {
    private final LootyPlugin plugin;
    private final ComponentRegister componentRegister;
    private Layout layout;
    private final SwappableElement swappableElement;

    public ComponentEditorView(LootyPlugin plugin, ComponentRegister componentRegister) {
        this.plugin = plugin;
        this.componentRegister = componentRegister;
        layout = new Layout();

        swappableElement = new SwappableElement(layout);

        ContainerElement containerElement = new ContainerElement(5, 7, ImmutableSet.of("component"), plugin);

        ToolInterceptor toolInterceptor = new ToolInterceptor(containerElement, plugin);

        ScrollingPallet scrollingPallet = new ScrollingPallet(9);

        componentRegister.getAllComponents()
                .stream()
                .sorted(Comparator.comparing(Class::getSimpleName))
                .map(this::createComponentElement)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(scrollingPallet::addTool);

        ToolElement editTool = new ToolElement(Material.WRITABLE_BOOK, "Edit", "edit", plugin);

        layout.addElement(1, 0, toolInterceptor);
        layout.addElement(0, 5, scrollingPallet);
        layout.addElement(0, 0, editTool);

        ImmutableMap.Builder<String, List<String>> convertersBuilder = ImmutableMap.builder();

        convertersBuilder.put("particle_name", Arrays.stream(Particle.values())
                .filter(p -> p.getDataType() == Void.class)
                .map(Particle::name)
                .collect(toImmutableList()));
        convertersBuilder.put("sound_name", Arrays.stream(Sound.values()).map(Sound::name).collect(toImmutableList()));

        ImmutableMap<String, List<String>> converters = convertersBuilder.build();

        toolInterceptor.registerToolAction("edit", (clickEvent, element) -> {
            ItemStack currentItem = clickEvent.getRawEvent().getCurrentItem();


            if (currentItem == null || !currentItem.hasItemMeta()) {
                return;
            }

            String protoType = currentItem.getItemMeta()
                    .getCustomTagContainer()
                    .getCustomTag(new NamespacedKey(plugin, "proto_type"), ItemTagType.STRING);

            byte[] protoBytes = currentItem.getItemMeta()
                    .getCustomTagContainer()
                    .getCustomTag(new NamespacedKey(plugin, "proto_bytes"), ItemTagType.BYTE_ARRAY);

            Class<? extends Message> clazz = componentRegister.getMessageForString(protoType);

            try {
                Method builderMethod = clazz.getMethod("newBuilder");
                Message.Builder messageBuilder = (Message.Builder) builderMethod.invoke(null);

                messageBuilder.mergeFrom(protoBytes);

                ProtobufInput protobufInput = new ProtobufInput(messageBuilder.build(), converters, plugin);


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
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
        });
    }

    private Optional<Element> createComponentElement(Class<? extends Component> clazz) {
        try {
            String key = clazz.getSimpleName().toLowerCase();
            Class<? extends Message> infoClazz = componentRegister.getMessageForString(key);

            Method builderMethod = infoClazz.getMethod("newBuilder");
            Message.Builder messageBuilder = (Message.Builder) builderMethod.invoke(null);


            ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);

            if (messageBuilder.build().getDescriptorForType().getOptions().hasExtension(VisualExtensions.displayAs)) {
                VisualExtensions.DisplayHead displayHead = messageBuilder.build()
                        .getDescriptorForType()
                        .getOptions()
                        .getExtension(VisualExtensions.displayAs);
                itemStack = HeadLib.setSkullOwner(itemStack, displayHead.getUuid(), displayHead.getTexture());
            } else {
                String firstLetter = clazz.getSimpleName().substring(0, 1).toUpperCase();
                itemStack = HeadLib.valueOf(String.format("QUARTZ_%s", firstLetter))
                        .toItemStack(1, clazz.getSimpleName());
            }

            ItemMeta itemMeta = itemStack.getItemMeta();

            itemMeta.getCustomTagContainer()
                    .setCustomTag(new NamespacedKey(plugin, TYPE_KEY), ItemTagType.STRING, "component");

            itemMeta.getCustomTagContainer()
                    .setCustomTag(new NamespacedKey(plugin, "proto_type"), ItemTagType.STRING, key);
            itemMeta.getCustomTagContainer()
                    .setCustomTag(new NamespacedKey(plugin, "proto_bytes"), ItemTagType.BYTE_ARRAY, messageBuilder.build()
                            .toByteArray());

            itemMeta.setLore(MessageFormater.format(messageBuilder.build()));

            itemStack.setItemMeta(itemMeta);

            Element element = Cell.forItemStack(itemStack, clazz.getSimpleName());

            ClickableElement clickableElement = new ClickableElement(element);
            clickableElement.setClickAction(clickEvent -> clickEvent.setCancelled(false));

            return Optional.of(clickableElement);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            Bukkit.getLogger().warning(String.format("Failed to create element for %s", clazz.getSimpleName()));
            e.printStackTrace();
            return Optional.empty();
        }
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
