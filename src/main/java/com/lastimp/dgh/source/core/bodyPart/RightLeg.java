
package com.lastimp.dgh.source.core.bodyPart;

import com.lastimp.dgh.api.bodyPart.AbstractLeg;
import net.minecraft.network.chat.Component;

public class RightLeg extends AbstractLeg {

    @Override
    public String getShortID() {
        return "0054C789-7F24-42A6-95D2";
    }

    @Override
    public Component getComponent() {
        return Component.literal("右腿");
    }
}
