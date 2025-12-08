package com.lastimp.dgh.api.bodyPart;

import com.lastimp.dgh.source.core.player.PlayerHealthCapability;

import static com.lastimp.dgh.api.enums.BodyComponents.TORSO;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;

public class AbstractLeg extends AbstractExtremities{
    public AbstractLeg() {
        super();
    }

    @Override
    public int slowDownLevel(PlayerHealthCapability health) {
        return super.slowDownLevel(health) + (this.available(health)? 0 : 8);
    }
}
