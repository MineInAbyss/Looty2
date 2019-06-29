package com.derongan.minecraft.looty;

import com.derongan.minecraft.looty.config.ConfigLoader;
import com.derongan.minecraft.looty.registration.ItemRegister;
import com.derongan.minecraft.looty.skill.proto.ItemType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

class LootyCommandExecutor implements TabExecutor {
    private final ItemRegister itemRegistrar;
    private final ConfigLoader configLoader;

    @Inject
    LootyCommandExecutor(ItemRegister itemRegistrar, ConfigLoader configLoader) {
        this.itemRegistrar = itemRegistrar;
        this.configLoader = configLoader;
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

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (command.getName().equals("looty")) {
                if (args.length == 0) {
                    return false;
                }
                for (ItemType relicType : itemRegistrar.getAllTypes()) {
                    if (relicType.getName().replace(" ", "_").toLowerCase().equals(args[0].toLowerCase())) {
                        player.getInventory().addItem(getItem(relicType));
                        return true;
                    }
                }
            }

            if (command.getName().equals("looties")) {
                player.sendMessage("Items: " + itemRegistrar.getAllTypes().stream()
                        .map(itemType -> ChatColor.GRAY + itemType.getName())
                        .map(s -> s.replace(" ", "_"))
                        .collect(Collectors.joining(", ")));
                return true;
            }
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
        if (command.getName().equals("looty")) {
            return itemRegistrar.getAllTypes()
                    .stream()
                    .map(ItemType::getName)
                    .map(name -> name.replace(" ", "_"))
                    .collect(Collectors.toList());
        }

        return null;
    }
}
