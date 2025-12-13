
package com.lastimp.dgh.source.core.bodyPart;

import com.lastimp.dgh.api.bodyPart.AbstractArm;
import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.world.entity.player.Player;

public class LeftArm extends AbstractArm {
    public static final String ID = "48E34DED-3A12-4D68-8A68";

    public LeftArm() {
        super();
    }

    @Override
    public String getShortID() {
        return ID;
    }

    public LeftArm(Void unused) {
        this();
    }

    @Override
    public AbstractBody update(HealthCapability health, Player player) {
        return super.update(health, player);
    }
}
