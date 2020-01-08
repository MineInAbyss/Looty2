package com.derongan.minecraft.looty.command;

import com.derongan.minecraft.looty.Permissions;
import com.derongan.minecraft.looty.item.ConfigItemRegister;
import com.google.common.collect.ImmutableList;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Lists available looties.
 */
public class ListSubCommandExecutor implements SubCommandExecutor {
    private final ConfigItemRegister itemRegister;

    @Inject
    ListSubCommandExecutor(ConfigItemRegister itemRegister) {
        this.itemRegister = itemRegister;
    }

    @Override
    public boolean execute(CommandSender sender, List<String> args) {
        if (!sender.hasPermission(Permissions.LIST)) {
            sender.sendMessage("You do not have permission to use this command");
            return true;
        }

        sender.sendMessage("Items: " + itemRegister.getAllTypes().stream()
                .map(itemType -> ChatColor.GRAY + itemType.getName())
                .map(s -> s.replace(" ", "_"))
                .collect(Collectors.joining(", ")));

        return true;
    }

    @Override
    public List<String> complete(CommandSender sender, List<String> args) {
        return ImmutableList.of();
    }
}
