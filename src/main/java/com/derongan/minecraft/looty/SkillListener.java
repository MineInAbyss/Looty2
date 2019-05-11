package com.derongan.minecraft.looty;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.derongan.minecraft.looty.component.internal.TargetHistory;
import com.derongan.minecraft.looty.component.internal.TargetInfo;
import com.derongan.minecraft.looty.registration.ItemIdentifier;
import com.derongan.minecraft.looty.registration.ItemRegistrar;
import com.derongan.minecraft.looty.registration.PlayerSkillRegistrar;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
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
class SkillListener implements Listener {
    private final PlayerSkillRegistrar playerSkillRegistrar;
    private final ItemRegistrar itemRegistrar;
    private final Engine engine;
    private final Logger logger;

    @Inject
    SkillListener(PlayerSkillRegistrar playerSkillRegistrar, ItemRegistrar itemRegistrar, Engine engine, Logger logger) {
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


            if (rayTraceResult != null && rayTraceResult.getHitEntity() != null) {
                TargetInfo.Builder targetInfoBuilder = TargetInfo.builder()
                        .setEntityTargetHistory(TargetHistory.<org.bukkit.entity.Entity>builder()
                                .setCurrentTarget(rayTraceResult.getHitEntity())
                                .setInitialTargetOrSource(player)
                                .setLastTargetOrSource(player)
                                .build())
                        .setLocationTargetHistory(TargetHistory.<Location>builder().build());

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
}
