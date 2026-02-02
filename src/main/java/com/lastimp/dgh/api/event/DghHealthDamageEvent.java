package com.lastimp.dgh.api.event;

import net.minecraft.world.damagesource.DamageSource;
import net.neoforged.bus.api.Event;

public class DghHealthDamageEvent extends Event {
    private final DamageSource source;
    private final float sourceDamage;
    private float newDamage;

    public DghHealthDamageEvent(DamageSource source, float sourceDamage, float newDamage) {
        this.source = source;
        this.sourceDamage = sourceDamage;
        this.newDamage = newDamage;
    }

    public float newDamage() {
        return newDamage;
    }

    public void setNewDamage(float newDamage) {
        this.newDamage = newDamage;
    }

    public DamageSource source() {
        return source;
    }

    public float sourceDamage() {
        return sourceDamage;
    }
}
