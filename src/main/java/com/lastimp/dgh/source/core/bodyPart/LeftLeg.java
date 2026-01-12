
package com.lastimp.dgh.source.core.bodyPart;

import com.lastimp.dgh.api.bodyPart.AbstractLeg;
import net.minecraft.network.chat.Component;

public class LeftLeg extends AbstractLeg {

    @Override
    public String getShortID() {
        return "left_leg";
    }

    @Override
    public Component getComponent() {
        return Component.literal("左腿");
    }
}
