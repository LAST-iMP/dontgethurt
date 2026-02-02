
package com.lastimp.dgh.source.core.bodyPart;

import com.lastimp.dgh.api.bodyPart.AbstractArm;
import com.lastimp.dgh.api.enums.BodyComponents;
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
