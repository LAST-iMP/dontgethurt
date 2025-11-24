package com.lastimp.dgh.api.bodyPart;

import static com.lastimp.dgh.api.enums.BodyCondition.*;

public class AbstractLeg extends AbstractExtremities{
    public AbstractLeg() {
        super();
    }

    @Override
    public int slowDownLevel() {
        boolean available = this.isBandaged() || this.isBadBandaged() || !this.abnormal(DISLOCATION);
        available &= this.abnormal(PLASTER_CAST) || !this.abnormal(FRACTURE);
        return super.slowDownLevel() + (available? 0 : 4);
    }
}
