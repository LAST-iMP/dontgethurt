package com.lastimp.dgh.fabric.mixin;

import com.lastimp.dgh.common.item.bases.AbstractSmallBag;
import com.lastimp.dgh.fabric.capability.BagHolder;
import com.lastimp.dgh.fabric.capability.provider.BagItemInventoryProvider;
import com.lastimp.dgh.fabric.container.BackpackInventoryNF;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin implements BagHolder {
    @Unique
    private BagItemInventoryProvider dgh$bagProvider;

    private void tryinit() {
        ItemStack self = (ItemStack)(Object)this;
        if (self.getItem() instanceof AbstractSmallBag bag) {
            var inv = new BackpackInventoryNF(9);
            bag.initBag(inv);
            this.dgh$bagProvider = new BagItemInventoryProvider(inv, self);
        }
    }

    @Inject(method = "<init>(Lnet/minecraft/world/level/ItemLike;I)V", at = @At("TAIL"))
    private void dgh$initProvider(CallbackInfo ci) {
        this.tryinit();
    }

    @Inject(method = "<init>(Lnet/minecraft/nbt/CompoundTag;)V", at = @At("TAIL"))
    private void dgh$initProvider(CompoundTag compoundTag, CallbackInfo ci) {
        this.tryinit();
    }

    @Inject(method="copy", at=@At("RETURN"))
    private void dgh$copy(CallbackInfoReturnable<ItemStack> cir) {
        ItemStack newStack = cir.getReturnValue();
        if (newStack.getItem() instanceof AbstractSmallBag bag) {
            var inv = new BackpackInventoryNF(9);
            bag.initBag(inv);
            this.dgh$bagProvider = new BagItemInventoryProvider(inv, newStack);
        }
    }

    @Override
    public BagItemInventoryProvider dgh$getBagProvider() {
        return dgh$bagProvider;
    }
}
