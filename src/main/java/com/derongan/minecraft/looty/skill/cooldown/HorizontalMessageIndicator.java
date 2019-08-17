package com.derongan.minecraft.looty.skill.cooldown;

import com.derongan.minecraft.looty.skill.proto.Skill;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.Map;

public class HorizontalMessageIndicator implements CooldownIndicator {
    private ComponentBuilder componentBuilder;

    public HorizontalMessageIndicator() {
    }

    @Override
    public void show(Player player) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, componentBuilder.create());
    }

    @Override
    public void update(Map<Skill, Integer> ticksRemainingBySkill) {
        componentBuilder = new ComponentBuilder("");

        if (ticksRemainingBySkill.size() == 1) {
            Map.Entry<Skill, Integer> entry = ticksRemainingBySkill.entrySet().iterator().next();

            double proportion = entry.getValue() * 1.0 / entry.getKey().getCooldown();

            componentBuilder.append(proportionToString(proportion));
        } else if (ticksRemainingBySkill.size() > 1) {
            Iterator<Map.Entry<Skill, Integer>> iterator = ticksRemainingBySkill.entrySet().iterator();

            Map.Entry<Skill, Integer> entry1 = iterator.next();
            double proportion1 = entry1.getValue() * 1.0 / entry1.getKey().getCooldown();

            Map.Entry<Skill, Integer> entry2 = iterator.next();
            double proportion2 = entry2.getValue() * 1.0 / entry2.getKey().getCooldown();

            componentBuilder.append(proportionToString(proportion1, proportion2));
        }
    }

    @Override
    public void remove() {
    }

    private String proportionToString(double proportion1, double proportion2) {
        int offset1 = (int) Math.ceil(proportion1 * 8);
        int offset2 = (int) Math.ceil(proportion2 * 8);

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            if (i < offset1 && i < offset2) {
                stringBuilder.append("\u2588");
            } else if (i < offset1) {
                stringBuilder.append("\u2580");
            } else if (i < offset2) {
                stringBuilder.append("\u2584");
            } else {
                stringBuilder.append("\u00A0");
            }
        }

        return ChatColor.RED + stringBuilder.toString();
    }

    private String proportionToString(double proportion1) {
        int offset = (int) Math.ceil(proportion1 * 8);

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            if (i < offset) {
                stringBuilder.append("\u2588");
            } else {
                stringBuilder.append("\u00A0");
            }
        }

        return ChatColor.RED + stringBuilder.toString();
    }
}
