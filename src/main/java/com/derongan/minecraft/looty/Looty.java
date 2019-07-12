package com.derongan.minecraft.looty;

import com.badlogic.ashley.core.Engine;
import com.derongan.minecraft.looty.config.ConfigLoader;
import com.derongan.minecraft.looty.item.ItemSkillListener;
import com.derongan.minecraft.looty.registration.ItemRegister;
import com.derongan.minecraft.looty.skill.component.proto.*;
import com.derongan.minecraft.looty.skill.proto.Action;
import com.derongan.minecraft.looty.skill.proto.ItemType;
import com.derongan.minecraft.looty.skill.proto.Skill;
import com.derongan.minecraft.looty.skill.proto.SkillTrigger;
import com.derongan.minecraft.looty.ui.GUIListener;
import com.derongan.minecraft.looty.ui.LootyEditorCommandExecutor;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableSet;
import com.google.protobuf.Any;
import com.google.protobuf.Message;
import org.bukkit.Material;
import org.bukkit.Server;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.logging.Logger;

import static com.google.common.collect.ImmutableList.toImmutableList;

@Singleton
public
class Looty {
    private final ItemSkillListener itemSkillListener;
    private final ItemRegister itemRegistrar;
    private final LootyCommandExecutor lootyCommandExecutor;
    private final LootyEditorCommandExecutor LootyEditorCommandExecutor;
    private final GUIListener guiListener;
    private final LootyPlugin lootyPlugin;
    private final Server server;
    private final Engine engine;
    private final ConfigLoader configLoader;
    private final Logger logger;

    @Inject
    public Looty(ItemSkillListener itemSkillListener,
                 ItemRegister itemRegistrar,
                 LootyCommandExecutor lootyCommandExecutor,
                 LootyEditorCommandExecutor LootyEditorCommandExecutor,
                 GUIListener guiListener, LootyPlugin lootyPlugin,
                 Server server,
                 Engine engine,
                 ConfigLoader configLoader, Logger logger) {
        this.itemSkillListener = itemSkillListener;
        this.itemRegistrar = itemRegistrar;
        this.lootyCommandExecutor = lootyCommandExecutor;
        this.LootyEditorCommandExecutor = LootyEditorCommandExecutor;
        this.guiListener = guiListener;
        this.lootyPlugin = lootyPlugin;
        this.server = server;
        this.engine = engine;
        this.configLoader = configLoader;
        this.logger = logger;
    }

    void onEnable() {
        server.getScheduler().scheduleSyncRepeatingTask(lootyPlugin, () -> engine.update(1), 1, 1);
        server.getPluginManager().registerEvents(itemSkillListener, lootyPlugin);
        server.getPluginManager().registerEvents(guiListener, lootyPlugin);

        configLoader.reload();

        lootyPlugin.getCommand("looty").setExecutor(lootyCommandExecutor);
        lootyPlugin.getCommand("looties").setExecutor(lootyCommandExecutor);
        lootyPlugin.getCommand("lootyreload").setExecutor(lootyCommandExecutor);
        lootyPlugin.getCommand("lootyeditor").setExecutor(LootyEditorCommandExecutor);
        logger.info("Loaded Looty");
    }

    @VisibleForTesting
    public ItemType blazeReap() {
        SkillTrigger normalTrigger = SkillTrigger.newBuilder().addTrigger(SkillTrigger.Trigger.SWING).build();

        return ItemType.newBuilder()
                .setDurability(1)
                .setName("Blaze Reap")
                .addSkill(Skill.newBuilder().addTrigger(normalTrigger).addAction(getExplode()))
                .setMaterial(Material.DIAMOND_PICKAXE.name())
                .build();
    }

    //
//    @VisibleForTesting
//    public ItemType rainStick() {
//        SkillTrigger normalTrigger = SkillTrigger.builder().setHand(Hand.RIGHT).build();
//        SkillTrigger leftTrigger = SkillTrigger.builder()
//                .setHand(Hand.LEFT)
//                .build();
//        SkillTrigger crouchTrigger = SkillTrigger.builder()
//                .setHand(Hand.RIGHT)
//                .addModifier(InputModifier.SNEAKING)
//                .build();
//
//        return ItemType.builder()
//                .setDurability((short) 1)
//                .setItemStack(Material.BLAZE_ROD)
//                .setName("Rain Stick")
//                .setItemRarity(ItemRarity.SECOND_GRADE)
//                .addSkillWithTrigger(normalTrigger, getLavaRain())
//                .addSkillWithTrigger(leftTrigger, getWaterRain())
//                .build();
//    }
//
//    @VisibleForTesting
//    Skill getWaterRain() {
//        ActionEntityBuilder rainActionEntityBuilder = new ActionEntityBuilder();
//
//        rainActionEntityBuilder
//                .addComponent(() ->
//                        new OriginChooser(OriginChooserInfo.newBuilder().setOffset(Offset.newBuilder()
//                                .setDirectionType(DirectionType.UP)
//                                .setLocationReferenceType(LocationReferenceType.IMPACT)
//                                .setMagnitude(28))
//                                .build())
//                )
//
//                .addComponent(() -> new Particle(ParticleInfo.newBuilder()
//                        .setParticleName(org.bukkit.Particle.DRIP_WATER.name())
//                        .setFillStyle(ParticleInfo.FillStyle.RANDOM)
//                        .build())).addComponent(() -> new Radius(RadiusInfo.newBuilder().setRadius(2).build()))
//                .addComponent(() -> new Grounded(GroundedInfo.getDefaultInstance()))
//                .addComponent(() -> new Linger(LingerInfo.newBuilder().setNumberOfTicks(15 * 20).build()));
//
//
//        ActionEntityBuilder cloudActionEntityBuilder = new ActionEntityBuilder();
//        cloudActionEntityBuilder
//                .addComponent(() ->
//                        new OriginChooser(OriginChooserInfo.newBuilder().setOffset(Offset.newBuilder()
//                                .setDirectionType(DirectionType.UP)
//                                .setLocationReferenceType(LocationReferenceType.IMPACT)
//                                .setMagnitude(28))
//                                .build())
//                )
//                .addComponent(() -> new Particle(ParticleInfo.newBuilder()
//                        .setParticleName(org.bukkit.Particle.CLOUD.name())
//                        .setFillStyle(ParticleInfo.FillStyle.RANDOM)
//                        .build())).addComponent(() -> new Radius(RadiusInfo.newBuilder().setRadius(3).build()))
//                .addComponent(() -> new Grounded(GroundedInfo.getDefaultInstance()))
//                .addComponent(() -> new Linger(LingerInfo.newBuilder().setNumberOfTicks(15 * 20).build()));
//
//
//        ActionEntityBuilder splashActionEntityBuilder = new ActionEntityBuilder();
//        splashActionEntityBuilder
//                .addComponent(() ->
//                        new OriginChooser(OriginChooserInfo.newBuilder().setOffset(Offset.newBuilder()
//                                .setDirectionType(DirectionType.UP)
//                                .setLocationReferenceType(LocationReferenceType.IMPACT)
//                                .setMagnitude(0))
//                                .build())
//                )
//
//                .addComponent(() -> new Particle(ParticleInfo.newBuilder()
//                        .setParticleName(org.bukkit.Particle.WATER_SPLASH.name())
//                        .setFillStyle(ParticleInfo.FillStyle.RANDOM)
//                        .build()))
//                .addComponent(() -> new Radius(RadiusInfo.newBuilder().setRadius(2).build()))
//                .addComponent(() -> new Delay(DelayInfo.newBuilder().setNumberOfTicks(15 * 5).build()))
//                .addComponent(() -> new EntityTargetLimit(EntityTargetLimitInfo.newBuilder().setLimit(1).build()))
//                .addComponent(() -> new Lightning(LightningInfo.getDefaultInstance()))
//                .addComponent(() -> new Grounded(GroundedInfo.getDefaultInstance()))
//                .addComponent(() -> new Ignite(IgniteInfo.newBuilder().setStrength(0).build()))
//                .addComponent(() -> new Linger(LingerInfo.newBuilder().setNumberOfTicks(15 * 15).build()));
//
//        return Skill.builder()
//                .addActionBuilder(rainActionEntityBuilder)
//                .addActionBuilder(cloudActionEntityBuilder)
//                .addActionBuilder(splashActionEntityBuilder)
//                .build();
//    }
//
    @VisibleForTesting
    protected Action getExplode() {
        ImmutableSet.Builder<Message> explosionBuilder = new ImmutableSet.Builder<>();

        explosionBuilder.add(RadiusInfo.newBuilder().setRadius(3).build(),
                ParticleInfo.newBuilder()
                        .setFillStyle(ParticleInfo.FillStyle.TARGET)
                        .setParticleName(org.bukkit.Particle.EXPLOSION_HUGE.name())
                        .build(), TargetChooserInfo.newBuilder()
                        .setOffset(Offset.newBuilder()
                                .setDirectionType(DirectionType.HEADING)
                                .setLocationReferenceType(LocationReferenceType.IMPACT)
                                .build())
                        .build(), DamageInfo.newBuilder().setDamage(5).build());

        return Action.newBuilder()
                .addAllComponent(explosionBuilder.build().stream().map(Any::pack).collect(toImmutableList()))
                .build();
    }
//
//
//    private Skill getLavaRain() {
//        ActionEntityBuilder rainActionEntityBuilder = new ActionEntityBuilder();
//
//        rainActionEntityBuilder
//                .addComponent(() ->
//                        new OriginChooser(OriginChooserInfo.newBuilder().setOffset(Offset.newBuilder()
//                                .setDirectionType(DirectionType.UP)
//                                .setLocationReferenceType(LocationReferenceType.IMPACT)
//                                .setMagnitude(28))
//                                .build())
//                )
//                .addComponent(() -> new Grounded(GroundedInfo.getDefaultInstance()))
//                .addComponent(() -> new Particle(ParticleInfo.newBuilder()
//                        .setFillStyle(ParticleInfo.FillStyle.RANDOM)
//                        .setParticleName(org.bukkit.Particle.DRIP_LAVA.name())
//                        .build()))
//                .addComponent(() -> new Radius(RadiusInfo.newBuilder().setRadius(2).build()))
//                .addComponent(() -> new Linger(LingerInfo.newBuilder().setNumberOfTicks(15 * 20).build()));
//
//        ActionEntityBuilder cloudActionEntityBuilder = new ActionEntityBuilder();
//        cloudActionEntityBuilder
//                .addComponent(() ->
//                        new OriginChooser(OriginChooserInfo.newBuilder().setOffset(Offset.newBuilder()
//                                .setDirectionType(DirectionType.UP)
//                                .setLocationReferenceType(LocationReferenceType.IMPACT)
//                                .setMagnitude(28))
//                                .build())
//                )
//                .addComponent(() -> new Grounded(GroundedInfo.getDefaultInstance()))
//                .addComponent(() -> new Particle(ParticleInfo.newBuilder()
//                        .setParticleName(org.bukkit.Particle.CLOUD.name())
//                        .setFillStyle(ParticleInfo.FillStyle.RANDOM)
//                        .build()))
//                .addComponent(() -> new Radius(RadiusInfo.newBuilder().setRadius(3).build()))
//                .addComponent(() -> new Linger(LingerInfo.newBuilder().setNumberOfTicks(15 * 20).build()));
//
//
//        ActionEntityBuilder splashActionEntityBuilder = new ActionEntityBuilder();
//        splashActionEntityBuilder
//                .addComponent(() ->
//                        new OriginChooser(OriginChooserInfo.newBuilder().setOffset(Offset.newBuilder()
//                                .setDirectionType(DirectionType.UP)
//                                .setLocationReferenceType(LocationReferenceType.IMPACT)
//                                .setMagnitude(0))
//                                .build())
//                )
//                .addComponent(() -> new Grounded(GroundedInfo.getDefaultInstance()))
//                .addComponent(() -> new Delay(DelayInfo.newBuilder().setNumberOfTicks(15 * 5).build()))
//                .addComponent(() -> new Particle(ParticleInfo.newBuilder()
//                        .setParticleName(org.bukkit.Particle.LAVA.name())
//                        .setFillStyle(ParticleInfo.FillStyle.RANDOM)
//                        .build())).addComponent(() -> new Radius(RadiusInfo.newBuilder().setRadius(2).build()))
//                .addComponent(() -> new Ignite(IgniteInfo.newBuilder().setStrength(0).build()))
//                .addComponent(() -> new Linger(LingerInfo.newBuilder().setNumberOfTicks(15 * 15).build()));
//
//        return Skill.builder()
//                .addActionBuilder(rainActionEntityBuilder)
//                .addActionBuilder(cloudActionEntityBuilder)
//                .addActionBuilder(splashActionEntityBuilder)
//                .build();
//    }
}
