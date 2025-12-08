package com.lastimp.dgh.api.bodyPart;

import com.lastimp.dgh.source.core.player.PlayerHealthCapability;

public class AbstractLeg extends AbstractExtremities{
    public AbstractLeg() {
        super();
    }

    @Override
    public int slowDownLevel(PlayerHealthCapability health) {
        return super.slowDownLevel(health) + (this.available(health)? 0 : 8);
    }
}
