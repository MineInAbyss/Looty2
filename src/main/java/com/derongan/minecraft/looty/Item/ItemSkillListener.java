package com.derongan.minecraft.looty.Item;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.derongan.minecraft.looty.registration.ItemIdentifier;
import com.derongan.minecraft.looty.registration.ItemRegistrar;
import com.derongan.minecraft.looty.registration.PlayerSkillRegistrar;
import com.derongan.minecraft.looty.skill.Hand;
import com.derongan.minecraft.looty.skill.InputModifier;
import com.derongan.minecraft.looty.skill.Skill;
import com.derongan.minecraft.looty.skill.SkillTrigger;
import com.derongan.minecraft.looty.skill.component.internal.TargetInfo;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.RayTraceResult;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;
import java.util.logging.Logger;

@Singleton
public class ItemSkillListener implements Listener {
    private final PlayerSkillRegistrar playerSkillRegistrar;
    private final ItemRegistrar itemRegistrar;
    private final Engine engine;
    private final Logger logger;

    @Inject
    ItemSkillListener(PlayerSkillRegistrar playerSkillRegistrar,
                      ItemRegistrar itemRegistrar,
                      Engine engine,
                      Logger logger) {
        this.playerSkillRegistrar = playerSkillRegistrar;
        this.itemRegistrar = itemRegistrar;
        this.engine = engine;
        this.logger = logger;
    }

    @EventHandler
    public void onPlayerAnimationEvent(PlayerAnimationEvent event) {
        if (event.getAnimationType() == PlayerAnimationType.ARM_SWING) {
            SkillTrigger.Builder skillTriggerBuilder = SkillTrigger.builder();
            Player player = event.getPlayer();
            if (player.isSneaking()) {
                skillTriggerBuilder.addModifier(InputModifier.SNEAKING);
            }
            if (player.isSprinting()) {
                skillTriggerBuilder.addModifier(InputModifier.SPRINTING);
            }

            skillTriggerBuilder.setHand(Hand.RIGHT);

            Material material = player.getInventory().getItemInMainHand().getType();
            ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
            short durability = (meta == null) ? 0 : (short) ((Damageable) meta).getDamage();

            Optional<Skill> skillToUse = itemRegistrar.getSkill(skillTriggerBuilder.build(), ItemIdentifier.builder()
                    .setDurability(durability)
                    .setMaterial(material)
                    .build());

            if (!skillToUse.isPresent()) {
                skillToUse = playerSkillRegistrar.getSkill(player, skillTriggerBuilder.build());
            }

            RayTraceResult rayTraceResult = player
                    .getWorld()
                    .rayTrace(player.getEyeLocation(), player.getEyeLocation()
                            .getDirection(), 5, FluidCollisionMode.NEVER, true, .1, (entity -> entity != player));


            TargetInfo.Builder targetInfoBuilder = TargetInfo.builder()
                    .setInitiator(player)
                    .setOrigin(player);

            if (rayTraceResult != null) {
                if (rayTraceResult.getHitEntity() != null) {
                    targetInfoBuilder.setTargetEntity(rayTraceResult.getHitEntity());
                }
                targetInfoBuilder.setTargetLocation(rayTraceResult.getHitPosition().toLocation(player.getWorld()));
            } else {
                targetInfoBuilder.setTargetLocation(player.getEyeLocation()
                        .clone()
                        .add(player.getEyeLocation().getDirection().clone().normalize().multiply(5)));
            }

            skillToUse.ifPresent(skill -> {
                event.setCancelled(true);

                skill.getActionEntityBuilders().forEach(actionBuilder -> {
                    Entity entity = actionBuilder.build();
                    entity.add(targetInfoBuilder.build());
                    engine.addEntity(entity);
                });
            });
        }
    }
}
