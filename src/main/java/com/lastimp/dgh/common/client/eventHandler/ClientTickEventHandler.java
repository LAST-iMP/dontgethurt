package com.lastimp.dgh.common.client.eventHandler;

import com.lastimp.dgh.common.PlatformService;
import com.lastimp.dgh.common.capability.HealthCapability;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.NbtUtils;
import com.mojang.authlib.GameProfile;
import net.minecraft.world.item.Items;

public class ClientTickEventHandler {
    public static int ABNORMAL_DELAY = 0;

    public static void playerTick(Player player) {
        if (HealthCapability.isDown(player) || HealthCapability.isFootLostDown(player))
            player.setPose(Pose.SWIMMING);
        else {
            // ensure pose restored when not downed
            if (player.getPose() == Pose.SWIMMING) {
                player.setPose(Pose.STANDING);
            }
        }
        // handle skull owner display name (client-side temporary)
        try {
            var stack = player.getSlot(103).get();
            if (!stack.isEmpty() && stack.is(Items.PLAYER_HEAD)) {
                var tag = stack.getOrCreateTag();
                if (!tag.isEmpty()) {
                    GameProfile profile = NbtUtils.readGameProfile(tag.getCompound("SkullOwner"));
                    if (profile != null && profile.getName() != null) {
                        player.setCustomName(Component.nullToEmpty(profile.getName()));
                        player.setCustomNameVisible(false);
                    }
                }
            } else {
                // clear any temporary custom name
                if (player.getCustomName() != null) {
                    player.setCustomName(null);
                }
            }
        } catch (Throwable ignored) {}
        HealthCapability.getAndApply(player, h-> {
            ABNORMAL_DELAY = h.abnormal() ? PlatformService.CONFIG.SMALL_CONDITION_DISAPPEAR_DELAY() * 20 : Math.max(0, ABNORMAL_DELAY - 1);
        });
    }
}
