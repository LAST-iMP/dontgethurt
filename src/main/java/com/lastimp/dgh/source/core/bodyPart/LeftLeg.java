
package com.lastimp.dgh.source.core.bodyPart;

import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.bodyPart.AbstractLeg;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.world.entity.player.Player;

public class LeftLeg extends AbstractLeg {
    public LeftLeg() {
        super();
    }

    @Override
    public String getShortID() {
        return "left_leg";
    }

    public LeftLeg(Void unused) {
        this();
    }
}
