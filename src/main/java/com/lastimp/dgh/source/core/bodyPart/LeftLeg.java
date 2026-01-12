
package com.lastimp.dgh.source.core.bodyPart;

import com.lastimp.dgh.api.bodyPart.AbstractLeg;
import net.minecraft.network.chat.Component;

public class LeftLeg extends AbstractLeg {

    @Override
    public String getShortID() {
        return "66C09B3B-3483-491F-BB25";
    }

    @Override
    public Component getComponent() {
        return Component.literal("左腿");
    }
}
