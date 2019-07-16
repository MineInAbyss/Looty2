package com.derongan.minecraft.looty.item;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.derongan.minecraft.looty.DynamicLocationImpl;
import com.derongan.minecraft.looty.registration.ItemIdentifier;
import com.derongan.minecraft.looty.registration.ItemRegister;
import com.derongan.minecraft.looty.registration.PlayerSkillRegister;
import com.derongan.minecraft.looty.registration.SingleItemSkillRegister;
import com.derongan.minecraft.looty.skill.SkillWrapper;
import com.derongan.minecraft.looty.skill.component.ActionAttributes;
import com.derongan.minecraft.looty.skill.proto.SkillTrigger;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

@Singleton
public class ItemSkillListener implements Listener {
    private final PlayerSkillRegister playerSkillRegister;
    private final ItemRegister itemRegistrar;
    private final SingleItemSkillRegister singleItemSkillRegister;
    private final Engine engine;
    private final Logger logger;

    @Inject
    ItemSkillListener(PlayerSkillRegister playerSkillRegister,
                      ItemRegister itemRegistrar,
                      SingleItemSkillRegister singleItemSkillRegister,
                      Engine engine,
                      Logger logger) {
        this.playerSkillRegister = playerSkillRegister;
        this.itemRegistrar = itemRegistrar;
        this.singleItemSkillRegister = singleItemSkillRegister;
        this.engine = engine;
        this.logger = logger;
    }

    private static Set<Action> leftActions = ImmutableSet.of(Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK);
    private static Set<Action> rightActions = ImmutableSet.of(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK);
    private static Set<Action> actions = ImmutableSet.<Action>builder().addAll(leftActions)
            .addAll(rightActions)
            .build();

    // TODO this doesn't work when a player punches a block in adventure mode :(
    @EventHandler
    public void onPlayerAnimationEvent(PlayerAnimationEvent event) {
        SkillTrigger.Builder skillTriggerBuilder = SkillTrigger.newBuilder();
        Player player = event.getPlayer();
        if (player.isSneaking()) {
            skillTriggerBuilder.addModifier(SkillTrigger.Modifier.SNEAKING);
        }
        if (player.isSprinting()) {
            skillTriggerBuilder.addModifier(SkillTrigger.Modifier.SPRINTING);
        }

        skillTriggerBuilder.addTrigger(SkillTrigger.Trigger.SWING);

        SkillTrigger trigger = skillTriggerBuilder.build();

        Collection<SkillWrapper> skillsToUse = getSkillWrappers(player, trigger);

        ActionAttributes actionAttributes = new ActionAttributes();

        actionAttributes.initiatorLocation = new DynamicLocationImpl(player.getEyeLocation(), player);
        actionAttributes.initiatorEntity = player;
        actionAttributes.referenceHeading = player.getEyeLocation().getDirection();

        RayTraceResult rayTraceResult = getRayTraceResultOrDefault(player);

        Vector hitPosition = rayTraceResult.getHitPosition();
        Location hitLocation = hitPosition.toLocation(player.getWorld());
        if (rayTraceResult.getHitEntity() != null) {
            org.bukkit.entity.Entity hitEntity = rayTraceResult.getHitEntity();
            actionAttributes.impactEntity = hitEntity;
            actionAttributes.impactLocation = new DynamicLocationImpl(hitLocation, hitEntity);
        } else {
            actionAttributes.impactLocation = new DynamicLocationImpl(hitLocation);
        }

        skillsToUse.forEach(skill -> {
            event.setCancelled(true);

            skill.getActions().forEach(components -> {
                Entity entity = new Entity();
                components.forEach(entity::add);
                entity.add(actionAttributes);
                engine.addEntity(entity);
            });
        });
    }

    @NotNull
    private Collection<SkillWrapper> getSkillWrappers(Player player, SkillTrigger trigger) {
        Collection<SkillWrapper> skillsToUse = singleItemSkillRegister.getSkill(trigger, player.getInventory()
                .getItemInMainHand());

        if (skillsToUse.isEmpty()) {

            Material material = player.getInventory().getItemInMainHand().getType();
            ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
            short durability = (meta == null) ? 0 : (short) ((Damageable) meta).getDamage();

            Optional<SkillWrapper> skillToUse = itemRegistrar.getSkill(trigger, ItemIdentifier.builder()
                    .setDurability(durability)
                    .setMaterial(material)
                    .build());

            if (skillToUse.isPresent()) {
                skillsToUse = ImmutableList.of(skillToUse.get());
            }
        }
        return skillsToUse;
    }

    @EventHandler
    public void on(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        if (entityDamageByEntityEvent.getDamager() instanceof Player) {

            SkillTrigger.Builder skillTriggerBuilder = SkillTrigger.newBuilder();
            Player player = (Player) entityDamageByEntityEvent.getDamager();
            if (player.isSneaking()) {
                skillTriggerBuilder.addModifier(SkillTrigger.Modifier.SNEAKING);
            }
            if (player.isSprinting()) {
                skillTriggerBuilder.addModifier(SkillTrigger.Modifier.SPRINTING);
            }

            skillTriggerBuilder.addTrigger(SkillTrigger.Trigger.SWING);

            SkillTrigger trigger = skillTriggerBuilder.build();

            if (!getSkillWrappers(player, trigger).isEmpty()) {
                entityDamageByEntityEvent.setCancelled(true);
            }
        }
    }

    private RayTraceResult getRayTraceResultOrDefault(Player player) {
        RayTraceResult rayTraceResult = player
                .getWorld()
                .rayTrace(player.getEyeLocation(), player.getEyeLocation()
                        .getDirection(), 6, FluidCollisionMode.NEVER, true, .5, (entity -> entity != player));


        if (rayTraceResult == null) {
            Location hitLocation = player.getEyeLocation()
                    .clone()
                    .add(player.getEyeLocation().getDirection().clone().normalize().multiply(5));
            rayTraceResult = new RayTraceResult(hitLocation.toVector());
        }

        return rayTraceResult;
    }
}
