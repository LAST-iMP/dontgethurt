package com.lastimp.dgh.source.core.dyingSystem;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.api.tags.ModDamageType;
import com.lastimp.dgh.source.core.Utils;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import static com.lastimp.dgh.source.core.bodyPart.base.BodyCondition.*;
import static com.lastimp.dgh.api.enums.BodyComponents.*;

@EventBusSubscriber(modid = DontGetHurt.MODID)
public class DyingHandler {
    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Pre event) {
        if (event.getEntity().level().isClientSide) return;
        if (!(event.getEntity() instanceof LivingEntity livingEntity)) return;
        if (!HealthCapability.has(livingEntity)) return;

        if (!event.getEntity().level().isClientSide) {
            if (HealthCapability.isDown(livingEntity)) {
                if (livingEntity instanceof Player player && player.isFallFlying()) player.stopFallFlying();
                if (livingEntity.isSleeping()) livingEntity.stopSleeping();
                livingEntity.stopUsingItem();
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        var entity = event.getEntity();
        if (!HealthCapability.has(entity)) return;
        HealthCapability.getAndApply(entity, h -> {
            h.addOriginOrganOnDeath(entity);
            h.deserializeNBT(entity.registryAccess(), h.deathSerializeNBT(entity.registryAccess()));
        });

        if (!(event.getEntity() instanceof Player player)) return;
        var data = player.getPersistentData();
        var persistedTag = data.getCompound(Player.PERSISTED_NBT_TAG);
        HealthCapability.getAndApply(player, h ->
                persistedTag.put(HealthCapability.HEALTH_RECORD, h.deathSerializeNBT(entity.registryAccess()))
        );
        data.put(Player.PERSISTED_NBT_TAG, persistedTag);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        var player = event.getEntity();
        var data = player.getPersistentData();
        var persistedTag = data.getCompound(Player.PERSISTED_NBT_TAG);
        HealthCapability.getAndApply(player, newHealth ->
                newHealth.respawnDeserializeNBT(player.registryAccess(), persistedTag.getCompound(HealthCapability.HEALTH_RECORD))
        );
        persistedTag.remove(HealthCapability.HEALTH_RECORD);
        data.put(Player.PERSISTED_NBT_TAG, persistedTag);
    }

    public static void setLivingDead(LivingEntity entity) {
        if (entity.isDeadOrDying()) return;
        if (entity instanceof ServerPlayer player) {
            if (checkTotemDeathProtection(player)) return;
        }
        HealthCapability.getAndApply(entity, h -> {
            if (!h.oxygenMask().getStackInSlot(0).isEmpty()) {
                Utils.drop(h.oxygenMask().getStackInSlot(0), entity);
                h.oxygenMask().setStackInSlot(0, ItemStack.EMPTY);
            }
            if (!h.autoPulse().getStackInSlot(0).isEmpty()) {
                Utils.drop(h.autoPulse().getStackInSlot(0), entity);
                h.autoPulse().setStackInSlot(0, ItemStack.EMPTY);
            }

            var lastDamageSource = entity.getLastDamageSource();
            var record = HealthCapability.getAndApply(entity, HealthCapability::lastEntityDamage, null);
            if (lastDamageSource == null || lastDamageSource.is(DamageTypeTags.IS_DROWNING)) {
                if (record != null) {
                    lastDamageSource = new DamageSource(getKillerDamageType(entity, h), record.getDirectEntity(), record.getEntity(), record.getSourcePosition());
                } else {
                    lastDamageSource = new DamageSource(getKillerDamageType(entity, h));
                }
            }
            entity.getCombatTracker().recordDamage(lastDamageSource, 1);
            entity.setHealth(0);
            entity.die(lastDamageSource);
        });
    }

    public static Holder<DamageType> getKillerDamageType(LivingEntity entity, HealthCapability health) {
        var damageType = entity.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE);

        var head = health.getComponent(HEAD);
        if (head.getConditionValue(TRAUMATIC_SHOCK) > 0.4) {
            return damageType.getOrThrow(ModDamageType.SURGERY_DAMAGE);
        }

        var blood = health.getComponent(BLOOD);
        if (blood.getConditionValue(BLOOD_LOSS) > 0.7) {
            return damageType.getOrThrow(ModDamageType.BLEED_DAMAGE);
        }

        var torso = health.getComponent(TORSO);
        if (torso.abnormal(RESPIRATORY_ARREST)) {
            return damageType.getOrThrow(ModDamageType.CANT_BREATH_DAMAGE);
        }

        if (head.getConditionValue(BRAIN_DAMAGE) > 0.9) {
            return damageType.getOrThrow(ModDamageType.BRAIN_DAMAGE);
        }

        var left_arm = health.getComponent(LEFT_ARM);
        var left_leg = health.getComponent(LEFT_LEG);
        var right_arm = health.getComponent(RIGHT_ARM);
        var right_leg = health.getComponent(RIGHT_LEG);
        float internal_injury = left_arm.getConditionValue(INTERNAL_INJURY) + left_leg.getConditionValue(INTERNAL_INJURY) +
                right_arm.getConditionValue(INTERNAL_INJURY) + right_leg.getConditionValue(INTERNAL_INJURY);
        float open_wound = left_arm.getConditionValue(OPEN_WOUND) + left_leg.getConditionValue(OPEN_WOUND) +
                right_arm.getConditionValue(OPEN_WOUND) + right_leg.getConditionValue(OPEN_WOUND) +
                left_arm.getConditionHidden(OPEN_WOUND) + left_leg.getConditionHidden(OPEN_WOUND) +
                right_arm.getConditionHidden(OPEN_WOUND) + right_leg.getConditionHidden(OPEN_WOUND);
        open_wound += left_arm.getConditionValue(PASS_THROUGH) + left_leg.getConditionValue(PASS_THROUGH) +
                right_arm.getConditionValue(PASS_THROUGH) + right_leg.getConditionValue(PASS_THROUGH) +
                left_arm.getConditionHidden(PASS_THROUGH) + left_leg.getConditionHidden(PASS_THROUGH) +
                right_arm.getConditionHidden(PASS_THROUGH) + right_leg.getConditionHidden(PASS_THROUGH);
        float burn = left_arm.getConditionValue(BURN) + left_leg.getConditionValue(BURN) +
                right_arm.getConditionValue(BURN) + right_leg.getConditionValue(BURN) +
                left_arm.getConditionHidden(BURN) + left_leg.getConditionHidden(BURN) +
                right_arm.getConditionHidden(BURN) + right_leg.getConditionHidden(BURN);
        if (burn > open_wound && burn > internal_injury) {
            return damageType.getOrThrow(ModDamageType.BURN_DAMAGE);
        }
        if (open_wound > internal_injury) {
            return damageType.getOrThrow(ModDamageType.OPEN_WOUND_DAMAGE);
        }
        return damageType.getOrThrow(ModDamageType.INTERNAL_INJURY_DAMAGE);
    }

    private static boolean checkTotemDeathProtection(ServerPlayer player) {
        ItemStack itemstack = null;
        for (InteractionHand interactionhand : InteractionHand.values()) {
            ItemStack itemstack1 = player.getItemInHand(interactionhand);
            DamageSource source = player.level().damageSources().genericKill();
            if (itemstack1.is(Items.TOTEM_OF_UNDYING) && CommonHooks.onLivingUseTotem(player, source, itemstack1, interactionhand)) {
                itemstack = itemstack1.copy();
                itemstack1.shrink(1);
                break;
            }
        }
        if (itemstack == null) return false;

        if (player instanceof ServerPlayer serverplayer) {
            serverplayer.awardStat(Stats.ITEM_USED.get(Items.TOTEM_OF_UNDYING), 1);
            CriteriaTriggers.USED_TOTEM.trigger(serverplayer, itemstack);
            player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
        }

        HealthCapability.getAndApply(player, h -> h.healingAll(true));
        player.setHealth(player.getMaxHealth());
        player.removeEffectsCuredBy(net.neoforged.neoforge.common.EffectCures.PROTECTED_BY_TOTEM);
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
        player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
        player.level().broadcastEntityEvent(player, (byte)35);
        return true;
    }
}
