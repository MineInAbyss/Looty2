package com.derongan.minecraft.looty;

import com.badlogic.ashley.core.Engine;
import com.derongan.minecraft.looty.Item.ItemSkillListener;
import com.derongan.minecraft.looty.Item.ItemType;
import com.derongan.minecraft.looty.registration.ItemRegistrar;
import com.derongan.minecraft.looty.registration.PlayerSkillRegistrar;
import com.derongan.minecraft.looty.skill.*;
import com.derongan.minecraft.looty.skill.component.effective.Damage;
import com.derongan.minecraft.looty.skill.component.effective.Ignite;
import com.derongan.minecraft.looty.skill.component.effective.Lightning;
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


        itemRegistrar.register(weatherVane());

        server.getScheduler().scheduleSyncRepeatingTask(lootyPlugin, () -> engine.update(1), 1, 1);
        server.getPluginManager().registerEvents(itemSkillListener, lootyPlugin);
        lootyPlugin.getCommand("looty").setExecutor(lootyCommandExecutor);
        lootyPlugin.getCommand("looties").setExecutor(lootyCommandExecutor);


        logger.info("Loaded Looty");
    }

    private ItemType weatherVane() {
        SkillTrigger normalTrigger = SkillTrigger.builder().setHand(Hand.RIGHT).build();
        SkillTrigger leftTrigger = SkillTrigger.builder()
                .setHand(Hand.LEFT)
                .build();
        SkillTrigger crouchTrigger = SkillTrigger.builder()
                .setHand(Hand.RIGHT)
                .addModifier(InputModifier.SNEAKING)
                .build();

        ItemType weatherVaneType = ItemType.builder()
                .setDurability((short) 1)
                .setMaterial(Material.DIAMOND_AXE)
                .setName("Test Axe")
                .addSkillWithTrigger(normalTrigger, getLavaRain())
                .addSkillWithTrigger(leftTrigger, getWaterRain())
                .build();

        return weatherVaneType;
    }

    private Skill getWaterRain() {
        ActionEntityBuilder rainActionEntityBuilder = new ActionEntityBuilder();

        rainActionEntityBuilder
                .addComponent(() -> {
                    OriginChooser originChooser = new OriginChooser();
                    originChooser.locationReferenceType = LocationReferenceType.IMPACT;
                    originChooser.magnitude = 28.0;
                    originChooser.directionType = DirectionType.UP;

                    return originChooser;
                })
                .addComponent(() -> Particle.create(org.bukkit.Particle.DRIP_WATER, Particle.ParticleStyle.RANDOM))
                .addComponent(() -> {
                    Radius radius = new Radius();
                    radius.radius = 2.0;
                    return radius;
                })
                .addComponent(() -> {
                    Linger linger = new Linger();
                    linger.duration = 15 * 20;
                    return linger;
                });

        ActionEntityBuilder cloudActionEntityBuilder = new ActionEntityBuilder();
        cloudActionEntityBuilder
                .addComponent(() -> {
                    OriginChooser originChooser = new OriginChooser();
                    originChooser.locationReferenceType = LocationReferenceType.IMPACT;
                    originChooser.magnitude = 28.0;
                    originChooser.directionType = DirectionType.UP;

                    return originChooser;
                })
                .addComponent(() -> Particle.create(org.bukkit.Particle.CLOUD, Particle.ParticleStyle.RANDOM))
                .addComponent(() -> {
                    Radius radius = new Radius();
                    radius.radius = 3.0;
                    return radius;
                })
                .addComponent(() -> {
                    Linger linger = new Linger();
                    linger.duration = 15 * 20;
                    return linger;
                });

        ActionEntityBuilder splashActionEntityBuilder = new ActionEntityBuilder();
        splashActionEntityBuilder
                .addComponent(() -> {
                    OriginChooser originChooser = new OriginChooser();
                    originChooser.locationReferenceType = LocationReferenceType.IMPACT;
                    originChooser.magnitude = 0.0;
                    originChooser.directionType = DirectionType.UP;

                    return originChooser;
                })
                .addComponent(() -> Particle.create(org.bukkit.Particle.WATER_SPLASH, Particle.ParticleStyle.RANDOM))
                .addComponent(() -> {
                    Radius radius = new Radius();
                    radius.radius = 2.0;
                    return radius;
                })
                .addComponent(Lightning::new)
                .addComponent(() -> Ignite.create(0))
                .addComponent(() -> {
                    Linger linger = new Linger();
                    linger.duration = 15 * 20;
                    return linger;
                });

        return Skill.builder()
                .addActionBuilder(rainActionEntityBuilder)
                .addActionBuilder(cloudActionEntityBuilder)
                .addActionBuilder(splashActionEntityBuilder)
                .build();
    }

    private Skill getLavaRain() {
        ActionEntityBuilder rainActionEntityBuilder = new ActionEntityBuilder();

        rainActionEntityBuilder
                .addComponent(() -> {
                    OriginChooser originChooser = new OriginChooser();
                    originChooser.locationReferenceType = LocationReferenceType.IMPACT;
                    originChooser.magnitude = 28.0;
                    originChooser.directionType = DirectionType.UP;

                    return originChooser;
                })
                .addComponent(() -> Particle.create(org.bukkit.Particle.DRIP_LAVA, Particle.ParticleStyle.RANDOM))
                .addComponent(() -> {
                    Radius radius = new Radius();
                    radius.radius = 2.0;
                    return radius;
                })
                .addComponent(() -> {
                    Linger linger = new Linger();
                    linger.duration = 15 * 20;
                    return linger;
                });

        ActionEntityBuilder cloudActionEntityBuilder = new ActionEntityBuilder();
        cloudActionEntityBuilder
                .addComponent(() -> {
                    OriginChooser originChooser = new OriginChooser();
                    originChooser.locationReferenceType = LocationReferenceType.IMPACT;
                    originChooser.magnitude = 28.0;
                    originChooser.directionType = DirectionType.UP;

                    return originChooser;
                })
                .addComponent(() -> Particle.create(org.bukkit.Particle.CLOUD, Particle.ParticleStyle.RANDOM))
                .addComponent(() -> {
                    Radius radius = new Radius();
                    radius.radius = 3.0;
                    return radius;
                })
                .addComponent(() -> {
                    Linger linger = new Linger();
                    linger.duration = 15 * 20;
                    return linger;
                });

        ActionEntityBuilder splashActionEntityBuilder = new ActionEntityBuilder();
        splashActionEntityBuilder
                .addComponent(() -> {
                    OriginChooser originChooser = new OriginChooser();
                    originChooser.locationReferenceType = LocationReferenceType.IMPACT;
                    originChooser.magnitude = 0.0;
                    originChooser.directionType = DirectionType.UP;

                    return originChooser;
                })
                .addComponent(() -> Particle.create(org.bukkit.Particle.LAVA, Particle.ParticleStyle.RANDOM))
                .addComponent(() -> {
                    Radius radius = new Radius();
                    radius.radius = 2.0;
                    return radius;
                })
                .addComponent(() -> Ignite.create(10))
                .addComponent(() -> {
                    Linger linger = new Linger();
                    linger.duration = 15 * 20;
                    return linger;
                });

        return Skill.builder()
                .addActionBuilder(rainActionEntityBuilder)
                .addActionBuilder(cloudActionEntityBuilder)
                .addActionBuilder(splashActionEntityBuilder)
                .build();
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
                }).addComponent(() -> Damage.create(1));

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
                    targetChooser.magnitude = 20.0;

                    return targetChooser;
                })
                .addComponent(() -> Particle.create(org.bukkit.Particle.WATER_DROP, Particle.ParticleStyle.OUTLINE))
                .addComponent(() -> {
                    Radius radius = new Radius();
                    radius.radius = .5;

                    return radius;
                })
                .addComponent(() -> {
                    Movement movement = new Movement();
                    movement.headSpeed = .2;
                    movement.tailSpeed = .2;

                    movement.tailWait = 15;

                    return movement;
                });

        return Skill.builder().addActionBuilder(actionEntityBuilder).build();
    }
}
