package com.derongan.minecraft.looty.skill.cooldown;

import com.derongan.minecraft.looty.skill.proto.Skill;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.stream.Collectors;

public class VerticalMessageIndicator implements CooldownIndicator {
    private ComponentBuilder componentBuilder;

    public VerticalMessageIndicator() {
    }

    @Override
    public void show(Player player) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, componentBuilder.create());
    }


    @Override
    public void update(Map<Skill, Integer> ticksRemainingBySkill) {
        String result = ticksRemainingBySkill.entrySet().stream().filter(entry -> entry.getValue() > 0).map(entry -> {
            double proportion = entry.getValue() * 1.0 / entry.getKey().getCooldown();

            return ChatColor.BLUE + String.valueOf(ticksRemainingBySkill.hashCode()) + ": " + proportionToString(proportion);
        }).collect(Collectors.joining(" "));

        componentBuilder = new ComponentBuilder(result);
    }

    private String proportionToString(double proportion) {
        int offset = 8 - (int) Math.ceil(proportion * 8);

        int initial = '\u2588';

        return ChatColor.RED + String.valueOf((char) (initial - offset));
    }

    @Override
    public void remove() {

    }
}
