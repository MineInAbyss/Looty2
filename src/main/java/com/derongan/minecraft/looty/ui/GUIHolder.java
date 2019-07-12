package com.derongan.minecraft.looty.ui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class GUIHolder implements InventoryHolder {
    private Inventory inventory;
    private int numRows;
    private Layout initial;
    private final Plugin plugin;

    public GUIHolder(int numRows, String title, Layout initial, Plugin plugin) {
        this.numRows = numRows;
        this.initial = initial;
        this.plugin = plugin;

        inventory = Bukkit.createInventory(this, 9 * numRows, title);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }


    public void render() {
        inventory.clear();
        initial.draw((x, y, itemStack) -> {
            if (x + 9 * y < numRows * 9) {
                if (itemStack == null) {
                    inventory.clear(x + 9 * y);
                } else {
                    inventory.setItem(x + 9 * y, itemStack);
                }
            }
        });
    }

    private void renderFuture() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this::render);
    }

    public void onPickup(ClickEvent clickEvent) {
        processEvent(clickEvent, initial::onPickup);
    }

    public void onPlace(ClickEvent clickEvent) {
        processEvent(clickEvent, initial::onPlace);
    }

    public void onSwap(ClickEvent clickEvent) {
        processEvent(clickEvent, initial::onSwap);
    }

    private void processEvent(ClickEvent clickEvent, Consumer<ClickEvent> clickEventConsumer) {
        clickEventConsumer.accept(clickEvent);
        renderFuture();
    }

    public void show(Player player) {
        render();
        player.openInventory(inventory);
    }
}
