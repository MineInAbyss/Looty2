package com.derongan.minecraft.looty.item;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.derongan.minecraft.looty.DynamicLocationImpl;
import com.derongan.minecraft.looty.registration.ItemIdentifier;
import com.derongan.minecraft.looty.registration.ItemRegister;
import com.derongan.minecraft.looty.registration.PlayerSkillRegister;
import com.derongan.minecraft.looty.skill.SkillWrapper;
import com.derongan.minecraft.looty.skill.component.ActionAttributes;
import com.derongan.minecraft.looty.skill.proto.SkillTrigger;
import com.google.common.collect.ImmutableSet;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

@Singleton
public class ItemSkillListener implements Listener {
    private final PlayerSkillRegister playerSkillRegister;
    private final ItemRegister itemRegistrar;
    private final Engine engine;
    private final Logger logger;

    @Inject
    ItemSkillListener(PlayerSkillRegister playerSkillRegister,
                      ItemRegister itemRegistrar,
                      Engine engine,
                      Logger logger) {
        this.playerSkillRegister = playerSkillRegister;
        this.itemRegistrar = itemRegistrar;
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
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (actions.contains(event.getAction())) {
            SkillTrigger.Builder skillTriggerBuilder = SkillTrigger.newBuilder();
            Player player = event.getPlayer();
            if (player.isSneaking()) {
                skillTriggerBuilder.addModifier(SkillTrigger.Modifier.SNEAKING);
            }
            if (player.isSprinting()) {
                skillTriggerBuilder.addModifier(SkillTrigger.Modifier.SPRINTING);
            }

            // TODO this is bork
            if (leftActions.contains(event.getAction())) {
                skillTriggerBuilder.addTrigger(SkillTrigger.Trigger.SWING);
            } else {
                skillTriggerBuilder.addTrigger(SkillTrigger.Trigger.SWING);
            }

            Material material = player.getInventory().getItemInMainHand().getType();
            ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
            short durability = (meta == null) ? 0 : (short) ((Damageable) meta).getDamage();

            Optional<SkillWrapper> skillToUse = itemRegistrar.getSkill(skillTriggerBuilder.build(), ItemIdentifier.builder()
                    .setDurability(durability)
                    .setMaterial(material)
                    .build());

//            if (!skillToUse.isPresent()) {
//                skillToUse = playerSkillRegister.getSkill(player, skillTriggerBuilder.build());
//            }

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

            skillToUse.ifPresent(skill -> {
                event.setCancelled(true);

                skill.getActions().forEach(components -> {
                    Entity entity = new Entity();
                    components.forEach(entity::add);
                    entity.add(actionAttributes);
                    engine.addEntity(entity);
                });
            });
        }
    }

    private RayTraceResult getRayTraceResultOrDefault(Player player) {
        RayTraceResult rayTraceResult = player
                .getWorld()
                .rayTrace(player.getEyeLocation(), player.getEyeLocation()
                        .getDirection(), 5, FluidCollisionMode.NEVER, true, .1, (entity -> entity != player));


        if (rayTraceResult == null) {
            Location hitLocation = player.getEyeLocation()
                    .clone()
                    .add(player.getEyeLocation().getDirection().clone().normalize().multiply(5));
            rayTraceResult = new RayTraceResult(hitLocation.toVector());
        }

        return rayTraceResult;
    }
}
