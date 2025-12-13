
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
        return "66C09B3B-3483-491F-BB25";
    }

    public LeftLeg(Void unused) {
        this();
    }

    @Override
    public AbstractBody update(HealthCapability health, Player player) {
        return super.update(health, player);
    }
}
