package com.derongan.minecraft.looty;

import com.badlogic.ashley.core.Engine;
import com.derongan.minecraft.looty.Item.ItemSkillListener;
import com.derongan.minecraft.looty.Item.ItemType;
import com.derongan.minecraft.looty.registration.ItemRegistrar;
import com.derongan.minecraft.looty.registration.PlayerSkillRegistrar;
import com.derongan.minecraft.looty.skill.*;
import com.derongan.minecraft.looty.skill.component.effective.Particle;
import com.derongan.minecraft.looty.skill.component.target.*;
import org.bukkit.Material;
import org.bukkit.Server;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.logging.Logger;

@Singleton
class Looty {
    private final ItemSkillListener itemSkillListener;
    private final PlayerSkillRegistrar playerSkillRegistrar;
    private final ItemRegistrar itemRegistrar;
    private final LootyCommandExecutor lootyCommandExecutor;
    private final LootyPlugin lootyPlugin;
    private final Server server;
    private final Engine engine;
    private final Logger logger;

    @Inject
    Looty(ItemSkillListener itemSkillListener,
          PlayerSkillRegistrar playerSkillRegistrar,
          ItemRegistrar itemRegistrar,
          LootyCommandExecutor lootyCommandExecutor,
          LootyPlugin lootyPlugin,
          Server server,
          Engine engine,
          Logger logger) {
        this.itemSkillListener = itemSkillListener;
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
        SkillTrigger skillTrigger2 = SkillTrigger.builder()
                .setHand(Hand.RIGHT)
                .addModifier(InputModifier.SNEAKING)
                .build();
        SkillTrigger skillTrigger3 = SkillTrigger.builder()
                .setHand(Hand.RIGHT)
                .addModifier(InputModifier.SPRINTING)
                .build();

        ItemType itemType = ItemType.builder()
                .setDurability((short) 1)
                .setMaterial(Material.DIAMOND_AXE)
                .setName("Test Axe")
                .addSkillWithTrigger(skillTrigger, getSkill())
                .addSkillWithTrigger(skillTrigger2, getSkill2())
                .build();

        itemRegistrar.register(itemType);

        server.getScheduler().scheduleSyncRepeatingTask(lootyPlugin, () -> engine.update(1), 1, 1);
        server.getPluginManager().registerEvents(itemSkillListener, lootyPlugin);
        lootyPlugin.getCommand("looty").setExecutor(lootyCommandExecutor);
        lootyPlugin.getCommand("looties").setExecutor(lootyCommandExecutor);


        logger.info("Loaded Looty");
    }

    private Skill getSkill() {
        ActionEntityBuilder actionEntityBuilder = new ActionEntityBuilder();

        actionEntityBuilder
                .addComponent(() -> {
                    OriginChooser originChooser = new OriginChooser();
                    originChooser.directionType = DirectionType.HEADING;
                    originChooser.locationReferenceType = LocationReferenceType.INITIATOR;
                    originChooser.magnitude = 1.0;

                    return originChooser;
                })
                .addComponent(() -> Particle.create(org.bukkit.Particle.DRAGON_BREATH, Particle.ParticleStyle.OUTLINE))
                .addComponent(() -> {
                    Radius radius = new Radius();
                    radius.radius = 1;

                    return radius;
                })
                .addComponent(() -> {
                    Linger linger = new Linger();

                    linger.duration = 15 * 100;
                    return linger;
                });

        return Skill.builder().addActionBuilder(actionEntityBuilder).build();
    }

    private Skill getSkill2() {
        ActionEntityBuilder actionEntityBuilder = new ActionEntityBuilder();

        actionEntityBuilder
                .addComponent(() -> {
                    OriginChooser originChooser = new OriginChooser();
                    originChooser.directionType = DirectionType.HEADING;
                    originChooser.locationReferenceType = LocationReferenceType.INITIATOR;
                    originChooser.magnitude = 1.0;

                    return originChooser;
                })
                .addComponent(() -> {
                    TargetChooser targetChooser = new TargetChooser();
                    targetChooser.directionType = DirectionType.HEADING;
                    targetChooser.locationReferenceType = LocationReferenceType.INITIATOR;
                    targetChooser.magnitude = 100.0;

                    return targetChooser;
                })
                .addComponent(() -> Particle.create(org.bukkit.Particle.SMOKE_NORMAL, Particle.ParticleStyle.PATH))
                .addComponent(() -> {
                    Radius radius = new Radius();
                    radius.radius = 1;

                    return radius;
                })
                .addComponent(() -> {
                    Speed speed = new Speed();

                    speed.headSpeed = 10;
                    speed.tailSpeed = 10;

                    speed.tailWait = 15;

                    return speed;
                })
                .addComponent(() -> {
                    Linger linger = new Linger();

                    linger.duration = 15 * 10;
                    return linger;
                });

        return Skill.builder().addActionBuilder(actionEntityBuilder).build();
    }
}
