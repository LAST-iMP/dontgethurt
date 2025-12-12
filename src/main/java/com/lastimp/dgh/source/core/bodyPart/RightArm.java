
package com.lastimp.dgh.source.core.bodyPart;

import com.lastimp.dgh.api.bodyPart.AbstractArm;
import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import net.minecraft.world.entity.player.Player;

public class RightArm extends AbstractArm {
    public static final String ID = "48E34DED-3A12-4D68-8A69";

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

    @Override
    public AbstractBody update(PlayerHealthCapability health, Player player) {
        return super.update(health, player);
    }
}
