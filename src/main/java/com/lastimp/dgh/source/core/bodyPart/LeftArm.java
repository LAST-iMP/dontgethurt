
package com.lastimp.dgh.source.core.bodyPart;

import com.lastimp.dgh.api.bodyPart.AbstractArm;
import net.minecraft.network.chat.Component;

public class LeftArm extends AbstractArm {

    @Override
    public String getShortID() {
        return "left_arm";
    }

    @Override
    public Component getComponent() {
        return Component.literal("左臂");
    }
}
