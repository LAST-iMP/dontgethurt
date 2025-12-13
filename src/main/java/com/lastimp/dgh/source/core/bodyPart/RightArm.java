
package com.lastimp.dgh.source.core.bodyPart;

import com.lastimp.dgh.api.bodyPart.AbstractArm;
import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.world.entity.player.Player;

public class RightArm extends AbstractArm {
    public static final String ID = "right_arm";

    public RightArm() {
        super();
    }

    @Override
    public String getShortID() {
        return ID;
    }

    public RightArm(Void unused) {
        this();
    }
}
