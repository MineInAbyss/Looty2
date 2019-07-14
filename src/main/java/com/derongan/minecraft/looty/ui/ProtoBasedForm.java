package com.derongan.minecraft.looty.ui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ProtoBasedForm {
    private final Player player;
    private ItemStack beingEdited;
    private Queue<String> stringQueue;
    private boolean done = false;
    private String currentQuestion;

    public ProtoBasedForm(Player player, ItemStack beingEdited, List<String> questions) {
        this.player = player;
        this.beingEdited = beingEdited;

        done = questions.isEmpty();
        stringQueue = new LinkedList<>(questions);
    }

    public boolean isWaiting() {
        return !done;
    }


    public void feed() {
        currentQuestion = stringQueue.remove();
        player.sendMessage(currentQuestion);
    }

    public void answer(String message) {
        if (stringQueue.isEmpty()) {
            done = true;
        } else {
            feed();
        }
    }

}

