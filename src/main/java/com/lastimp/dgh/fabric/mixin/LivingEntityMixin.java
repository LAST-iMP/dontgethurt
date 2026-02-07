package com.lastimp.dgh.fabric.mixin;

import com.lastimp.dgh.common.capability.HealthCapability;
import com.lastimp.dgh.fabric.capability.HealthHolder;
import com.lastimp.dgh.fabric.capability.provider.HealthProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements HealthHolder {
    @Unique
    private HealthProvider dgh$healthProvider;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void dgh$initProvider(CallbackInfo ci) {

        LivingEntity self = (LivingEntity)(Object)this;

        if (HealthCapability.has(self)) {
            this.dgh$healthProvider = new HealthProvider();
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void loadHealth(CompoundTag compoundTag, CallbackInfo ci) {
        if (this.dgh$healthProvider != null) {
            this.dgh$healthProvider.deserialize(compoundTag.getCompound("dgh$health"));
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void saveHealth(CompoundTag compoundTag, CallbackInfo ci) {
        if (this.dgh$healthProvider != null) {
            var tag = this.dgh$healthProvider.serialize();
            compoundTag.put("dgh$health", tag);
        }
    }

    @Override
    public HealthProvider dgh$getHealthProvider() {
        return dgh$healthProvider;
    }
}
