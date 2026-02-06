package com.lastimp.dgh.common.item.limbs;

import com.lastimp.dgh.common.item.bases.AbstractLimbs;
import com.lastimp.dgh.common.capability.bodyPart.base.AbstractVisibleBody;
import com.lastimp.dgh.common.enums.BodyComponents;
import com.lastimp.dgh.common.capability.HealthCapability;
import org.jetbrains.annotations.NotNull;

public class HumanHand extends AbstractLimbs {
    public HumanHand(Properties properties) {
        super(properties);
    }

    @Override
    protected void addLimb(@NotNull HealthCapability health, @NotNull AbstractVisibleBody body) {

    }

    @Override
    protected void initComponents() {
        applicableComponents.add(BodyComponents.LEFT_ARM);
        applicableComponents.add(BodyComponents.RIGHT_ARM);
    }
}
