package com.derongan.minecraft.looty.command;

import com.google.common.collect.ImmutableList;
import org.bukkit.command.CommandSender;

import java.util.List;

public class LootyImportSubCommandExecutor implements SubCommandExecutor {
    @Override
    public boolean execute(CommandSender sender, List<String> args) {
        return false;
    }

    @Override
    public List<String> complete(CommandSender sender, List<String> args) {
        return ImmutableList.of();
    }
}
