package com.derongan.minecraft.looty;

import com.badlogic.ashley.core.Engine;
import com.derongan.minecraft.looty.Item.ItemRarity;
import com.derongan.minecraft.looty.Item.ItemSkillListener;
import com.derongan.minecraft.looty.Item.ItemType;
import com.derongan.minecraft.looty.registration.ItemRegistrar;
import com.derongan.minecraft.looty.registration.PlayerSkillRegistrar;
import com.derongan.minecraft.looty.skill.*;
import com.derongan.minecraft.looty.skill.component.Delay;
import com.derongan.minecraft.looty.skill.component.EntityTargetLimit;
import com.derongan.minecraft.looty.skill.component.effective.Ignite;
import com.derongan.minecraft.looty.skill.component.effective.Lightning;
import com.derongan.minecraft.looty.skill.component.effective.Particle;
import com.derongan.minecraft.looty.skill.component.proto.*;
import com.derongan.minecraft.looty.skill.component.target.Grounded;
import com.derongan.minecraft.looty.skill.component.target.Linger;
import com.derongan.minecraft.looty.skill.component.target.OriginChooser;
import com.derongan.minecraft.looty.skill.component.target.Radius;
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


        itemRegistrar.register(rainStick());

        server.getScheduler().scheduleSyncRepeatingTask(lootyPlugin, () -> engine.update(1), 1, 1);
        server.getPluginManager().registerEvents(itemSkillListener, lootyPlugin);
        lootyPlugin.getCommand("looty").setExecutor(lootyCommandExecutor);
        lootyPlugin.getCommand("looties").setExecutor(lootyCommandExecutor);


        logger.info("Loaded Looty");
    }

    private ItemType rainStick() {
        SkillTrigger normalTrigger = SkillTrigger.builder().setHand(Hand.RIGHT).build();
        SkillTrigger leftTrigger = SkillTrigger.builder()
                .setHand(Hand.LEFT)
                .build();
        SkillTrigger crouchTrigger = SkillTrigger.builder()
                .setHand(Hand.RIGHT)
                .addModifier(InputModifier.SNEAKING)
                .build();

        return ItemType.builder()
                .setDurability((short) 1)
                .setMaterial(Material.BLAZE_ROD)
                .setName("Rain Stick")
                .setItemRarity(ItemRarity.SECOND_GRADE)
                .addSkillWithTrigger(normalTrigger, getLavaRain())
                .addSkillWithTrigger(leftTrigger, getWaterRain())
                .build();
    }

    private Skill getWaterRain() {
        ActionEntityBuilder rainActionEntityBuilder = new ActionEntityBuilder();

        rainActionEntityBuilder
                .addComponent(() ->
                        new OriginChooser(OffsetInfo.newBuilder()
                                .setDirectionType(OffsetInfo.DirectionType.UP)
                                .setLocationReferenceType(OffsetInfo.LocationReferenceType.IMPACT)
                                .setMagnitude(28)
                                .build())
                )

                .addComponent(() -> new Particle(ParticleInfo.newBuilder()
                        .setParticleName(org.bukkit.Particle.DRIP_WATER.name())
                        .setFillStyle(ParticleInfo.FillStyle.RANDOM)
                        .build())).addComponent(() -> new Radius(RadiusInfo.newBuilder().setRadius(2).build()))
                .addComponent(Grounded::new)
                .addComponent(() -> new Linger(DurationInfo.newBuilder().setNumberOfTicks(15 * 20).build()));


        ActionEntityBuilder cloudActionEntityBuilder = new ActionEntityBuilder();
        cloudActionEntityBuilder
                .addComponent(() ->
                        new OriginChooser(OffsetInfo.newBuilder()
                                .setDirectionType(OffsetInfo.DirectionType.UP)
                                .setLocationReferenceType(OffsetInfo.LocationReferenceType.IMPACT)
                                .setMagnitude(28)
                                .build())
                )
                .addComponent(() -> new Particle(ParticleInfo.newBuilder()
                        .setParticleName(org.bukkit.Particle.CLOUD.name())
                        .setFillStyle(ParticleInfo.FillStyle.RANDOM)
                        .build())).addComponent(() -> new Radius(RadiusInfo.newBuilder().setRadius(3).build()))
                .addComponent(Grounded::new)
                .addComponent(() -> new Linger(DurationInfo.newBuilder().setNumberOfTicks(15 * 20).build()));


        ActionEntityBuilder splashActionEntityBuilder = new ActionEntityBuilder();
        splashActionEntityBuilder
                .addComponent(() ->
                        new OriginChooser(OffsetInfo.newBuilder()
                                .setDirectionType(OffsetInfo.DirectionType.UP)
                                .setLocationReferenceType(OffsetInfo.LocationReferenceType.IMPACT)
                                .setMagnitude(0)
                                .build())
                )

                .addComponent(() -> new Particle(ParticleInfo.newBuilder()
                        .setParticleName(org.bukkit.Particle.WATER_SPLASH.name())
                        .setFillStyle(ParticleInfo.FillStyle.RANDOM)
                        .build()))
                .addComponent(() -> new Radius(RadiusInfo.newBuilder().setRadius(2).build()))
                .addComponent(() -> new Delay(DurationInfo.newBuilder().setNumberOfTicks(15 * 5).build()))
                .addComponent(() -> {
                    EntityTargetLimit limit = new EntityTargetLimit();
                    limit.limit = 1;
                    return limit;
                })
                .addComponent(Lightning::new)
                .addComponent(Grounded::new)
                .addComponent(() -> new Ignite(IgniteInfo.newBuilder().setStrength(0).build()))
                .addComponent(() -> new Linger(DurationInfo.newBuilder().setNumberOfTicks(15 * 15).build()));

        return Skill.builder()
                .addActionBuilder(rainActionEntityBuilder)
                .addActionBuilder(cloudActionEntityBuilder)
                .addActionBuilder(splashActionEntityBuilder)
                .build();
    }

    private Skill getLavaRain() {
        ActionEntityBuilder rainActionEntityBuilder = new ActionEntityBuilder();

        rainActionEntityBuilder
                .addComponent(() ->
                        new OriginChooser(OffsetInfo.newBuilder()
                                .setDirectionType(OffsetInfo.DirectionType.UP)
                                .setLocationReferenceType(OffsetInfo.LocationReferenceType.IMPACT)
                                .setMagnitude(28)
                                .build())
                )
                .addComponent(Grounded::new)
                .addComponent(() -> new Particle(ParticleInfo.newBuilder()
                        .setFillStyle(ParticleInfo.FillStyle.RANDOM)
                        .setParticleName(org.bukkit.Particle.DRIP_LAVA.name())
                        .build()))
                .addComponent(() -> new Radius(RadiusInfo.newBuilder().setRadius(2).build()))
                .addComponent(() -> new Linger(DurationInfo.newBuilder().setNumberOfTicks(15 * 20).build()));

        ActionEntityBuilder cloudActionEntityBuilder = new ActionEntityBuilder();
        cloudActionEntityBuilder
                .addComponent(() ->
                        new OriginChooser(OffsetInfo.newBuilder()
                                .setDirectionType(OffsetInfo.DirectionType.UP)
                                .setLocationReferenceType(OffsetInfo.LocationReferenceType.IMPACT)
                                .setMagnitude(28)
                                .build())
                )
                .addComponent(Grounded::new)
                .addComponent(() -> new Particle(ParticleInfo.newBuilder()
                        .setParticleName(org.bukkit.Particle.CLOUD.name())
                        .setFillStyle(ParticleInfo.FillStyle.RANDOM)
                        .build()))
                .addComponent(() -> new Radius(RadiusInfo.newBuilder().setRadius(3).build()))
                .addComponent(() -> new Linger(DurationInfo.newBuilder().setNumberOfTicks(15 * 20).build()));


        ActionEntityBuilder splashActionEntityBuilder = new ActionEntityBuilder();
        splashActionEntityBuilder
                .addComponent(() ->
                        new OriginChooser(OffsetInfo.newBuilder()
                                .setDirectionType(OffsetInfo.DirectionType.UP)
                                .setLocationReferenceType(OffsetInfo.LocationReferenceType.IMPACT)
                                .setMagnitude(0)
                                .build())
                )
                .addComponent(Grounded::new)
                .addComponent(() -> new Delay(DurationInfo.newBuilder().setNumberOfTicks(15 * 5).build()))
                .addComponent(() -> new Particle(ParticleInfo.newBuilder()
                        .setParticleName(org.bukkit.Particle.LAVA.name())
                        .setFillStyle(ParticleInfo.FillStyle.RANDOM)
                        .build())).addComponent(() -> new Radius(RadiusInfo.newBuilder().setRadius(2).build()))
                .addComponent(() -> new Ignite(IgniteInfo.newBuilder().setStrength(0).build()))
                .addComponent(() -> new Linger(DurationInfo.newBuilder().setNumberOfTicks(15 * 15).build()));

        return Skill.builder()
                .addActionBuilder(rainActionEntityBuilder)
                .addActionBuilder(cloudActionEntityBuilder)
                .addActionBuilder(splashActionEntityBuilder)
                .build();
    }
}
