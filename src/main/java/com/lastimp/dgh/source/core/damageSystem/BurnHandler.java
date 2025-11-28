package com.lastimp.dgh.source.core.damageSystem;

import com.lastimp.dgh.Config;
import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;

import static com.lastimp.dgh.api.enums.BodyCondition.BURN;
import static com.lastimp.dgh.api.enums.BodyCondition.BURN_RES;

public class BurnHandler {
    public static void handle(PlayerHealthCapability health, AbstractBody body, float damageAmount) {
        if (body instanceof AbstractVisibleBody visibleBody)
            damageAmount *= (1.0f - visibleBody.getConditionValue(BURN_RES) * Config.resistance_max);
        body.injury(BURN, damageAmount);
    }
}
