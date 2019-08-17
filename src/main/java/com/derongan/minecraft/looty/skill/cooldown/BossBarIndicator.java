package com.derongan.minecraft.looty.skill.cooldown;

import com.derongan.minecraft.looty.skill.proto.Skill;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class BossBarIndicator implements CooldownIndicator {
    private final NamespacedKey namespacedKey;
    private Map<Skill, BossBar> skillToBossBarMap;

    public BossBarIndicator(NamespacedKey namespacedKey) {
        this.namespacedKey = namespacedKey;
        skillToBossBarMap = new HashMap<>();
    }

    @Override
    public void show(Player player) {
        skillToBossBarMap.values().forEach(bossBar -> {
            bossBar.addPlayer(player);
            bossBar.setVisible(true);
        });
    }

    @Override
    public void remove() {
        skillToBossBarMap.values().forEach(BossBar::removeAll);
    }


    @Override
    public void update(Map<Skill, Integer> ticksRemainingBySkill) {
        ticksRemainingBySkill.forEach(this::update);
    }

    private void update(Skill skill, int ticksRemaining) {
        if (skill.getCooldown() == 0) {
            return;
        }

        String name = skill.getName().isEmpty() ? String.valueOf(skill.hashCode()) : skill.getName();

        BossBar bossBar = skillToBossBarMap.computeIfAbsent(skill, skill1 -> Bukkit.createBossBar(namespacedKey, name, BarColor.RED, BarStyle.SOLID));


        bossBar.setProgress(ticksRemaining * 1.0 / skill.getCooldown());

        if (bossBar.getProgress() == 0) {
            bossBar.setVisible(false);
            bossBar.removeAll();
            Bukkit.removeBossBar(namespacedKey);
        } else {
            bossBar.setVisible(true);
        }
    }
}
