package com.derongan.minecraft.looty.ui;

import com.derongan.minecraft.looty.LootyPlugin;
import com.derongan.minecraft.looty.skill.proto.Action;
import com.derongan.minecraft.looty.skill.proto.Skill;
import com.google.common.collect.ImmutableList;
import de.erethon.headlib.HeadLib;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.ItemTagType;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.derongan.minecraft.guiy.GuiyKeys.TYPE_KEY;
import static com.google.common.collect.ImmutableList.toImmutableList;

public class LootyEditorCommandExecutor implements CommandExecutor {

    private final LootyPlugin plugin;
    private static final List<HeadLib> ANIMAL_HEADS = Arrays.stream(HeadLib.values())
            .filter(a -> a.name().startsWith("ANIMAL_")).collect(toImmutableList());

    private static final List<HeadLib> OBJECT_HEADS = Arrays.stream(HeadLib.values())
            .filter(a -> a.name().startsWith("OBJECT_")).collect(toImmutableList());


    private static final Random random = new Random();

    @Inject
    public LootyEditorCommandExecutor(LootyPlugin plugin) {
        this.plugin = plugin;

    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args) {

        if (command.getName().equals("createskill")) {

            ItemStack itemStack = ANIMAL_HEADS.get(random.nextInt(ANIMAL_HEADS.size())).toItemStack();

            ItemMeta itemMeta = itemStack.getItemMeta();

            itemMeta.setDisplayName(ChatColor.RESET + generateName() + " (Skill)");

            itemMeta.getCustomTagContainer()
                    .setCustomTag(new NamespacedKey(plugin, "proto_bytes"), ItemTagType.BYTE_ARRAY, Skill.newBuilder()
                            .build()
                            .toByteArray());

            itemMeta.getCustomTagContainer()
                    .setCustomTag(new NamespacedKey(plugin, TYPE_KEY), ItemTagType.STRING, "skill");

            itemStack.setItemMeta(itemMeta);

            if (sender instanceof Player) {
                ((Player) sender).getInventory().addItem(itemStack);
            } else if (sender instanceof BlockCommandSender) {
                Location location = ((BlockCommandSender) sender).getBlock().getLocation();

                location.getWorld()
                        .getNearbyEntities(location, 3, 3, 3)
                        .stream()
                        .filter(e -> e instanceof Player)
                        .findAny()
                        .ifPresent(e -> ((Player) e).getInventory().addItem(itemStack));
            }
        }

        if (command.getName().equals("createaction")) {

            ItemStack itemStack = OBJECT_HEADS.get(random.nextInt(OBJECT_HEADS.size())).toItemStack();

            ItemMeta itemMeta = itemStack.getItemMeta();

            itemMeta.setDisplayName(ChatColor.RESET + generateName() + " (Action)");

            itemMeta.getCustomTagContainer()
                    .setCustomTag(new NamespacedKey(plugin, "proto_bytes"), ItemTagType.BYTE_ARRAY, Action.newBuilder()
                            .build()
                            .toByteArray());

            itemMeta.getCustomTagContainer()
                    .setCustomTag(new NamespacedKey(plugin, TYPE_KEY), ItemTagType.STRING, "action");

            itemStack.setItemMeta(itemMeta);

            if (sender instanceof Player) {
                ((Player) sender).getInventory().addItem(itemStack);
            } else if (sender instanceof BlockCommandSender) {
                Location location = ((BlockCommandSender) sender).getBlock().getLocation();

                location.getWorld()
                        .getNearbyEntities(location, 3, 3, 3)
                        .stream()
                        .filter(e -> e instanceof Player)
                        .findAny()
                        .ifPresent(e -> ((Player) e).getInventory().addItem(itemStack));
            }
        }


        return true;
    }


    private static String generateName() {
        return PREFIXES.get(random.nextInt(PREFIXES.size())) + " " + SUFFIXES.get(random.nextInt(SUFFIXES.size()));
    }

    private static final ImmutableList<String> PREFIXES = ImmutableList.of("Amazing", "Powerful", "Stupendous", "Murderous", "Killer", "Sick", "Sweet", "Demonic", "Angelic");
    private static final ImmutableList<String> SUFFIXES = ImmutableList.of("Power", "Skill", "Move", "Dance", "Song", "Intent");
}
