package com.derongan.minecraft.looty.ui;

import com.google.common.collect.ImmutableList;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
public class LootyEditorListener implements Listener {
    public Map<UUID, ProtoBasedForm> playerIsWaitingOnInput = new HashMap<>();

    @Inject
    public LootyEditorListener() {
    }


    @EventHandler
    public void onMessage(AsyncPlayerChatEvent playerChatEvent) {
        ProtoBasedForm proto = playerIsWaitingOnInput.getOrDefault(playerChatEvent.getPlayer()
                .getUniqueId(), new ProtoBasedForm(null, null, ImmutableList
                .of()));
        if (proto.isWaiting()) {
            proto.answer(playerChatEvent.getMessage());
        }
    }
}
