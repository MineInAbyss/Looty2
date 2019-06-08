package com.derongan.minecraft.looty.skill.component.effective;

import com.derongan.minecraft.looty.skill.component.Component;
import com.derongan.minecraft.looty.skill.component.proto.DamageInfo;

public class Damage implements Component<DamageInfo> {
    private DamageInfo damageInfo;

    public Damage(DamageInfo damageInfo) {
        this.damageInfo = damageInfo;
    }

    @Override
    public DamageInfo getInfo() {
        return damageInfo;
    }

    @Override
    public void setInfo(DamageInfo info) {
        damageInfo = info;
    }
}
