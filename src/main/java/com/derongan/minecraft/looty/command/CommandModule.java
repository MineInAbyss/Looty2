package com.derongan.minecraft.looty.command;


import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;

@Module
public class CommandModule {

    @Provides
    @IntoMap
    @StringKey("list")
    static SubCommandExecutor provideListSubCommand(ListSubCommandExecutor listSubCommand) {
        return listSubCommand;
    }

    @Provides
    @IntoMap
    @StringKey("give")
    static SubCommandExecutor provideGiveSubCommand(GiveSubCommandExecutor giveSubCommand) {
        return giveSubCommand;
    }

    @Provides
    @IntoMap
    @StringKey("help")
    static SubCommandExecutor provideHelpSubCommand(HelpSubCommandExecutor helpSubCommand) {
        return helpSubCommand;
    }

    @Provides
    @IntoMap
    @StringKey("reload")
    static SubCommandExecutor provideReloadSubCommand(ReloadSubCommandExecutor reloadSubCommand) {
        return reloadSubCommand;
    }

    @Provides
    @IntoMap
    @StringKey("indicator")
    static SubCommandExecutor provideIndicatorSubCommand(CooldownIndicatorSubCommandExecutor indicatorSubCommand) {
        return indicatorSubCommand;
    }
}
