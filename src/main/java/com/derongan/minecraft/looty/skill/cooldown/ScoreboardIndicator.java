package com.derongan.minecraft.looty.skill.cooldown;

import com.derongan.minecraft.looty.skill.proto.Skill;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ScoreboardIndicator implements CooldownIndicator {
    public final Scoreboard scoreboard;
    private final Objective objective;
    private List<UUID> players;

    public ScoreboardIndicator() {
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = scoreboard.registerNewObjective("Cooldown", "dummy", "Cooldown");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        players = new ArrayList<>();
    }

    @Override
    public void show(Player player) {
        player.setScoreboard(scoreboard);
        players.add(player.getUniqueId());
    }

    @Override
    public void update(Map<Skill, Integer> ticksRemainingBySkill) {
        ticksRemainingBySkill.forEach(this::update);
    }

    private void update(Skill skill, int ticksRemaining) {
        String name = skill.getName().isEmpty() ? String.valueOf(skill.hashCode()) : skill.getName();
        Score score = objective.getScore(name);
        score.setScore(ticksRemaining);

        if (ticksRemaining <= 0) {
            scoreboard.resetScores(name);
        }
    }

    @Override
    public void remove() {
        players.forEach(a -> Bukkit.getPlayer(a).setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard()));
    }
}
