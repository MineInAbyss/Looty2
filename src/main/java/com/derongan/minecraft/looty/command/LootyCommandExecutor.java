package com.derongan.minecraft.looty.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * CommandExecutor for looty. Delegates calls to {@link SubCommandExecutor}s.
 */
public class LootyCommandExecutor implements TabExecutor {
    private final Map<String, SubCommandExecutor> subCommandExecutorMap;

    @Inject
    public LootyCommandExecutor(Map<String, SubCommandExecutor> subCommandExecutorMap) {
        this.subCommandExecutorMap = subCommandExecutorMap;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args) {

        if (args.length == 0) {
            return false;
        } else {
            SubCommandExecutor subCommandExecutor = subCommandExecutorMap.getOrDefault(args[0],
                    new SubCommandExecutor.FailingSubCommandExecutor());

            return subCommandExecutor.execute(sender, Stream.of(args).skip(1).collect(toImmutableList()));
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
                                                @NotNull Command command,
                                                @NotNull String alias,
                                                @NotNull String[] args) {

        if (args.length == 1) {
            return subCommandExecutorMap.keySet()
                    .stream()
                    .map(String::toLowerCase)
                    .filter(s -> s.startsWith(args[0]))
                    .collect(toImmutableList());
        } else {
            SubCommandExecutor subCommandExecutor = subCommandExecutorMap.getOrDefault(args[0],
                    new SubCommandExecutor.FailingSubCommandExecutor());

            return subCommandExecutor.complete(sender, Stream.of(args).skip(1).collect(toImmutableList()));
        }
    }
}
