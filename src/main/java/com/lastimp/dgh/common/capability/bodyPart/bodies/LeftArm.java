
package com.lastimp.dgh.common.capability.bodyPart.bodies;

import com.lastimp.dgh.common.capability.bodyPart.base.AbstractArm;
import com.lastimp.dgh.common.enums.BodyComponents;
import net.minecraft.network.chat.Component;

public class LeftArm extends AbstractArm {

    @Override
    public String getShortID() {
        return "left_arm";
    }

    @Override
    public BodyComponents getBodyType() {
        return BodyComponents.LEFT_ARM;
    }

    @Override
    public Component getComponent() {
        return Component.literal("左臂");
    }
}
