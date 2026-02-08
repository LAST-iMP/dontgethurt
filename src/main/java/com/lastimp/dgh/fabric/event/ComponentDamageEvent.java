package com.lastimp.dgh.fabric.event;

import com.lastimp.dgh.common.enums.BodyComponents;
import com.lastimp.dgh.common.event.ICancelable;
import com.lastimp.dgh.common.event.IComponentDamageEvent;
import net.minecraft.resources.ResourceLocation;

public class ComponentDamageEvent implements IComponentDamageEvent, ICancelable {
    private float blocking;
    private float resist;
    private final float damageAmount;
    private final ResourceLocation type;
    private final BodyComponents component;

    private boolean canceled = false;

    public ComponentDamageEvent(float blocking, BodyComponents component, float damageAmount, float resist, ResourceLocation type) {
        this.blocking = blocking;
        this.component = component;
        this.damageAmount = damageAmount;
        this.resist = resist;
        this.type = type;
    }

    @Override
    public float block() {
        return blocking;
    }

    @Override
    public void setBlocking(float blocking) {
        this.blocking = blocking;
    }

    @Override
    public BodyComponents component() {
        return component;
    }

    @Override
    public float damageAmount() {
        return damageAmount;
    }

    @Override
    public float resist() {
        return resist;
    }

    @Override
    public void setResist(float resist) {
        this.resist = resist;
    }

    @Override
    public ResourceLocation type() {
        return type;
    }

    @Override
    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    @Override
    public boolean isCanceled() {
        return this.canceled;
    }
}
