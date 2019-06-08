package com.derongan.minecraft.looty.skill.component.effective;

import com.derongan.minecraft.looty.skill.component.Component;
import com.derongan.minecraft.looty.skill.component.proto.ParticleInfo;

public class Particle implements Component<ParticleInfo> {
    private ParticleInfo particleInfo;

    public Particle(ParticleInfo particleInfo) {
        this.particleInfo = particleInfo;
    }

    @Override
    public ParticleInfo getInfo() {
        return particleInfo;
    }

    @Override
    public void setInfo(ParticleInfo info) {

    }
}
