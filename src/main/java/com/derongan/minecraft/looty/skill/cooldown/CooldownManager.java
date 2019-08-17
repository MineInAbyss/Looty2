package com.derongan.minecraft.looty.skill.cooldown;

import com.derongan.minecraft.looty.skill.proto.Skill;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

@Singleton
public class CooldownManager {
    private final Plugin plugin;
    private final Server server;
    private Map<UUID, Map<Skill, Integer>> cooldownBySkillByPlayer;
    private Map<UUID, CooldownIndicator> indicatorMap;
    private int taskId = -1;
    private Supplier<CooldownIndicator> cooldownIndicatorSupplier;

    @Inject
    public CooldownManager(Plugin plugin, Server server) {
        this.plugin = plugin;
        this.server = server;
        cooldownBySkillByPlayer = new HashMap<>();
        indicatorMap = new HashMap<>();

        cooldownIndicatorSupplier = ScoreboardIndicator::new;
    }

    public void start() {
        taskId = server.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            cooldownBySkillByPlayer.forEach(this::updateCooldowns);
        }, 0, 1);
    }

    public void stop() {
        server.getScheduler().cancelTask(taskId);
    }

    public boolean isOnCooldown(Player player, Skill skill) {
        Map<Skill, Integer> cooldownBySkillMap = cooldownBySkillByPlayer.computeIfAbsent(player.getUniqueId(), uuid -> new HashMap<>());
        int cooldown = cooldownBySkillMap.getOrDefault(skill, 0);

        return cooldown > 0;
    }

    public void startCooldown(Player player, Skill skill) {
        Map<Skill, Integer> cooldownBySkillMap = cooldownBySkillByPlayer.computeIfAbsent(player.getUniqueId(), uuid -> new HashMap<>());

        cooldownBySkillMap.put(skill, skill.getCooldown());

        CooldownIndicator cooldownIndicator = indicatorMap.computeIfAbsent(player.getUniqueId(), uuid -> getNewIndicator());

        cooldownIndicator.show(player);
    }


    private void updateCooldowns(UUID uuid, Map<Skill, Integer> cooldownsBySkill) {
        cooldownsBySkill.forEach((skill, integer) -> {
            cooldownsBySkill.put(skill, integer - 1);
        });

        CooldownIndicator cooldownIndicator = indicatorMap.computeIfAbsent(uuid, uuid1 -> cooldownIndicatorSupplier.get());
        cooldownIndicator.update(cooldownsBySkill);
        cooldownsBySkill.values().removeIf(integer -> integer == 0);
        cooldownIndicator.show(Bukkit.getPlayer(uuid));
    }

    private CooldownIndicator getNewIndicator() {
        return cooldownIndicatorSupplier.get();
    }

    public void setCooldownIndicatorSupplier(Supplier<CooldownIndicator> cooldownIndicatorSupplier) {
        this.cooldownIndicatorSupplier = cooldownIndicatorSupplier;

        indicatorMap.forEach((uuid, cooldownIndicator) -> cooldownIndicator.remove());
        indicatorMap.clear();
    }
}
