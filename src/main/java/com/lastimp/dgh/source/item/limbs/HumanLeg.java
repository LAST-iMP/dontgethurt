package com.lastimp.dgh.source.item.limbs;

import com.lastimp.dgh.api.healingItems.AbstractLimbs;
import com.lastimp.dgh.source.core.bodyPart.base.AbstractVisibleBody;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import org.jetbrains.annotations.NotNull;

public class HumanLeg extends AbstractLimbs {
    public HumanLeg(Properties properties) {
        super(properties);
    }

    @Override
    protected void addLimb(@NotNull HealthCapability health, @NotNull AbstractVisibleBody body) {

    }

    @Override
    protected void initComponents() {
        applicableComponents.add(BodyComponents.LEFT_LEG);
        applicableComponents.add(BodyComponents.RIGHT_LEG);
    }
}
