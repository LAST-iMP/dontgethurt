package com.lastimp.dgh.common.client.eventHandler;

import com.lastimp.dgh.common.PlatformService;
import com.lastimp.dgh.common.capability.HealthCapability;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;

public class ClientTickEventHandler {
    public static int ABNORMAL_DELAY = 0;

    public static void playerTick(Player player) {
        if (HealthCapability.isDown(player) || HealthCapability.isFootLostDown(player))
            player.setPose(Pose.SWIMMING);
        HealthCapability.getAndApply(player, h-> {
            ABNORMAL_DELAY = h.abnormal() ? PlatformService.CONFIG.SMALL_CONDITION_DISAPPEAR_DELAY() * 20 : Math.max(0, ABNORMAL_DELAY - 1);
        });
    }
}
