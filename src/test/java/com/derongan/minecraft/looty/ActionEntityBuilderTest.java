package com.derongan.minecraft.looty;

import com.badlogic.ashley.core.Entity;
import com.derongan.minecraft.looty.skill.ActionEntityBuilder;
import com.derongan.minecraft.looty.skill.component.effective.Damage;
import com.derongan.minecraft.looty.skill.component.effective.Ignite;
import com.derongan.minecraft.looty.skill.component.proto.DamageInfo;
import com.derongan.minecraft.looty.skill.component.proto.IgniteInfo;
import org.bukkit.util.Vector;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;


public class ActionEntityBuilderTest {
    @Test
    public void testReuasble() {
        ActionEntityBuilder builder = new ActionEntityBuilder();
        Damage damage = new Damage(DamageInfo.newBuilder().setDamage(5).build());
        Ignite ignite = new Ignite(IgniteInfo.newBuilder().setStrength(3).build());

        builder.addComponent(() -> damage);
        Entity entity = builder.build();
        builder.addComponent(() -> ignite);
        Entity entity2 = builder.build();

        assertThat(entity.getComponents()).containsExactly(damage);
        assertThat(entity2.getComponents()).containsExactly(damage, ignite);
    }


    @Test
    public void generic() {
        Vector vector = new Vector(1, 0, 0);

        Vector v2 = new Vector(0, 0, -1);


        System.out.println(v2.angle(vector));
    }

}