
package com.lastimp.dgh.source.core.bodyPart;

import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.bodyPart.AbstractLeg;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.world.entity.player.Player;

public class RightLeg extends AbstractLeg {
    public RightLeg() {
        super();
    }

    @Override
    public String getShortID() {
        return "0054C789-7F24-42A6-95D2";
    }

    public RightLeg(Void unused) {
        this();
    }
}
