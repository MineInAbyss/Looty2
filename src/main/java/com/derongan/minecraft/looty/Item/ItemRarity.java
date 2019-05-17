package com.derongan.minecraft.looty.Item;

import org.bukkit.ChatColor;

//TODO extract to config
public enum ItemRarity {
    SPECIAL_GRADE(ChatColor.LIGHT_PURPLE),
    FIRST_GRADE(ChatColor.RED),
    SECOND_GRADE(ChatColor.GOLD),
    THIRD_GRADE(ChatColor.DARK_GREEN),
    FOURTH_GRADE(ChatColor.DARK_BLUE),
    TOOL(ChatColor.GRAY),
    NOT_IMPLEMENTED(ChatColor.WHITE);

    private final ChatColor color;

    ItemRarity(ChatColor color) {
        this.color = color;
    }

    public ChatColor getColor() {
        return color;
    }
}
