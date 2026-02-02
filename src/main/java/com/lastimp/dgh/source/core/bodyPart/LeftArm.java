
package com.lastimp.dgh.source.core.bodyPart;

import com.lastimp.dgh.source.core.bodyPart.base.AbstractArm;
import com.lastimp.dgh.api.enums.BodyComponents;
import net.minecraft.network.chat.Component;

public class LeftArm extends AbstractArm {
    public static final String ID = "48E34DED-3A12-4D68-8A68";

    @Override
    public String getShortID() {
        return ID;
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
