package com.lastimp.dgh.common.event;

import com.lastimp.dgh.common.enums.BodyComponents;
import net.minecraft.resources.ResourceLocation;

public interface IComponentDamageEvent {
    float block();

    void setBlocking(float blocking);

    BodyComponents component();

    float damageAmount();

    float resist();

    void setResist(float resist);

    ResourceLocation type();

    void setCanceled(boolean canceled);

    boolean isCanceled();
}
