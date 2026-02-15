package com.lastimp.dgh.mixin.entity;

import com.lastimp.dgh.common.capability.HealthCapability;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {
    @Inject(method = "showEndCredits", at = @At("TAIL"))
    public void showEndCredits(CallbackInfo ci) {
        ServerPlayer self = (ServerPlayer) (Object) this;
        if (!HealthCapability.has(self)) return;

        var data = self.getPersistentData();
        var persistedTag = data.getCompound(Player.PERSISTED_NBT_TAG);
        HealthCapability.getAndApply(self, h ->
                persistedTag.put(HealthCapability.HEALTH_RECORD + "_win", h.serialize(self.registryAccess()))
        );
        data.put(Player.PERSISTED_NBT_TAG, persistedTag);
    }
}
