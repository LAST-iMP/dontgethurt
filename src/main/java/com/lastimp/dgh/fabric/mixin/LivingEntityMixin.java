package com.lastimp.dgh.fabric.mixin;

import com.lastimp.dgh.common.capability.HealthCapability;
import com.lastimp.dgh.fabric.capability.HealthHolder;
import com.lastimp.dgh.fabric.capability.provider.HealthProvider;
import com.lastimp.dgh.fabric.event.callback.LivingEventCallBack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements HealthHolder {
    @Shadow
    protected Brain<?> brain;
    @Unique
    private HealthProvider dgh$healthProvider;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void dgh$initProvider(CallbackInfo ci) {

        LivingEntity self = (LivingEntity)(Object)this;

        if (HealthCapability.has(self)) {
            this.dgh$healthProvider = new HealthProvider();
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void dgh$tick(CallbackInfo ci) {
        LivingEventCallBack.LivingTickEvent.EVENT.invoker().interact((LivingEntity)(Object)this);
    }

    @Invoker("decreaseAirSupply")
    protected abstract int decreaseAirSupply(int i);

    @Redirect(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getAirSupply()I"))
    private int dgh$getAirSupply(LivingEntity instance) {
        return Integer.MAX_VALUE;
    }

    @Redirect(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isEyeInFluid(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean dgh$baseTick(LivingEntity instance, TagKey<Fluid> tagKey) {
        LivingEntity entity = (LivingEntity)(Object)this;
        boolean isInWater = entity.isEyeInFluid(FluidTags.WATER);
        boolean isAir = !isInWater || entity.level().getBlockState(BlockPos.containing(entity.getX(), entity.getEyeY(), entity.getZ())).is(Blocks.BUBBLE_COLUMN);
        boolean canBreathe = isAir;
        int refillAirAmount = 4;
        if (!isAir && (MobEffectUtil.hasWaterBreathing(entity) || !(entity.canBreatheUnderwater() && isInWater) || (entity instanceof Player player && player.getAbilities().invulnerable))) {
            canBreathe = true;
            refillAirAmount = 0;
        }
        canBreathe &= LivingEventCallBack.BreathEvent.EVENT.invoker().interact(entity);
        if (canBreathe) {
            entity.setAirSupply(Math.min(entity.getAirSupply() + refillAirAmount, entity.getMaxAirSupply()));
        } else {
            entity.setAirSupply(this.decreaseAirSupply(entity.getAirSupply()));
        }

        if (entity.getAirSupply() <= -20) {
            entity.setAirSupply(0);
            Vec3 vec3 = entity.getDeltaMovement();

            for (int i = 0; i < 8; ++i) {
                double d2 = entity.getRandom().nextDouble() - entity.getRandom().nextDouble();
                double d3 = entity.getRandom().nextDouble() - entity.getRandom().nextDouble();
                double d4 = entity.getRandom().nextDouble() - entity.getRandom().nextDouble();
                entity.level().addParticle(ParticleTypes.BUBBLE, entity.getX() + d2, entity.getY() + d3, entity.getZ() + d4, vec3.x, vec3.y, vec3.z);
            }
            entity.hurt(entity.damageSources().drown(), 2.0f);
        }

        if (!entity.level().isClientSide && entity.isPassenger() && entity.getVehicle() != null && entity.getVehicle().dismountsUnderwater()) {
            entity.stopRiding();
        }
        return false;
    }

    @ModifyVariable(method = "actuallyHurt", at = @At(value = "LOAD", ordinal = 5), index = 2, argsOnly = true)
    private float dgh$damage(float amount, DamageSource source) {
        return LivingEventCallBack.DamageEvent.EVENT.invoker().interact((LivingEntity)(Object)this, source, amount);
    }

    @Inject(method = "heal", at = @At("HEAD"), cancellable = true)
    private void dgh$heal(float f, CallbackInfo ci) {
        LivingEntity self = (LivingEntity)(Object)this;
        if (!self.isDeadOrDying())
            LivingEventCallBack.HealingEvent.EVENT.invoker().interact(self, f);
        ci.cancel();
    }

    @Inject(method = "die", at = @At("HEAD"))
    private void dgh$die(DamageSource damageSource, CallbackInfo ci) {
        LivingEntity self = (LivingEntity)(Object)this;
        if (!self.isDeadOrDying())
            LivingEventCallBack.DeathEvent.EVENT.invoker().interact(self);
    }

    @ModifyArg(method = "causeFallDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;calculateFallDamage(FF)I"), index = 0)
    private float dgh$handleFallDamage(float f) {
        LivingEntity self = (LivingEntity)(Object)this;
        return Math.max(0, f - LivingEventCallBack.FallEvent.EVENT.invoker().interact(self));
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
