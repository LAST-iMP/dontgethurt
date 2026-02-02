package com.lastimp.dgh.source.item.limbs;

import com.lastimp.dgh.source.core.bodyPart.base.AbstractVisibleBody;
import com.lastimp.dgh.api.healingItems.AbstractLimbs;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import org.jetbrains.annotations.NotNull;

public class HumanLung extends AbstractLimbs {

    public HumanLung(Properties properties) {
        super(properties);
    }

    @Override
    protected void addLimb(@NotNull HealthCapability health, @NotNull AbstractVisibleBody body) {

    }
}
