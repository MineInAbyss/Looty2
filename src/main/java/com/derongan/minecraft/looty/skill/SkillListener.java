package com.derongan.minecraft.looty.skill;

import com.derongan.minecraft.looty.item.LootyItemDetector;
import com.derongan.minecraft.looty.skill.proto.SkillTrigger;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import javax.inject.Inject;

/**
 * Listener that manages skill usage
 */
public class SkillListener implements Listener {

    private final LootyItemDetector lootyItemDetector;
    private final SkillUseAggregator skillUseAggregator;

    @Inject
    public SkillListener(LootyItemDetector lootyItemDetector, SkillUseAggregator skillUseAggregator) {
        this.lootyItemDetector = lootyItemDetector;
        this.skillUseAggregator = skillUseAggregator;
    }

    @EventHandler(ignoreCancelled = true)
    private void onPlayerAnimationEvent(PlayerAnimationEvent playerAnimationEvent) {
        Player player = playerAnimationEvent.getPlayer();

        ItemStack mainHandItemStack = player.getInventory().getItemInMainHand();

        if (lootyItemDetector.isLootyItem(mainHandItemStack)) {
            skillUseAggregator.addEvent(player, SkillTrigger.Trigger.LEFT_CLICK);
        }
    }

    @EventHandler(ignoreCancelled = true)
    private void onPlayerInteractEvent(PlayerInteractEvent playerInteractEvent) {
        Player player = playerInteractEvent.getPlayer();
        ItemStack mainHandItemStack = player.getInventory().getItemInMainHand();

        if (lootyItemDetector.isLootyItem(mainHandItemStack)) {
            switch (playerInteractEvent.getAction()) {
                case RIGHT_CLICK_BLOCK:
                case RIGHT_CLICK_AIR:
                    skillUseAggregator.addEvent(player, SkillTrigger.Trigger.RIGHT_CLICK);
                    break;
                case LEFT_CLICK_BLOCK:
                case LEFT_CLICK_AIR:
                    skillUseAggregator.addEvent(player, SkillTrigger.Trigger.LEFT_CLICK);
                    break;
            }
        }
    }

    @EventHandler
    private void onPlayerDamageEntity(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        Entity damager = entityDamageByEntityEvent.getDamager();

        if (damager instanceof Player) {
            ItemStack mainHandItemStack = ((Player) damager).getInventory().getItemInMainHand();

            if (lootyItemDetector.isLootyItem(mainHandItemStack)) {
                entityDamageByEntityEvent.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void onBlockPlaceEvent(BlockPlaceEvent blockPlaceEvent) {
        ItemStack mainHandItemStack = blockPlaceEvent.getItemInHand();

        if (lootyItemDetector.isLootyItem(mainHandItemStack)) {
            blockPlaceEvent.setCancelled(true);
        }
    }

    @EventHandler
    private void onBlockBreakEvent(BlockBreakEvent blockBreakEvent) {
        ItemStack mainHandItemStack = blockBreakEvent.getPlayer().getInventory().getItemInMainHand();

        if (lootyItemDetector.isLootyItem(mainHandItemStack)) {
            blockBreakEvent.setCancelled(true);
        }
    }
}
