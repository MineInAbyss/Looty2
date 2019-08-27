package com.derongan.minecraft.looty.command;

import com.derongan.minecraft.looty.config.ConfigLoader;
import com.derongan.minecraft.looty.item.ConfigItemRegister;
import com.derongan.minecraft.looty.skill.cooldown.*;
import com.derongan.minecraft.looty.skill.proto.ItemType;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LootyCommandExecutor implements TabExecutor {
    private final ConfigItemRegister itemRegistrar;
    private final ConfigLoader configLoader;
    private final CooldownManager cooldownManager;
    private Plugin plugin;

    @Inject
    LootyCommandExecutor(ConfigItemRegister itemRegistrar,
                         ConfigLoader configLoader,
                         CooldownManager cooldownManager,
                         Plugin plugin) {
        this.itemRegistrar = itemRegistrar;
        this.configLoader = configLoader;
        this.cooldownManager = cooldownManager;
        this.plugin = plugin;
    }

    // TODO should allow /looties from non player senders
    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args) {
        if (command.getName().equals("lootyreload")) {
            sender.sendMessage("Reloading...");
            configLoader.reload();
            sender.sendMessage("Reloaded");
            return true;
        }

        if (command.getName().equals("indicator")) {
            if (args.length == 0) {
                return false;
            }

            switch (args[0].toLowerCase()) {
                case "score":
                    cooldownManager.setCooldownIndicatorSupplier(ScoreboardIndicator::new);
                    break;
                case "vert":
                    cooldownManager.setCooldownIndicatorSupplier(VerticalMessageIndicator::new);
                    break;
                case "horiz":
                    cooldownManager.setCooldownIndicatorSupplier(HorizontalMessageIndicator::new);
                    break;
                case "boss":
                    cooldownManager.setCooldownIndicatorSupplier(() -> new BossBarIndicator(new NamespacedKey(plugin, "bossbar")));
                    break;
                default:
                    return false;
            }

            return true;
        }

        Collection<ItemType> allTypes = itemRegistrar.getAllTypes();
        if (command.getName().equals("looty")) {
            if (args.length == 0) {
                return false;
            }

            Optional<ItemType> itemType = Optional.empty();

            if (args[0].toLowerCase().equals("random")) {
                itemType = allTypes.stream().skip((int) (allTypes.size() * Math.random())).findFirst();

            }
            for (ItemType relicType : allTypes) {
                if (relicType.getName().replace(" ", "_").toLowerCase().equals(args[0].toLowerCase())) {
                    itemType = Optional.of(relicType);
                }
            }

            if (itemType.isPresent()) {
                if (sender instanceof Player) {
                    ((Player) sender).getInventory().addItem(getItem(itemType.get()));
                    return true;
                } else if (sender instanceof BlockCommandSender) {
                    Location location = ((BlockCommandSender) sender).getBlock()
                            .getLocation()
                            .clone()
                            .add(Vector.getRandom());
                    location.getWorld().dropItem(location, getItem(itemType.get()));
                }
            }

        }

        if (command.getName().equals("looties")) {
            sender.sendMessage("Items: " + allTypes.stream()
                    .map(itemType -> ChatColor.GRAY + itemType.getName())
                    .map(s -> s.replace(" ", "_"))
                    .collect(Collectors.joining(", ")));
            return true;
        }

        return false;
    }

    private ItemStack getItem(ItemType type) {
        ItemStack item = new org.bukkit.inventory.ItemStack(Material.valueOf(type.getMaterial()));

        ItemMeta meta = item.getItemMeta();
        meta.setUnbreakable(true);

        meta.setLore(type.getLoreList());

        ((Damageable) meta).setDamage(type.getDurability());

        meta.setDisplayName(ChatColor.GRAY + type.getName());

        item.setItemMeta(meta);

        return item;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
                                                @NotNull Command command,
                                                @NotNull String alias,
                                                @NotNull String[] args) {
        if (command.getName().equals("looty") && args.length == 1) {
            return itemRegistrar.getAllTypes()
                    .stream()
                    .map(ItemType::getName)
                    .map(name -> name.replace(" ", "_"))
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }

        return null;
    }
}
