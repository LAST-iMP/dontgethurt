package com.lastimp.dgh.fabric.mixin.client;

import com.lastimp.dgh.fabric.client.event.ScreenCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @ModifyVariable(method = "setScreen", at = @At(value = "LOAD", ordinal = 0), argsOnly = true)
    private Screen dgh$setScreen(Screen value) {
        var success = ScreenCallback.Opening.EVENT.invoker().interact(value, Minecraft.getInstance().screen);
        return success ? value : Minecraft.getInstance().screen;
    }
}
