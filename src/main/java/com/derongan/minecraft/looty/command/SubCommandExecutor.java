package com.derongan.minecraft.looty.command;

import com.google.common.collect.ImmutableList;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Like {@link org.bukkit.command.CommandExecutor} but for subcommands.
 */
public interface SubCommandExecutor {
    class FailingSubCommandExecutor implements SubCommandExecutor {
        @Override
        public boolean execute(CommandSender sender, List<String> args) {
            return false;
        }

        @Override
        public List<String> complete(CommandSender sender, List<String> args) {
            return ImmutableList.of();
        }
    }

    /**
     * Execute the subcommand.
     *
     * @param sender of the command.
     * @param args   for the command, excluding the subcommand itself.
     * @return true if the command was successfully completed.
     */
    boolean execute(CommandSender sender, List<String> args);

    /**
     * Provide autocompletions for the subcommand.
     *
     * @param sender of the command.
     * @param args   for the command, excluding the subcommand itself.
     * @return a list of completions.
     */
    List<String> complete(CommandSender sender, List<String> args);
}
