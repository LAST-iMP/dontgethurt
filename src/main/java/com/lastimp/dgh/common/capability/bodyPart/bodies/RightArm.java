
package com.lastimp.dgh.common.capability.bodyPart.bodies;

import com.lastimp.dgh.common.capability.bodyPart.base.AbstractArm;
import com.lastimp.dgh.common.enums.BodyComponents;
import net.minecraft.network.chat.Component;

public class RightArm extends AbstractArm {
    public static final String ID = "48E34DED-3A12-4D68-8A69";

    @Override
    public String getShortID() {
        return ID;
    }

    @Override
    public BodyComponents getBodyType() {
        return BodyComponents.RIGHT_ARM;
    }

    @Override
    public Component getComponent() {
        return Component.literal("右臂");
    }
}
