package com.derongan.minecraft.looty.ui;

import com.derongan.minecraft.guiy.gui.*;
import com.derongan.minecraft.guiy.gui.inputs.MessageFormater;
import com.derongan.minecraft.guiy.gui.inputs.ProtobufInput;
import com.derongan.minecraft.looty.LootyPlugin;
import com.derongan.minecraft.looty.skill.component.ComponentRegister;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import de.erethon.headlib.HeadLib;
import org.bukkit.*;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.inventory.meta.tags.ItemTagType;

import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

import static com.derongan.minecraft.guiy.GuiyKeys.TYPE_KEY;
import static com.derongan.minecraft.looty.ui.LootyEditorFactory.*;
import static com.google.common.collect.ImmutableList.toImmutableList;

public class LootyEditorGui extends GuiHolder {
    private final LootyPlugin lootyPlugin;
    private final ComponentRegister componentRegister;
    private final LootyEditorListener lootyEditorListener;
    private Layout main;
    private Layout selectItemLayout;
    private Layout selectSkillLayout;
    private Layout editSkillLayout;
    private Layout editActionLayout;
    private ClickableElement confirmItemButton;

    private SwappableElement swappableArea;
    private Layout history;

    private Random random = new Random();
    private ClickableElement editDraggable;

    @Inject
    public LootyEditorGui(LootyPlugin lootyPlugin,
                          ComponentRegister componentRegister,
                          LootyEditorListener lootyEditorListener) {
        super(6, "Looty Editor", lootyPlugin);
        this.lootyPlugin = lootyPlugin;
        this.componentRegister = componentRegister;
        this.lootyEditorListener = lootyEditorListener;


        main = buildMain();

        selectItemLayout = buildSelectItemLayout();
        selectSkillLayout = buildSelectSkillLayout();
        editSkillLayout = buildEditSkillLayout();
        editActionLayout = buildEditActionLayout();

        this.setElement(main);

        swappableArea.swap(selectItemLayout);
    }


    private Layout buildMain() {
        Layout main = new Layout();
        swappableArea = new SwappableElement(new Layout());
        history = buildHistoryList();

        main.addElement(0, 0, swappableArea);
        main.addElement(8, 0, history);


        return main;
    }

    private Layout buildHistoryList() {
        Layout container = new Layout();
        return container;
    }

    private Layout buildSelectItemLayout() {
        Layout layout = new Layout();

        ContainerElement fillableElement = new ContainerElement(1, 1, null, lootyPlugin);

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 5; y++) {
                if (x == 4 && y == 2) {
                    layout.addElement(x, y, fillableElement);
                    continue;
                }
                layout.addElement(x, y, Cell.forMaterial(Material.IRON_BARS, "-"));
            }
        }

        ScrollingPallet scrollingPallet = new ScrollingPallet(9);

        Element confirm = Cell.forItemStack(HeadLib.CHECKMARK.toItemStack("Confirm"));
        confirmItemButton = new ClickableElement(confirm);

        scrollingPallet.addTool(confirmItemButton);

        confirmItemButton.setClickAction(clickEvent -> {
            if (!fillableElement.hasElement(0, 0)) {
                Location ploc = clickEvent.getRawEvent().getWhoClicked().getLocation();
                ploc.getWorld().playSound(ploc, Sound.ENTITY_CREEPER_HURT, 1, 1);
            } else {
                this.swappableArea.swap(selectSkillLayout);
                Element item = fillableElement.getElement(0, 0);

                history.addElement(0, 4, item);
            }
        });


        layout.addElement(0, 5, scrollingPallet);

        return layout;
    }

    private Layout buildSelectSkillLayout() {
        Layout skillLayout = new Layout();
        ContainerElement skillContainer = new ContainerElement(4, 5, ImmutableSet.of(SKILL_VALUE), lootyPlugin);
        ScrollingPallet scrollingPallet = new ScrollingPallet(9);


        for (int x = 1; x < 8; x++) {
            skillLayout.addElement(x, 4, Cell.forMaterial(Material.IRON_BARS, "-"));
        }

        for (int y = 0; y < 5; y++) {
            skillLayout.addElement(1, y, Cell.forMaterial(Material.IRON_BARS, "-"));
            skillLayout.addElement(7, y, Cell.forMaterial(Material.IRON_BARS, "-"));
        }


        ToolInterceptor toolInterceptor = new ToolInterceptor(skillContainer, lootyPlugin);

        toolInterceptor.registerToolAction("edit", (clickEvent, element) -> {
            Bukkit.getScheduler().scheduleSyncDelayedTask(lootyPlugin, () -> {
                clickEvent.getRawEvent().getWhoClicked().setItemOnCursor(null);
            }, 1);

            ItemStack currentItem = clickEvent.getRawEvent().getCurrentItem();
            if (currentItem != null && currentItem.hasItemMeta()) {
                CustomItemTagContainer customTagContainer = currentItem.getItemMeta()
                        .getCustomTagContainer();
                if (customTagContainer
                        .hasCustomTag(new NamespacedKey(lootyPlugin, TYPE_KEY), ItemTagType.STRING) && customTagContainer
                        .getCustomTag(new NamespacedKey(lootyPlugin, TYPE_KEY), ItemTagType.STRING)
                        .equals(SKILL_VALUE)) {
                    swappableArea.swap(editSkillLayout);
                    history.addElement(0, 3, element);
                }
            }
        });

        skillLayout.addElement(0, 5, scrollingPallet);
        skillLayout.addElement(2, 0, toolInterceptor);

        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);

        head = HeadLib.setSkullOwner(head, "b4f61444-f5fa-4668-91f0-9e00408d6298", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2VkZDIwYmU5MzUyMDk0OWU2Y2U3ODlkYzRmNDNlZmFlYjI4YzcxN2VlNmJmY2JiZTAyNzgwMTQyZjcxNiJ9fX0=");


        ClickableElement addNew = new ClickableElement(Cell.forItemStack(head, "Add Skill"));

        addNew.setClickAction(clickEvent -> {
            skillContainer.addElement(Cell.forItemStack(generateRandomSkill(), "Skill"));
        });

        scrollingPallet.addTool(addNew);

        editDraggable = buildButton(Material.WRITABLE_BOOK, "Edit", "edit");

        editDraggable.setClickAction(clickEvent -> clickEvent.setCancelled(false));

        skillLayout.addElement(0, 0, editDraggable);

        return skillLayout;
    }

    private Layout buildEditSkillLayout() {
        Layout editLayout = new Layout();
        ContainerElement actionContainer = new ContainerElement(4, 3, ImmutableSet.of(ACTION_VALUE), lootyPlugin);


        ScrollingPallet scrollingPallet = new ScrollingPallet(9);

        for (int x = 1; x < 8; x++) {
            editLayout.addElement(x, 4, Cell.forMaterial(Material.IRON_BARS, "-"));
        }

        for (int y = 0; y < 5; y++) {
            editLayout.addElement(1, y, Cell.forMaterial(Material.IRON_BARS, "-"));
            editLayout.addElement(5, y, Cell.forMaterial(Material.IRON_BARS, "-"));
            editLayout.addElement(7, y, Cell.forMaterial(Material.IRON_BARS, "-"));
        }

        ToolInterceptor toolInterceptor = new ToolInterceptor(actionContainer, lootyPlugin);

        toolInterceptor.registerToolAction("edit", (clickEvent, element) -> {
            Bukkit.getScheduler().scheduleSyncDelayedTask(lootyPlugin, () -> {
                clickEvent.getRawEvent().getWhoClicked().setItemOnCursor(null);
            }, 1);

            ItemStack currentItem = clickEvent.getRawEvent().getCurrentItem();
            if (currentItem != null && currentItem.hasItemMeta()) {
                CustomItemTagContainer customTagContainer = currentItem.getItemMeta()
                        .getCustomTagContainer();
                if (customTagContainer
                        .hasCustomTag(new NamespacedKey(lootyPlugin, TYPE_KEY), ItemTagType.STRING) && customTagContainer
                        .getCustomTag(new NamespacedKey(lootyPlugin, TYPE_KEY), ItemTagType.STRING)
                        .equals(ACTION_VALUE)) {
                    swappableArea.swap(editActionLayout);
                    history.addElement(0, 2, element);
                }
            }
        });

        editDraggable = buildButton(Material.WRITABLE_BOOK, "Edit", "edit");

        editDraggable.setClickAction(clickEvent -> clickEvent.setCancelled(false));


        editLayout.addElement(0, 5, scrollingPallet);
        editLayout.addElement(2, 0, toolInterceptor);

        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);

        head = HeadLib.setSkullOwner(head, "b4f61444-f5fa-4668-91f0-9e00408d6298", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2VkZDIwYmU5MzUyMDk0OWU2Y2U3ODlkYzRmNDNlZmFlYjI4YzcxN2VlNmJmY2JiZTAyNzgwMTQyZjcxNiJ9fX0=");


        ClickableElement addAction = new ClickableElement(Cell.forItemStack(head, "Add Action"));
        scrollingPallet.addTool(addAction);

        ItemStack head2 = new ItemStack(Material.PLAYER_HEAD, 1);

        head2 = HeadLib.setSkullOwner(head2, "2feda52e-9a86-49e8-adf5-2270bedbc0fc", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTkxMjNhMDY4ZGFjZmM0Mzk2NGVjODg2OTc2YzRiMDg4NzU2ODczMTZmYzFlZWQyNzExYTExZDY1ZmE3MTIifX19");


        scrollingPallet.addTool(Cell.forItemStack(head2, "Add Trigger"));

        addAction.setClickAction(clickEvent -> {
            actionContainer.addElement(Cell.forItemStack(generateRandomAction(), "Action"));
        });

        editLayout.addElement(0, 0, editDraggable);


        return editLayout;
    }

    private Layout buildEditActionLayout() {
        Layout editActionLayout = new Layout();
        ContainerElement componentContainer = new ContainerElement(4, 5, ImmutableSet.of(COMPONENT_VALUE), lootyPlugin);

        ToolInterceptor toolInterceptor = new ToolInterceptor(componentContainer, lootyPlugin);


        for (int x = 1; x < 8; x++) {
            editActionLayout.addElement(x, 4, Cell.forMaterial(Material.IRON_BARS, "-"));
        }

        for (int y = 0; y < 5; y++) {
            editActionLayout.addElement(1, y, Cell.forMaterial(Material.IRON_BARS, "-"));
            editActionLayout.addElement(7, y, Cell.forMaterial(Material.IRON_BARS, "-"));
        }

        ScrollingPallet scrollingPallet = new ScrollingPallet(9);

        editActionLayout.addElement(0, 5, scrollingPallet);
        editActionLayout.addElement(2, 0, toolInterceptor);

        Iterator<Material> woolIterator = getWoolIterator();

        componentRegister.getAllComponents().forEach(comp -> {
            ItemStack component = createComponent(woolIterator.next());


            try {
                String key = comp.getSimpleName()
                        .toLowerCase();
                Class<? extends Message> clazz = componentRegister.getMessageForString(key);

                Method builderMethod = clazz.getMethod("newBuilder");
                Message.Builder messageBuilder = (Message.Builder) builderMethod.invoke(null);

                ItemMeta itemMeta = component.getItemMeta();
                itemMeta.setLore(MessageFormater.format(messageBuilder.build()));

                itemMeta.getCustomTagContainer()
                        .setCustomTag(new NamespacedKey(lootyPlugin, "proto_data"), ItemTagType.BYTE_ARRAY, messageBuilder
                                .build()
                                .toByteArray());

                itemMeta.getCustomTagContainer()
                        .setCustomTag(new NamespacedKey(lootyPlugin, "proto_key"), ItemTagType.STRING, key);

                component.setItemMeta(itemMeta);

                Element cell = Cell.forItemStack(component, comp.getSimpleName());

                ClickableElement clickableElement = new ClickableElement(cell);

                clickableElement.setClickAction(clickEvent -> {
                    clickEvent.setCancelled(false);
                });

                scrollingPallet.addTool(clickableElement);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        toolInterceptor.registerToolAction("edit", (clickEvent, element) -> {
            Bukkit.getScheduler().scheduleSyncDelayedTask(lootyPlugin, () -> {
                clickEvent.getRawEvent().getWhoClicked().setItemOnCursor(null);
            }, 1);

            if (clickEvent.getRawEvent().getCurrentItem() != null && clickEvent.getRawEvent()
                    .getCurrentItem()
                    .hasItemMeta()) {
                //todo hack
                ItemStack currentItem = ((Cell) element).itemStack;

                CustomItemTagContainer customTagContainer = currentItem.getItemMeta()
                        .getCustomTagContainer();
                if (customTagContainer
                        .hasCustomTag(new NamespacedKey(lootyPlugin, TYPE_KEY), ItemTagType.STRING) && customTagContainer
                        .getCustomTag(new NamespacedKey(lootyPlugin, TYPE_KEY), ItemTagType.STRING)
                        .equals(COMPONENT_VALUE)) {

                    Layout protoLayout = new Layout();

                    byte[] componentData = customTagContainer.getCustomTag(new NamespacedKey(lootyPlugin, "proto_data"), ItemTagType.BYTE_ARRAY);
                    String key = customTagContainer.getCustomTag(new NamespacedKey(lootyPlugin, "proto_key"), ItemTagType.STRING);

                    try {
                        Method builderMethod = componentRegister.getMessageForString(key).getMethod("newBuilder");
                        Message.Builder messageBuilder = (Message.Builder) builderMethod.invoke(null);

                        messageBuilder.mergeFrom(componentData);

                        ProtobufInput protobufInput = new ProtobufInput(messageBuilder.build());
                        protoLayout.addElement(0, 0, protobufInput);

                        ClickableElement submit = new ClickableElement(Cell.forItemStack(HeadLib.CHECKMARK.toItemStack("Confirm")));
                        submit.setClickAction(e -> protobufInput.onSubmit());
                        protoLayout.addElement(4, 5, submit);

                        history.addElement(0, 1, element);

                        protobufInput.setSubmitAction(o -> {
                            ItemMeta itemMeta = currentItem.getItemMeta();
                            itemMeta.getCustomTagContainer()
                                    .setCustomTag(new NamespacedKey(lootyPlugin, "proto_data"), ItemTagType.BYTE_ARRAY, ((Message) o)
                                            .toByteArray());


                            itemMeta.setLore(MessageFormater.format((Message) o));

                            currentItem.setItemMeta(itemMeta);

                            swappableArea.swap(editActionLayout);
                            history.removeElement(0, 1);
                        });

                        swappableArea.swap(protoLayout);
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InvalidProtocolBufferException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        editDraggable = buildButton(Material.WRITABLE_BOOK, "Edit", "edit");

        editDraggable.setClickAction(clickEvent -> clickEvent.setCancelled(false));


        ClickableElement finalize = new ClickableElement(Cell.forItemStack(HeadLib.CHECKMARK.toItemStack("Confirm")));

        finalize.setClickAction(clickEvent -> {
            history.removeElement(0, 2);
            swappableArea.swap(editSkillLayout);
        });


        editActionLayout.addElement(0, 0, editDraggable);
        editActionLayout.addElement(0, 1, finalize);

        return editActionLayout;
    }

    private ItemStack createComponent(Material material) {
        ItemMeta meta = Bukkit.getItemFactory().getItemMeta(material);

        meta.getCustomTagContainer()
                .setCustomTag(new NamespacedKey(lootyPlugin, TYPE_KEY), ItemTagType.STRING, COMPONENT_VALUE);

        ItemStack itemStack = new ItemStack(material);

        itemStack.setItemMeta(meta);

        return itemStack;
    }


    private static Iterator<Material> getWoolIterator() {
        List<Material> wools = Arrays.stream(Material.values())
                .filter(mat -> mat.name().endsWith("_WOOL"))
                .collect(toImmutableList());

        return Iterators.cycle(wools);
    }

    private ItemStack generateRandomSkill() {
        List<Material> materialList = Arrays.stream(Material.values())
                .filter(mat -> mat.name().endsWith("BANNER") && !mat.name().contains("WALL"))
                .collect(toImmutableList());
        Material material = materialList.get(random.nextInt(materialList.size()));
        BannerMeta bannerMeta = (BannerMeta) Bukkit.getItemFactory()
                .getItemMeta(material);

        for (int i = 0; i < 3; i++) {
            PatternType patternType = PatternType.values()[random
                    .nextInt(PatternType.values().length)];
            DyeColor dyeColor = DyeColor.values()[random.nextInt(DyeColor.values().length)];
            bannerMeta.addPattern(new Pattern(dyeColor, patternType));
        }

        UUID uuid = UUID.randomUUID();

        byte[] uuidBytes = new byte[16];
        ByteBuffer.wrap(uuidBytes)
                .order(ByteOrder.BIG_ENDIAN)
                .putLong(uuid.getMostSignificantBits())
                .putLong(uuid.getLeastSignificantBits());

        bannerMeta.getCustomTagContainer()
                .setCustomTag(new NamespacedKey(lootyPlugin, SKILL_UUID_KEY), ItemTagType.BYTE_ARRAY, uuidBytes);
        bannerMeta.getCustomTagContainer()
                .setCustomTag(new NamespacedKey(lootyPlugin, TYPE_KEY), ItemTagType.STRING, SKILL_VALUE);

        ItemStack itemStack = new ItemStack(material);
        itemStack.setItemMeta(bannerMeta);
        return itemStack;
    }

    private ItemStack generateRandomAction() {
        List<Material> materialList = Arrays.stream(Material.values())
                .filter(mat -> mat.name().endsWith("_DYE"))
                .collect(toImmutableList());

        Material material = materialList.get(random.nextInt(materialList.size()));

        ItemMeta meta = Bukkit.getItemFactory().getItemMeta(material);

        meta.getCustomTagContainer()
                .setCustomTag(new NamespacedKey(lootyPlugin, TYPE_KEY), ItemTagType.STRING, ACTION_VALUE);


        ItemStack itemStack = new ItemStack(material);

        itemStack.setItemMeta(meta);

        return itemStack;
    }

    private ClickableElement buildButton(Material material, String name, String id) {
        ItemStack itemStack = new ItemStack(material);

        ItemMeta meta = Bukkit.getItemFactory().getItemMeta(material);

        meta.getCustomTagContainer()
                .setCustomTag(new NamespacedKey(lootyPlugin, TYPE_KEY), ItemTagType.STRING, TOOL_VALUE);
        meta.getCustomTagContainer()
                .setCustomTag(new NamespacedKey(lootyPlugin, TOOL_TYPE_KEY), ItemTagType.STRING, id);

        itemStack.setItemMeta(meta);

        return new ClickableElement(Cell.forItemStack(itemStack, name));
    }
}
