package com.derongan.minecraft.looty.component.effective;

import com.derongan.minecraft.looty.ActionEntityBuilder;
import com.derongan.minecraft.looty.component.Component;
import org.bukkit.Material;

import java.util.Collection;

public class Projectile implements Component {
    private final Collection<ActionEntityBuilder> onHitActions;
    private final Material appearance;

    private Projectile(Collection<ActionEntityBuilder> onHitActions, Material appearance) {
        this.onHitActions = onHitActions;
        this.appearance = appearance;
    }

    public Collection<ActionEntityBuilder> getOnHitActions() {
        return onHitActions;
    }

    public Material getAppearance() {
        return appearance;
    }

    public static Projectile create(Collection<ActionEntityBuilder> onHitActions, Material appearance){
        return new Projectile(onHitActions, appearance);
    }
}
