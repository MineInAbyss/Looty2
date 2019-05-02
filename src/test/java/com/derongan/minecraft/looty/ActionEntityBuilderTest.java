package com.derongan.minecraft.looty;

import com.badlogic.ashley.core.Entity;
import com.derongan.minecraft.looty.component.effective.Damage;
import com.derongan.minecraft.looty.component.effective.Ignite;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;


public class ActionEntityBuilderTest {
    @Test
    public void testReuasble() {
        ActionEntityBuilder builder = new ActionEntityBuilder();
        Damage damage = Damage.create(5);
        Ignite ignite = Ignite.create(3);

        builder.addComponent(() -> damage);
        Entity entity = builder.build();
        builder.addComponent(() -> ignite);
        Entity entity2 = builder.build();

        assertThat(entity.getComponents()).containsExactly(damage);
        assertThat(entity2.getComponents()).containsExactly(damage, ignite);
    }

}