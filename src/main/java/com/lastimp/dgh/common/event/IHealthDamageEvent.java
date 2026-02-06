package com.lastimp.dgh.common.event;

import net.minecraft.world.damagesource.DamageSource;

public interface IHealthDamageEvent {
    float newDamage();

    void setNewDamage(float newDamage);

    DamageSource source();

    float sourceDamage();
}
