package com.lastimp.dgh.mixin.client;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public class PlayerSkinMixin {
    @Inject(method = "getSkinTextureLocation", at = @At("RETURN"), cancellable = true)
    private void useOtherSkin(CallbackInfoReturnable<ResourceLocation> cir) {
        AbstractClientPlayer player = (AbstractClientPlayer) (Object) this;
        var stack = player.getSlot(103).get();
        if (stack.isEmpty()) return;
        if (!stack.is(Items.PLAYER_HEAD)) return;

        var tag = stack.getOrCreateTag();
        if (tag.isEmpty()) return;

        GameProfile profile = NbtUtils.readGameProfile(tag.getCompound("SkullOwner"));
        if (profile != null) {
            cir.setReturnValue(Minecraft.getInstance().getSkinManager().getInsecureSkinLocation(profile));
        }
    }
}
