package com.derongan.minecraft.looty.ui;

import com.derongan.minecraft.guiy.gui.*;
import com.derongan.minecraft.looty.LootyPlugin;
import com.google.common.collect.ImmutableSet;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.ItemTagType;

import javax.inject.Inject;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static com.derongan.minecraft.guiy.GuiyKeys.TYPE_KEY;
import static com.google.common.collect.ImmutableList.toImmutableList;

public class LootyEditorFactory {

    private static Random random = new Random();
    private final LootyPlugin lootyPlugin;
    public static final String SKILL_UUID_KEY = "skill_uuid";
    public static final String TOOL_TYPE_KEY = "tool_type";
    public static final String TOOL_VALUE = "tool";
    public static final String SKILL_VALUE = "skill";
    public static final String ACTION_VALUE = "action";
    public static final String COMPONENT_VALUE = "component";

    @Inject
    public LootyEditorFactory(LootyPlugin lootyPlugin) {
        this.lootyPlugin = lootyPlugin;
    }

    public Layout buildEditor() {
        Layout main = new Layout();
        Element divider = buildDivider();
        Layout pallet = new Layout();
        Layout skillSelectionWindow = new Layout();
        Layout skillEditWindow = new Layout();
        SwappableElement swappableWindow = new SwappableElement(skillSelectionWindow);
        ContainerElement dropArea = new ContainerElement(6, 5, ImmutableSet.of(SKILL_VALUE), lootyPlugin);
        ToolInterceptor toolInterceptor = new ToolInterceptor(dropArea, lootyPlugin);

        main.addElement(0, 0, pallet);
        main.addElement(1, 0, divider);
        main.addElement(2, 0, swappableWindow);
        main.addElement(7, 0, divider);


        skillSelectionWindow.addElement(0, 0, toolInterceptor);

        ClickableElement newButton = buildButton(Material.BUCKET, "New Skill");
        String editSkillName = "Edit Skill";
        ClickableElement editButton = buildButton(Material.SALMON_BUCKET, editSkillName);
        ClickableElement copyButton = buildButton(Material.WATER_BUCKET, "Copy Skill");
        ClickableElement deleteButton = buildButton(Material.LAVA_BUCKET, "Delete Skill");

        pallet.addElement(0, 0, newButton);
        pallet.addElement(0, 1, editButton);
        pallet.addElement(0, 2, copyButton);
        pallet.addElement(0, 5, deleteButton);

        newButton.setClickAction(clickEvent -> {
            ItemStack itemStack = generateRandomBanner();
            dropArea.addElement(Cell.forItemStack(itemStack, "Skill"));
            clickEvent.setCancelled(true);
        });

        editButton.setClickAction(clickEvent -> {
            clickEvent.setCancelled(false);
        });

        toolInterceptor.registerToolAction(editSkillName, (clickEvent, element) -> {
            swappableWindow.swap(skillEditWindow);
            clickEvent.setCancelled(true);
            Bukkit.getScheduler().scheduleSyncDelayedTask(lootyPlugin, () -> {
                clickEvent.getRawEvent().getWhoClicked().setItemOnCursor(null);
            }, 1);
        });

        return main;
    }

    private Element buildDivider() {
        Layout barrier = new Layout();
        for (int i = 0; i < 6; i++) {
            barrier.addElement(0, i, Cell.forMaterial(Material.BARRIER, "-"));
        }

        return barrier;
    }

    private ClickableElement buildButton(Material material, String name) {
        ItemStack itemStack = new ItemStack(material);

        ItemMeta meta = Bukkit.getItemFactory().getItemMeta(material);

        meta.getCustomTagContainer()
                .setCustomTag(new NamespacedKey(lootyPlugin, TYPE_KEY), ItemTagType.STRING, TOOL_VALUE);
        meta.getCustomTagContainer()
                .setCustomTag(new NamespacedKey(lootyPlugin, TOOL_TYPE_KEY), ItemTagType.STRING, name);

        itemStack.setItemMeta(meta);

        return new ClickableElement(Cell.forItemStack(itemStack, name));
    }

    private ItemStack generateRandomBanner() {
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

            System.out.println(dyeColor);
            System.out.println(patternType);
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
}
