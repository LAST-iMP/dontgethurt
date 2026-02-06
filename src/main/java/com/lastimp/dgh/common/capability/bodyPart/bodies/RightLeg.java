
package com.lastimp.dgh.common.capability.bodyPart.bodies;

import com.lastimp.dgh.common.capability.bodyPart.base.AbstractLeg;
import com.lastimp.dgh.common.enums.BodyComponents;
import net.minecraft.network.chat.Component;

public class RightLeg extends AbstractLeg {

    @Override
    public String getShortID() {
        return "0054C789-7F24-42A6-95D2";
    }

    @Override
    public BodyComponents getBodyType() {
        return BodyComponents.RIGHT_LEG;
    }

    @Override
    public Component getComponent() {
        return Component.literal("右腿");
    }
}
