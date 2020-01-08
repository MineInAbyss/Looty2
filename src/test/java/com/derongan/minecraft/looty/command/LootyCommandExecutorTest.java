package com.derongan.minecraft.looty.command;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;


public class LootyCommandExecutorTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private CommandSender commandSender;
    @Mock
    private Command command;

    @Test
    public void onTabCompleteNoSubCommand() {
        String hi = "hi";
        String hello = "hello";
        SubCommandExecutor helloCommand = new FakeSubCommandExecutor(ImmutableList.of(hello));
        SubCommandExecutor hiCommand = new FakeSubCommandExecutor(ImmutableList.of(hi));
        ImmutableMap<String, SubCommandExecutor> subCommands = ImmutableMap.of(hello, helloCommand, hi, hiCommand);
        LootyCommandExecutor lootyCommandExecutor = new LootyCommandExecutor(subCommands);

        List<String> results = lootyCommandExecutor.onTabComplete(commandSender, command, "", createArgs("h"));

        assertThat(results).containsExactly(hello, hi);
    }

    @Test
    public void onTabCompleteSubCommand() {
        String hi = "hi";
        String hello = "hello";
        SubCommandExecutor helloCommand = new FakeSubCommandExecutor(ImmutableList.of(hello));
        SubCommandExecutor hiCommand = new FakeSubCommandExecutor(ImmutableList.of(hi));
        ImmutableMap<String, SubCommandExecutor> subCommands = ImmutableMap.of(hello, helloCommand, hi, hiCommand);
        LootyCommandExecutor lootyCommandExecutor = new LootyCommandExecutor(subCommands);

        List<String> results = lootyCommandExecutor.onTabComplete(commandSender, command, "", createArgs(hi, ""));

        assertThat(results).containsExactly(hi);
    }

    @Test
    public void onCommandDelegatesToSucceedingSubCommand() {
        String success = "success";
        String failure = "failure";
        SubCommandExecutor successSubCommandExecutor = new FakeSubCommandExecutor(ImmutableList.of(success));
        SubCommandExecutor failingSubCommandExecutor = new SubCommandExecutor.FailingSubCommandExecutor();
        ImmutableMap<String, SubCommandExecutor> subCommands = ImmutableMap.of(success, successSubCommandExecutor, failure, failingSubCommandExecutor);
        LootyCommandExecutor lootyCommandExecutor = new LootyCommandExecutor(subCommands);

        boolean result = lootyCommandExecutor.onCommand(commandSender, this.command, "", createArgs(success));

        assertThat(result).isTrue();
    }

    @Test
    public void onCommandDelegatesToFailingSubCommand() {
        String success = "success";
        String failure = "failure";
        SubCommandExecutor successSubCommandExecutor = new FakeSubCommandExecutor(ImmutableList.of(success));
        SubCommandExecutor failingSubCommandExecutor = new SubCommandExecutor.FailingSubCommandExecutor();
        ImmutableMap<String, SubCommandExecutor> subCommands = ImmutableMap.of(success, successSubCommandExecutor, failure, failingSubCommandExecutor);
        LootyCommandExecutor lootyCommandExecutor = new LootyCommandExecutor(subCommands);

        boolean result = lootyCommandExecutor.onCommand(commandSender, this.command, "", createArgs(failure));

        assertThat(result).isFalse();
    }

    private String[] createArgs(String... args) {
        return args;
    }

    static class FakeSubCommandExecutor implements SubCommandExecutor {

        private final List<String> completions;

        FakeSubCommandExecutor(List<String> completions) {
            this.completions = completions;
        }

        @Override
        public boolean execute(CommandSender sender, List<String> args) {
            return true;
        }

        @Override
        public List<String> complete(CommandSender sender, List<String> args) {
            return completions;
        }
    }
}