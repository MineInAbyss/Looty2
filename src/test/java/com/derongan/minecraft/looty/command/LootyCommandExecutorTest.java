package com.derongan.minecraft.looty.command;

import com.derongan.minecraft.looty.item.ConfigItemRegister;
import com.derongan.minecraft.looty.skill.proto.ItemType;
import com.google.common.collect.ImmutableList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.junit.Test;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LootyCommandExecutorTest {
    @Test
    public void onTabComplete() {
        ConfigItemRegister configItemRegister = mock(ConfigItemRegister.class);
        when(configItemRegister.getAllTypes()).thenReturn(ImmutableList.of(ItemType.newBuilder()
                .setName("Itam")
                .build(), ItemType.newBuilder()
                .setName("iTems")
                .build()));
        LootyCommandExecutor lootyCommandExecutor = new LootyCommandExecutor(configItemRegister, null, null, null);
        String[] partialArgs = {"it"};
        String[] partialArgs2 = {"ita"};
        String[] partialArgs3 = {"itam otherarg"};

        List<String> results = lootyCommandExecutor.onTabComplete(mock(CommandSender.class), createCommand("looty"), "", partialArgs);
        List<String> results2 = lootyCommandExecutor.onTabComplete(mock(CommandSender.class), createCommand("looty"), "", partialArgs2);
        List<String> results3 = lootyCommandExecutor.onTabComplete(mock(CommandSender.class), createCommand("looty"), "", partialArgs3);

        assertThat(results).containsExactly("Itam", "iTems");
        assertThat(results2).containsExactly("Itam");
        assertThat(results3).isEmpty();
    }

    private Command createCommand(String commandName) {
        Command command = mock(Command.class);
        when(command.getName()).thenReturn(commandName);

        return command;
    }
}