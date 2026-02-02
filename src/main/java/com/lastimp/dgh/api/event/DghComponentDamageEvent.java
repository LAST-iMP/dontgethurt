package com.lastimp.dgh.api.event;

import com.lastimp.dgh.api.enums.BodyComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class DghComponentDamageEvent extends Event {
    private float blocking;
    private float resist;
    private final float damageAmount;
    private final ResourceLocation type;
    private final BodyComponents component;

    public DghComponentDamageEvent(float blocking, BodyComponents component, float damageAmount, float resist, ResourceLocation type) {
        this.blocking = blocking;
        this.component = component;
        this.damageAmount = damageAmount;
        this.resist = resist;
        this.type = type;
    }

    public float block() {
        return blocking;
    }

    public void setBlocking(float blocking) {
        this.blocking = blocking;
    }

    public BodyComponents component() {
        return component;
    }

    public float damageAmount() {
        return damageAmount;
    }

    public float resist() {
        return resist;
    }

    public void setResist(float resist) {
        this.resist = resist;
    }

    public ResourceLocation type() {
        return type;
    }
}
