package com.derongan.minecraft.looty;

import com.badlogic.ashley.core.Engine;
import com.derongan.minecraft.looty.component.effective.Ignite;
import com.derongan.minecraft.looty.component.target.Radius;
import com.derongan.minecraft.looty.registration.ItemRegistrar;
import com.derongan.minecraft.looty.registration.PlayerSkillRegistrar;
import org.bukkit.Material;
import org.bukkit.Server;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.logging.Logger;

@Singleton
class Looty {
    private final SkillListener skillListener;
    private final PlayerSkillRegistrar playerSkillRegistrar;
    private final ItemRegistrar itemRegistrar;
    private final LootyCommandExecutor lootyCommandExecutor;
    private final LootyPlugin lootyPlugin;
    private final Server server;
    private final Engine engine;
    private final Logger logger;

    @Inject
    Looty(SkillListener skillListener, PlayerSkillRegistrar playerSkillRegistrar, ItemRegistrar itemRegistrar, LootyCommandExecutor lootyCommandExecutor, LootyPlugin lootyPlugin, Server server, Engine engine, Logger logger) {
        this.skillListener = skillListener;
        this.playerSkillRegistrar = playerSkillRegistrar;
        this.itemRegistrar = itemRegistrar;
        this.lootyCommandExecutor = lootyCommandExecutor;
        this.lootyPlugin = lootyPlugin;
        this.server = server;
        this.engine = engine;
        this.logger = logger;
    }

    void onEnable() {
        SkillTrigger skillTrigger = SkillTrigger.builder().setHand(Hand.RIGHT).build();

        ActionEntityBuilder actionEntityBuilder = new ActionEntityBuilder()
                .addComponent(() -> Radius.create(2))
                .addComponent(() -> Ignite.create(60));

        Skill skill = Skill.builder().addActionBuilder(actionEntityBuilder).build();

        ItemType itemType = ItemType.builder()
                .setDurability((short) 1)
                .setMaterial(Material.DIAMOND_AXE)
                .setName("Test Axe")
                .addSkillWithTrigger(skillTrigger, skill)
                .build();

        itemRegistrar.register(itemType);

        server.getScheduler().scheduleSyncRepeatingTask(lootyPlugin, () -> engine.update(1), 1, 1);
        server.getPluginManager().registerEvents(skillListener, lootyPlugin);
        lootyPlugin.getCommand("looty").setExecutor(lootyCommandExecutor);
        lootyPlugin.getCommand("looties").setExecutor(lootyCommandExecutor);


        logger.info("Loaded Looty");
    }
}
