package com.derongan.minecraft.looty.skill.systems.projectile;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.derongan.minecraft.looty.LootyPlugin;
import com.derongan.minecraft.looty.skill.component.Head;
import com.derongan.minecraft.looty.skill.component.Projectile;
import com.derongan.minecraft.looty.skill.component.Tail;
import com.derongan.minecraft.looty.skill.component.proto.ProjectileInfo;
import com.derongan.minecraft.looty.skill.systems.AbstractDelayAwareIteratingSystem;
import net.minecraft.server.v1_13_R2.EntityTypes;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import javax.inject.Inject;

public class ProjectileSystem extends AbstractDelayAwareIteratingSystem {
    private final LootyPlugin plugin;
    private ComponentMapper<Projectile> projectileComponentMapper = ComponentMapper.getFor(Projectile.class);

    EntityTypes entityTypes;

    @Inject
    public ProjectileSystem(LootyPlugin plugin) {
        super(Family.all(Projectile.class).get());
        this.plugin = plugin;
    }

    @Override
    protected void processFilteredEntity(Entity entity, float deltaTime) {
        Projectile projectile = projectileComponentMapper.get(entity);

        Material material = Material.valueOf(projectile.getInfo().getMaterialName());
        double speed = projectile.getInfo().getSpeed();

        Head head = headComponentMapper.get(entity);
        Tail tail = tailComponentMapper.get(entity);
//
//        WorldServer worldHandle = ((CraftWorld) tail.location.getWorld()).getHandle();
//        BlockPosition blockPosition = new BlockPosition(tail.location.getBlockX(), tail.location.getBlockY(), tail.location
//                .getBlockZ());
//        entityTypes.a(worldHandle, null, null, null, blockPosition, true, false);

        Vector directionVector = head.location.toVector()
                .clone()
                .subtract(tail.location.toVector())
                .normalize()
                .multiply(speed);

        Vector axeOffset = new Vector(0, 1.62, 0);

        ArmorStand axeStand = tail.location.getWorld()
                .spawn(tail.location.clone().subtract(axeOffset), ArmorStand.class);
        ArmorStand hitboxStand = tail.location.getWorld().spawn(tail.location, ArmorStand.class);


        hitboxStand.setMarker(true);
        hitboxStand.setSmall(true);
        hitboxStand.setVisible(false);


        axeStand.setVisible(false);
        axeStand.setMarker(true);
        axeStand.setArms(true);
        axeStand.setItemInHand(new ItemStack(material));
        axeStand.setBasePlate(false);

        axeStand.setCustomName("Hello");


        if (projectile.getInfo().getRotationStyle() == ProjectileInfo.RotationStyle.VERTICAL) {
            // TODO I got this value by trial and error. need to figure out how it actually works.
            axeStand.setRightArmPose(new EulerAngle(0, 0, 0));

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (axeStand.isOnGround()) {
                        axeStand.remove();
                        hitboxStand.remove();
                        this.cancel();
                    }

                    axeStand.teleport(hitboxStand.getLocation().subtract(axeOffset));
                    axeStand.setVelocity(hitboxStand.getVelocity());
                    axeStand.setRightArmPose(axeStand.getRightArmPose().add(.5, 0, 0));
                }
            }.runTaskTimer(plugin, 0, 1);
        }

        hitboxStand.setVelocity(directionVector);
        axeStand.setVelocity(directionVector);
    }
}
