package com.lastimp.dgh.fabric.event;

import com.lastimp.dgh.common.event.IHealthDamageEvent;
import net.minecraft.world.damagesource.DamageSource;

public class HealthDamageEvent implements IHealthDamageEvent {
    private final DamageSource source;
    private final float sourceDamage;
    private float newDamage;

    public HealthDamageEvent(DamageSource source, float sourceDamage, float newDamage) {
        this.source = source;
        this.sourceDamage = sourceDamage;
        this.newDamage = newDamage;
    }

    @Override
    public float newDamage() {
        return newDamage;
    }

    @Override
    public void setNewDamage(float newDamage) {
        this.newDamage = newDamage;
    }

    @Override
    public DamageSource source() {
        return source;
    }

    @Override
    public float sourceDamage() {
        return sourceDamage;
    }
}
