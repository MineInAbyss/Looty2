package com.derongan.minecraft.looty;

import com.badlogic.ashley.core.Engine;
import com.derongan.minecraft.looty.component.effective.*;
import com.derongan.minecraft.looty.component.target.Beam;
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
                .addSkillWithTrigger(skillTrigger, getRaygun())
                .addSkillWithTrigger(skillTrigger2, getSmoker())
                .addSkillWithTrigger(skillTrigger3, getZappy())
                .build();

        itemRegistrar.register(itemType);

        server.getScheduler().scheduleSyncRepeatingTask(lootyPlugin, () -> engine.update(1), 1, 1);
        server.getPluginManager().registerEvents(skillListener, lootyPlugin);
        lootyPlugin.getCommand("looty").setExecutor(lootyCommandExecutor);
        lootyPlugin.getCommand("looties").setExecutor(lootyCommandExecutor);


        logger.info("Loaded Looty");
    }


    private static Skill getZappy() {
        ActionEntityBuilder actionEntityBuilder = new ActionEntityBuilder()
                .addComponent(() -> Radius.create(5))
                .addComponent(() -> Lightning.create())
                .addComponent(() -> Beam.create(64))
                .addComponent(()->Particle.create(org.bukkit.Particle.LAVA, Particle.ParticleStyle.TARGET));

        ActionEntityBuilder actionEntityBuilder2 = new ActionEntityBuilder()
                .addComponent(() -> Radius.create(2))
                .addComponent(() -> Beam.create(64))
                .addComponent(()->Particle.create(org.bukkit.Particle.CRIT, Particle.ParticleStyle.SPIRAL));

        return Skill.builder().addActionBuilder(actionEntityBuilder).addActionBuilder(actionEntityBuilder2).build();
    }


    private static Skill getRaygun() {
        ActionEntityBuilder actionEntityBuilder = new ActionEntityBuilder()
                .addComponent(() -> Radius.create(.1))
                .addComponent(() -> Beam.create(32))
                .addComponent(() -> Sound.create(org.bukkit.Sound.ENTITY_ENDER_DRAGON_SHOOT, Sound.SoundLocation.ORIGIN, 2))
                .addComponent(() -> Particle.create(org.bukkit.Particle.DRAGON_BREATH, Particle.ParticleStyle.DOUBLE_SPIRAL));

        ActionEntityBuilder firePlayerBuilder = new ActionEntityBuilder()
                .addComponent(() -> Particle.create(org.bukkit.Particle.FLAME, Particle.ParticleStyle.TARGET))
                .addComponent(() -> Beam.create(32))
//                .addComponent(() -> Damage.create(1))
                .addComponent(() -> Sound.create(org.bukkit.Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, Sound.SoundLocation.TARGET, 2))
                .addComponent(() -> Radius.create(2))
                .addComponent(()-> new VelocityImparting(1, VelocityImparting.Reference.TARGET, VelocityImparting.Reference.AWAY, VelocityImparting.Target.TARGET));

        return Skill.builder().addActionBuilder(actionEntityBuilder).addActionBuilder(firePlayerBuilder).build();
    }

    private static Skill getSmoker() {
        ActionEntityBuilder actionEntityBuilder = new ActionEntityBuilder()
                .addComponent(() -> Radius.create(.25))
                .addComponent(() -> Beam.create(32))
                .addComponent(() -> Sound.create(org.bukkit.Sound.BLOCK_FIRE_AMBIENT, Sound.SoundLocation.ORIGIN, 2))
                .addComponent(() -> Particle.create(org.bukkit.Particle.SMOKE_NORMAL, Particle.ParticleStyle.DOUBLE_SPIRAL));

        ActionEntityBuilder firePlayerBuilder = new ActionEntityBuilder()
                .addComponent(() -> Particle.create(org.bukkit.Particle.FLAME, Particle.ParticleStyle.TARGET))
                .addComponent(() -> Beam.create(32))
                .addComponent(() -> Ignite.create(45))
                .addComponent(() -> Sound.create(org.bukkit.Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, Sound.SoundLocation.TARGET, 2))
                .addComponent(() -> Radius.create(1));

        return Skill.builder().addActionBuilder(actionEntityBuilder).addActionBuilder(firePlayerBuilder).build();
    }
}
