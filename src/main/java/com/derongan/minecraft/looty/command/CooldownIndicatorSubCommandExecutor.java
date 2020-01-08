package com.derongan.minecraft.looty.command;

import com.derongan.minecraft.looty.Permissions;
import com.derongan.minecraft.looty.skill.cooldown.*;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Changes global cooldown indicator.
 */
//TODO move to cooldown module and install instead?
public class CooldownIndicatorSubCommandExecutor implements SubCommandExecutor {
    private final CooldownManager cooldownManager;
    private final Map<String, Supplier<CooldownIndicator>> cooldownSuppliers;

    @Inject
    public CooldownIndicatorSubCommandExecutor(CooldownManager cooldownManager, Plugin plugin) {
        this.cooldownManager = cooldownManager;

        cooldownSuppliers = ImmutableMap.of("vert", VerticalMessageIndicator::new,
                "horiz", HorizontalMessageIndicator::new,
                "score", ScoreboardIndicator::new,
                "boss", () -> new BossBarIndicator(new NamespacedKey(plugin, "bossbar")));

    }

    @Override
    public boolean execute(CommandSender sender, List<String> args) {
        if (!sender.hasPermission(Permissions.CHANGE_INDICATOR)) {
            sender.sendMessage("You do not have permission to use this command");
            return true;
        }

        if (args.size() != 1 || !cooldownSuppliers.containsKey(args.get(0).toLowerCase())) {
            return false;
        }

        cooldownManager.setCooldownIndicatorSupplier(cooldownSuppliers.get(args.get(0).toLowerCase()));
        sender.sendMessage(String.format("Indicator changed to %s", args.get(0).toLowerCase()));
        return true;
    }

    @Override
    public List<String> complete(CommandSender sender, List<String> args) {
        if (args.size() == 1) {
            return cooldownSuppliers.keySet()
                    .stream()
                    .map(String::toLowerCase)
                    .filter(s -> s.startsWith(args.get(0)))
                    .collect(toImmutableList());
        } else {
            return ImmutableList.of();
        }
    }
}
