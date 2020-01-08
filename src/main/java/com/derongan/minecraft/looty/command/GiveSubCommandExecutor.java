package com.derongan.minecraft.looty.command;

import com.derongan.minecraft.looty.Permissions;
import com.derongan.minecraft.looty.item.ConfigItemRegister;
import com.derongan.minecraft.looty.skill.proto.ItemType;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Handles giving looties to players.
 */
public class GiveSubCommandExecutor implements SubCommandExecutor {
    private final ConfigItemRegister itemRegister;

    @Inject
    public GiveSubCommandExecutor(ConfigItemRegister itemRegister) {
        this.itemRegister = itemRegister;
    }

    @Override
    public boolean execute(CommandSender sender, List<String> args) {
        if (!sender.hasPermission(Permissions.GIVE)) {
            sender.sendMessage("You do not have permission to use this command");
            return true;
        }

        if (args.size() >= 2) {
            int count = 1;
            if (args.size() == 3) {
                if (StringUtils.isNumeric(args.get(2))) {
                    count = Integer.parseInt(args.get(2));
                } else {
                    return false;
                }
            }

            Optional<ItemType> foundItem = itemRegister.getAllTypes()
                    .stream()
                    .filter(a -> convertToInputName(a.getName()).equals(args.get(1).toLowerCase()))
                    .findAny();

            if (foundItem.isPresent() && Bukkit.getPlayer(args.get(0)) != null) {
                for (int i = 0; i < count; i++) {
                    ((InventoryHolder) sender).getInventory().addItem(getItem(foundItem.get()));
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> complete(CommandSender sender, List<String> args) {
        if (args.size() == 1) {
            return Bukkit.getOnlinePlayers()
                    .stream()
                    .map(Player::getDisplayName)
                    .map(ChatColor::stripColor)
                    .collect(toImmutableList());
        } else if (args.size() == 2) {
            return itemRegister.getAllTypes()
                    .stream()
                    .map(ItemType::getName)
                    .map(GiveSubCommandExecutor::convertToInputName)
                    .filter(s -> s.startsWith(args.get(1).toLowerCase()))
                    .collect(toImmutableList());
        } else if (args.size() == 3 && StringUtils.isEmpty(args.get(2))) {
            return ImmutableList.of("1", "64");
        } else {
            return ImmutableList.of();
        }
    }

    private static ItemStack getItem(ItemType type) {
        ItemStack item = new org.bukkit.inventory.ItemStack(Material.valueOf(type.getMaterial()));

        ItemMeta meta = item.getItemMeta();
        meta.setUnbreakable(true);
        meta.setLore(type.getLoreList());
        meta.setCustomModelData(type.getModel());
        meta.setDisplayName(ChatColor.GRAY + type.getName());

        item.setItemMeta(meta);
        return item;
    }

    private static String convertToInputName(String itemName) {
        return itemName.replaceAll(" ", "_").toLowerCase();
    }
}
