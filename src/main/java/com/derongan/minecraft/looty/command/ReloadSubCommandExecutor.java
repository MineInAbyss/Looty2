package com.derongan.minecraft.looty.command;

import com.derongan.minecraft.looty.Permissions;
import com.derongan.minecraft.looty.config.ConfigLoader;
import com.google.common.collect.ImmutableList;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;
import java.util.List;

public class ReloadSubCommandExecutor implements SubCommandExecutor {
    private final ConfigLoader configLoader;

    @Inject
    public ReloadSubCommandExecutor(ConfigLoader configLoader) {
        this.configLoader = configLoader;
    }

    @Override
    public boolean execute(CommandSender sender, List<String> args) {
        if (!sender.hasPermission(Permissions.RELOAD)) {
            sender.sendMessage("You do not have permission to use this command");
            return true;
        }

        sender.sendMessage("Reloading...");
        configLoader.reload();
        sender.sendMessage("Reloaded.");
        return true;
    }

    @Override
    public List<String> complete(CommandSender sender, List<String> args) {
        return ImmutableList.of();
    }
}
