
package com.lastimp.dgh.source.core.bodyPart;

import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.bodyPart.AbstractExtremities;
import com.lastimp.dgh.api.bodyPart.AbstractLeg;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import net.minecraft.world.entity.player.Player;

public class RightLeg extends AbstractLeg {
    public RightLeg() {
        super();
    }

    public RightLeg(Void unused) {
        this();
    }

    @Override
    public AbstractBody update(PlayerHealthCapability health, Player player) {
        return super.update(health, player);
    }
}
