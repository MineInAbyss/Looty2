package com.derongan.minecraft.looty.command;

import com.google.common.collect.ImmutableList;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.ImmutableList.toImmutableList;

public class HelpSubCommandExecutor implements SubCommandExecutor {
    private final Provider<Map<String, SubCommandExecutor>> subcommandsProvider;

    // TODO is this circular dependency avoidable?
    @Inject
    public HelpSubCommandExecutor(Provider<Map<String, SubCommandExecutor>> subcommandsProvider) {
        this.subcommandsProvider = subcommandsProvider;
    }

    @Override
    public boolean execute(CommandSender sender, List<String> args) {
        if (args.size() == 1) {
            sender.sendMessage(String.format("Help for %s", args.get(0)));
            return true;
        } else if (args.isEmpty()) {
            sender.sendMessage("Generic help");
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<String> complete(CommandSender sender, List<String> args) {
        if (args.size() == 1) {
            return subcommandsProvider.get()
                    .keySet()
                    .stream()
                    .map(String::toLowerCase)
                    .filter(s -> s.startsWith(args.get(0)))
                    .collect(toImmutableList());
        } else {
            return ImmutableList.of();
        }
    }
}
