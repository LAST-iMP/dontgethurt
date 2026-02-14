package com.lastimp.dgh.compact.iseng.mixin;

import com.lastimp.dgh.common.capability.HealthCapability;
import net.aland.iseng.events.ReturnByDeathHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ReturnByDeathHandler.class)
public class ReturnByDeathHandlerMixin {
    @Inject(method = "saveSnapshot", at = @At("TAIL"))
    private static void saveSnapshot(ServerPlayer player, CallbackInfo ci) {
        if (!HealthCapability.has(player)) return;
        HealthCapability.getAndApply(player, h -> {
            CompoundTag snapshot = player.getPersistentData().getCompound("iseng_rbd_snapshot");
            snapshot.put("dgh_health", h.serialize(player.registryAccess()));
            player.getPersistentData().put("iseng_rbd_snapshot", snapshot);
        });
    }

    @Inject(method = "rollbackIndividualPlayer", at = @At("TAIL"))
    private static void rollbackIndividualPlayer(ServerPlayer player, CompoundTag snapshot, CallbackInfo ci) {
        if (!HealthCapability.has(player)) return;
        HealthCapability.getAndApply(player, h -> {
            h.deserialize(player.registryAccess(), snapshot.getCompound("dgh_health"));
        });
    }
}
