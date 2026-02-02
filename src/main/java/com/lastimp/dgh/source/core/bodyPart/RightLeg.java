
package com.lastimp.dgh.source.core.bodyPart;

import com.lastimp.dgh.source.core.bodyPart.base.AbstractLeg;
import com.lastimp.dgh.api.enums.BodyComponents;
import net.minecraft.network.chat.Component;

public class RightLeg extends AbstractLeg {

    @Override
    public String getShortID() {
        return "right_leg";
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
