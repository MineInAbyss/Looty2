package com.derongan.minecraft.ui;

import com.derongan.minecraft.looty.LootyPlugin;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.block.data.Directional;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.meta.tags.ItemTagType;

import javax.inject.Inject;

public class GUIListener implements Listener {
    private final LootyPlugin lootyPlugin;

    @Inject
    public GUIListener(LootyPlugin lootyPlugin) {
        this.lootyPlugin = lootyPlugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {
        InventoryHolder holder = inventoryClickEvent.getClickedInventory().getHolder();
        if (holder instanceof GUIHolder) {
            inventoryClickEvent.setCancelled(true);
            ((GUIHolder) holder).onClick(ClickEvent.createClickEvent(inventoryClickEvent));
        }
    }

    @EventHandler
    public void onPlaceCraftingBench(BlockPlaceEvent blockPlaceEvent) {
        if (blockPlaceEvent.getItemInHand().getType() == Material.CRAFTING_TABLE) {
            if (blockPlaceEvent.getItemInHand().getItemMeta() != null && blockPlaceEvent.getItemInHand()
                    .getItemMeta()
                    .getCustomTagContainer()
                    .hasCustomTag(new NamespacedKey(lootyPlugin, "craft"), ItemTagType.BYTE)) {

                Block east = blockPlaceEvent.getBlockPlaced().getRelative(BlockFace.EAST);
                east.setType(Material.RED_WALL_BANNER);
                Directional directional = (Directional) east.getBlockData();
                directional.setFacing(BlockFace.EAST);
                east.setBlockData(directional);

                Banner banner = setBanner((Banner) east.getState());

                banner.update(true);

            }
        }
    }

    private Banner setBanner(Banner banner) {
        banner.addPattern(new Pattern(DyeColor.BLACK, PatternType.SQUARE_TOP_RIGHT));
        banner.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT));
        banner.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE));
        banner.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
        banner.addPattern(new Pattern(DyeColor.BLACK, PatternType.HALF_HORIZONTAL_MIRROR));
        banner.addPattern(new Pattern(DyeColor.BLACK, PatternType.TRIANGLES_TOP));
        return banner;
    }
}
