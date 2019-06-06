package com.derongan.minecraft.looty;

import com.derongan.minecraft.looty.Item.ItemRarity;
import com.derongan.minecraft.looty.Item.ItemType;
import com.derongan.minecraft.looty.registration.ItemRegistrar;
import org.bukkit.command.*;
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
    private final ItemRegistrar itemRegistrar;

    @Inject
    LootyCommandExecutor(ItemRegistrar itemRegistrar) {
        this.itemRegistrar = itemRegistrar;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args) {
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
                        .map(itemType -> itemType.getItemRarity()
                                .orElse(ItemRarity.TOOL)
                                .getColor() + itemType.getName())
                        .map(s -> s.replace(" ", "_"))
                        .collect(Collectors.joining(", ")));
                return true;
            }
        }

        return false;
    }

    private ItemStack getItem(ItemType type) {
        ItemStack item = new org.bukkit.inventory.ItemStack(type.getMaterial());

        ItemMeta meta = item.getItemMeta();
        meta.setUnbreakable(true);

        type.getLore().ifPresent(meta::setLore);

        ((Damageable) meta).setDamage(type.getDurability());

        meta.setDisplayName(type.getItemRarity().orElse(ItemRarity.TOOL).getColor() + type.getName());

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
