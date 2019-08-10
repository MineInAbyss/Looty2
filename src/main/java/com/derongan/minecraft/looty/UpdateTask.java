package com.derongan.minecraft.looty;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.derongan.minecraft.looty.item.LootyItemDetector;
import com.derongan.minecraft.looty.item.SkillHolder;
import com.derongan.minecraft.looty.registration.ConfigItemIdentifier;
import com.derongan.minecraft.looty.registration.ConfigItemRegister;
import com.derongan.minecraft.looty.registration.NBTItemSkillCache;
import com.derongan.minecraft.looty.skill.SkillUseAggregator;
import com.derongan.minecraft.looty.skill.SkillWrapper;
import com.derongan.minecraft.looty.skill.component.ActionAttributes;
import com.derongan.minecraft.looty.skill.proto.Skill;
import com.derongan.minecraft.looty.skill.proto.SkillTrigger;
import com.derongan.minecraft.looty.skill.systems.particle.ParticleManager;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;

import javax.inject.Inject;
import java.util.*;
import java.util.logging.Logger;

import static com.google.common.collect.ImmutableList.toImmutableList;

public class UpdateTask extends BukkitRunnable {
    private final SkillUseAggregator skillUseAggregator;
    private final Engine engine;
    private final ParticleManager particleManager;
    private final Server server;
    private final ConfigItemRegister configItemRegister;
    private final NBTItemSkillCache nbtItemSkillCache;
    private final LootyItemDetector lootyItemDetector;
    private final Logger logger;
    private Map<UUID, Map<Skill, Integer>> cooldowns;

    @Inject
    public UpdateTask(SkillUseAggregator skillUseAggregator,
                      Engine engine,
                      ParticleManager particleManager,
                      Server server,
                      ConfigItemRegister configItemRegister,
                      NBTItemSkillCache nbtItemSkillCache,
                      LootyItemDetector lootyItemDetector,
                      Logger logger) {
        this.skillUseAggregator = skillUseAggregator;
        this.engine = engine;
        this.particleManager = particleManager;
        this.server = server;
        this.configItemRegister = configItemRegister;
        this.nbtItemSkillCache = nbtItemSkillCache;
        this.lootyItemDetector = lootyItemDetector;
        this.logger = logger;
        cooldowns = new HashMap<>();
    }

    @Override
    public void run() {
        cooldowns.forEach(((uuid, cd) -> cd.entrySet().forEach(a -> a.setValue(a.getValue() - 1))));
        cooldowns.forEach((uuid, cd) ->
                cd.entrySet().removeIf(a -> a.getValue() == 0));

        skillUseAggregator.getEventsTakenThisTick().forEach(this::createActionsForUUID);

        engine.update(1);
        particleManager.update(0);
        skillUseAggregator.reset();
    }


    private void createActionsForUUID(UUID uuid, SkillTrigger.Trigger triggerHint) {
        Player player = server.getPlayer(uuid);

        if (player != null && player.isOnline()) {
            ItemStack mainHandItemStack = player.getInventory().getItemInMainHand();

            Optional<SkillHolder> skillHolder = Optional.empty();

            if (lootyItemDetector.isNBTBasedLootyItem(mainHandItemStack)) {
                try {
                    skillHolder = nbtItemSkillCache.getSkillHolder(mainHandItemStack);
                } catch (NBTItemSkillCache.InvalidSkillNBTException e) {
                    player.sendMessage(ChatColor.LIGHT_PURPLE + "Your item's power has faded... you must imbue it again.");

                    ItemMeta newMeta = Bukkit.getItemFactory().getItemMeta(mainHandItemStack.getType());
                    mainHandItemStack.setItemMeta(newMeta);

                    return;
                }
            } else if (lootyItemDetector.isConfigBasedLootyItem(mainHandItemStack)) {
                skillHolder = configItemRegister.getConfigItemType(ConfigItemIdentifier.fromItemStack(mainHandItemStack));
            }

            if (skillHolder.isPresent()) {
                skillHolder.get()
                        .getSkills()
                        .stream()
                        .filter(skill -> doesPlayerMatchTriggerStateConditions(player, skill.getSkillTriggers()))
                        .forEach(skillWrapper -> {
                            if (isOnCooldown(uuid, skillWrapper)) {
                                return;
                            }

                            ImmutableList<SkillTrigger> validTriggers = skillWrapper.getSkillTriggers().stream()
                                    .filter(skillTrigger -> skillTrigger.getTriggerList().contains(triggerHint))
                                    .collect(toImmutableList());
                            for (SkillTrigger skillTrigger : validTriggers) {

                                double range = skillTrigger.getTarget().getRange();

                                RayTraceResult rayTraceResult = getRayTraceResultOrDefault(player, range);

                                boolean didHitEntity = false;
                                boolean didHitSolidBlock = false;

                                Location hitLocation = rayTraceResult.getHitPosition().toLocation(player.getWorld());

                                ActionAttributes actionAttributes = new ActionAttributes();

                                actionAttributes.initiatorLocation = new DynamicLocationImpl(player.getEyeLocation(), player);
                                actionAttributes.initiatorEntity = player;
                                actionAttributes.referenceHeading = player.getEyeLocation().getDirection();

                                if (rayTraceResult.getHitEntity() != null) {
                                    didHitEntity = true;
                                    actionAttributes.impactEntity = rayTraceResult.getHitEntity();
                                    actionAttributes.impactLocation = new DynamicLocationImpl(actionAttributes.impactEntity
                                            .getLocation(), actionAttributes.impactEntity);
                                } else if (rayTraceResult.getHitBlock() != null) {
                                    didHitSolidBlock = true;

                                    actionAttributes.impactLocation = new DynamicLocationImpl(hitLocation);
                                } else {
                                    actionAttributes.impactLocation = new DynamicLocationImpl(hitLocation);
                                }


                                boolean playerMatchesTriggerTargetConditions = doesPlayerMatchTriggerTargetConditions(didHitEntity, didHitSolidBlock, skillTrigger
                                        .getTarget());
                                if (playerMatchesTriggerTargetConditions) {
                                    cooldowns.putIfAbsent(uuid, new HashMap<>());
                                    cooldowns.get(uuid)
                                            .putIfAbsent(skillWrapper.getSkill(), skillWrapper.getSkill()
                                                    .getCooldown());


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

    private boolean isOnCooldown(UUID uuid, SkillWrapper skillWrapper) {
        return cooldowns.containsKey(uuid) && cooldowns.get(uuid).containsKey(skillWrapper.getSkill());
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

    private RayTraceResult getRayTraceResultOrDefault(Player player, double range) {
        RayTraceResult rayTraceResult = player
                .getWorld()
                .rayTrace(player.getEyeLocation(), player.getEyeLocation()
                        .getDirection(), range, FluidCollisionMode.NEVER, true, .5, (entity -> entity != player));


        if (rayTraceResult == null) {
            Location hitLocation = player.getEyeLocation()
                    .clone()
                    .add(player.getEyeLocation().getDirection().clone().normalize().multiply(range));
            rayTraceResult = new RayTraceResult(hitLocation.toVector());
        }

        return rayTraceResult;
    }
}
