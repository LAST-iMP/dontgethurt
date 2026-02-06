package com.lastimp.dgh.common.client.eventHandler;

import com.lastimp.dgh.common.capability.HealthCapability;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;

public class ClientTickEventHandler {
    public static void playerTick(Player player) {
        if (HealthCapability.isDown(player) || HealthCapability.isFootLostDown(player))
            player.setPose(Pose.SWIMMING);
    }
}
