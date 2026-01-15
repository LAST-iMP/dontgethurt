package com.lastimp.dgh.mixin.client;

import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin {
    @Inject(at = @At("HEAD"), method = "updatePlayerPose", cancellable = true)
    private void incapacitatedUpdatePlayerPose(CallbackInfo ci){
        Player player = (Player)(Object)this;
        if (HealthCapability.has(player) && HealthCapability.isDying(player)) {
            if (player.getVehicle() != null) return;
            ci.cancel();
        }
    }

    @Inject(method = "getName", at = @At("HEAD"), cancellable = true)
    private void changeName(CallbackInfoReturnable<Component> cir) {
        Player player = (Player)(Object)this;
        var stack = player.getSlot(103).get();
        if (stack.isEmpty()) return;
        if (!stack.is(Items.PLAYER_HEAD)) return;

        var tag = stack.getOrCreateTag();
        if (tag.isEmpty()) return;

        GameProfile profile = NbtUtils.readGameProfile(tag.getCompound("SkullOwner"));
        if (profile != null) {
            cir.setReturnValue(Component.nullToEmpty(profile.getName()));
        }
    }
}

