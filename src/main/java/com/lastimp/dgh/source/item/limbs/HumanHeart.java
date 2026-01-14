package com.lastimp.dgh.source.item.limbs;

import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import com.lastimp.dgh.api.healingItems.AbstractLimbs;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import org.jetbrains.annotations.NotNull;

public class HumanHeart extends AbstractLimbs {

    public HumanHeart(Properties properties) {
        super(properties);
    }

    @Override
    protected void addLimb(@NotNull HealthCapability health, @NotNull AbstractVisibleBody body) {

    }
}
