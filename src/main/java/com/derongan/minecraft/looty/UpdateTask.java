package com.derongan.minecraft.looty;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.derongan.minecraft.looty.item.CompoundSkillHolderExtractor;
import com.derongan.minecraft.looty.item.SkillHolder;
import com.derongan.minecraft.looty.location.DynamicLocationImpl;
import com.derongan.minecraft.looty.skill.SkillUseAggregator;
import com.derongan.minecraft.looty.skill.component.components.ActionAttributes;
import com.derongan.minecraft.looty.skill.cooldown.CooldownManager;
import com.derongan.minecraft.looty.skill.proto.SkillTrigger;
import com.derongan.minecraft.looty.skill.systems.particle.ParticleManager;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

import static com.google.common.collect.ImmutableList.toImmutableList;

public class UpdateTask extends BukkitRunnable {
    private final SkillUseAggregator skillUseAggregator;
    private final Engine engine;
    private final ParticleManager particleManager;
    private final Server server;
    private final CompoundSkillHolderExtractor compoundSkillHolderExtractor;
    private final CooldownManager cooldownManager;
    private final Logger logger;

    @Inject
    public UpdateTask(SkillUseAggregator skillUseAggregator,
                      Engine engine,
                      ParticleManager particleManager,
                      Server server,
                      CompoundSkillHolderExtractor compoundSkillHolderExtractor,
                      CooldownManager cooldownManager,
                      Logger logger) {
        this.skillUseAggregator = skillUseAggregator;
        this.engine = engine;
        this.particleManager = particleManager;
        this.server = server;
        this.compoundSkillHolderExtractor = compoundSkillHolderExtractor;
        this.cooldownManager = cooldownManager;
        this.logger = logger;
    }

    @Override
    public void run() {
        skillUseAggregator.getEventsTakenThisTick().forEach(this::createActionsForUUID);

        engine.update(1);
        particleManager.update(0);
        skillUseAggregator.reset();
    }


    private void createActionsForUUID(UUID uuid, SkillTrigger.Trigger triggerHint) {
        Player player = server.getPlayer(uuid);

        if (player != null && player.isOnline()) {
            ItemStack mainHandItemStack = player.getInventory().getItemInMainHand();

            Optional<SkillHolder> skillHolder = compoundSkillHolderExtractor.getSkillHolder(mainHandItemStack);

            if (skillHolder.isPresent()) {
                skillHolder.get()
                        .getSkills()
                        .stream()
                        .filter(skill -> doesPlayerMatchTriggerStateConditions(player, skill.getSkillTriggers()))
                        .forEach(skillWrapper -> {
                            if (cooldownManager.isOnCooldown(player, skillWrapper.getSkill())) {
                                return;
                            }

                            ImmutableList<SkillTrigger> validTriggers = skillWrapper.getSkillTriggers().stream()
                                    .filter(skillTrigger -> skillTrigger.getTriggerList().contains(triggerHint))
                                    .collect(toImmutableList());
                            for (SkillTrigger skillTrigger : validTriggers) {

                                double range = skillTrigger.getTarget().getRange();

                                RayTraceResult rayTraceResult = RayTraceUtil.getRayTraceResultOrDefault(player, range);

                                boolean didHitEntity = false;
                                boolean didHitSolidBlock = false;

                                Location hitLocation = rayTraceResult.getHitPosition().toLocation(player.getWorld());

                                ActionAttributes actionAttributes = new ActionAttributes();

                                actionAttributes.initiatorLocation = new DynamicLocationImpl(player.getEyeLocation());
                                actionAttributes.initiatorEntity = player;
                                actionAttributes.referenceHeading = player.getEyeLocation().getDirection();

                                if (rayTraceResult.getHitEntity() != null) {
                                    didHitEntity = true;
                                    actionAttributes.impactEntity = rayTraceResult.getHitEntity();
                                    actionAttributes.impactLocation = new DynamicLocationImpl(actionAttributes.impactEntity
                                            .getLocation());
                                } else if (rayTraceResult.getHitBlock() != null) {
                                    didHitSolidBlock = true;

                                    actionAttributes.impactLocation = new DynamicLocationImpl(hitLocation);
                                } else {
                                    actionAttributes.impactLocation = new DynamicLocationImpl(hitLocation);
                                }


                                boolean playerMatchesTriggerTargetConditions = doesPlayerMatchTriggerTargetConditions(didHitEntity, didHitSolidBlock, skillTrigger
                                        .getTarget());
                                if (playerMatchesTriggerTargetConditions) {
                                    cooldownManager.startCooldown(player, skillWrapper.getSkill());

                                    skillWrapper.getActions().forEach(components -> {
                                        Entity entity = new Entity();
                                        entity.add(actionAttributes);
                                        components.forEach(entity::add);

                                        engine.addEntity(entity);
                                    });

                                    break;
                                }
                            }
                        });

            } else {
                logger.warning(String.format("Player %s was not holding an item when they should have been", player.getName()));
            }
        }
    }

    private boolean doesPlayerMatchTriggerStateConditions(Player player, List<SkillTrigger> skillTriggers) {
        ImmutableSet.Builder<SkillTrigger.Modifier> actualModifierBuilder = ImmutableSet.builder();

        if (player.isSneaking()) {
            actualModifierBuilder.add(SkillTrigger.Modifier.SNEAKING);
        }

        if (player.isSprinting()) {
            actualModifierBuilder.add(SkillTrigger.Modifier.SPRINTING);
        }

        ImmutableSet<SkillTrigger.Modifier> actualModifiers = actualModifierBuilder.build();
        return skillTriggers.stream().map(SkillTrigger::getModifierList).map(ImmutableSet::copyOf)
                .anyMatch(actualModifiers::equals);
    }

    private boolean doesPlayerMatchTriggerTargetConditions(boolean didHitEntity,
                                                           boolean didHitSolidBlock,
                                                           SkillTrigger.SkillTarget skillTarget) {
        ImmutableSet.Builder<SkillTrigger.SkillTarget.TargetType> actualTargetsBuilder = ImmutableSet.builder();

        if (didHitEntity) {
            actualTargetsBuilder.add(SkillTrigger.SkillTarget.TargetType.ENTITY);
        }
        if (didHitSolidBlock) {
            actualTargetsBuilder.add(SkillTrigger.SkillTarget.TargetType.BLOCK);
        } else {
            actualTargetsBuilder.add(SkillTrigger.SkillTarget.TargetType.AIR);
        }

        ImmutableSet<SkillTrigger.SkillTarget.TargetType> actualTargets = actualTargetsBuilder.build();

        return !Sets.intersection(ImmutableSet.copyOf(skillTarget.getTargetTypeList()), actualTargets).isEmpty();
    }
}
