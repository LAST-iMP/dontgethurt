
package com.lastimp.dgh.source.core.bodyPart;

import com.lastimp.dgh.api.bodyPart.AbstractArm;
import net.minecraft.network.chat.Component;

public class RightArm extends AbstractArm {
    public static final String ID = "right_arm";

    @Override
    public String getShortID() {
        return ID;
    }

    @Override
    public Component getComponent() {
        return Component.literal("右臂");
    }
}
