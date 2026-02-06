
package com.lastimp.dgh.common.capability.bodyPart.bodies;

import com.lastimp.dgh.common.capability.bodyPart.base.AbstractLeg;
import com.lastimp.dgh.common.enums.BodyComponents;
import net.minecraft.network.chat.Component;

public class LeftLeg extends AbstractLeg {

    @Override
    public String getShortID() {
        return "left_leg";
    }

    @Override
    public BodyComponents getBodyType() {
        return BodyComponents.LEFT_LEG;
    }

    @Override
    public Component getComponent() {
        return Component.literal("左腿");
    }
}
