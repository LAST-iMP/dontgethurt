package com.lastimp.dgh.api.bodyPart;

import com.lastimp.dgh.source.core.player.PlayerHealthCapability;

import static com.lastimp.dgh.api.enums.BodyComponents.TORSO;
import static com.lastimp.dgh.api.enums.BodyCondition.*;

public class AbstractArm extends AbstractExtremities{
    public AbstractArm() {
        super();
    }

    public boolean available(PlayerHealthCapability health) {
        boolean available = this.isBandaged() || this.isBadBandaged() || !this.abnormal(DISLOCATION);
        available &= this.abnormal(PLASTER_CAST) || !this.abnormalWithHidden(FRACTURE);
        available |= health.getComponent(TORSO).abnormal(ANALGESIA);
        return available;
    }
}
