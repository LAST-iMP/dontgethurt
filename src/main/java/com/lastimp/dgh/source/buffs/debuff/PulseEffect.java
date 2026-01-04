package com.lastimp.dgh.source.buffs.debuff;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.config.BlackList;
import com.lastimp.dgh.source.core.bodyPart.Head;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.register.ModEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.COMA;

@EventBusSubscriber(modid = DontGetHurt.MODID)
public class PulseEffect extends MobEffect {
    public PulseEffect(int color) {
        super(MobEffectCategory.HARMFUL, color);
    }

    @Override
    public void onEffectAdded(LivingEntity livingEntity, int amplifier) {
        super.onEffectAdded(livingEntity, amplifier);
        this.apply(livingEntity);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int p_19468_) {
        return this.apply(livingEntity);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int p_19455_, int p_19456_) {
        return p_19455_ % 10 == 0;
    }

    @Override
    public void onMobRemoved(LivingEntity livingEntity, int amplifier, Entity.RemovalReason reason) {
        super.onMobRemoved(livingEntity, amplifier, reason);
    }

    @SubscribeEvent
    public static void onEffectRemove(MobEffectEvent.Remove event) {
        if (!event.getEffect().is(ModEffects.ANALGESIA_POISON_EFFECT)) return;
        if (event.getEntity() instanceof Mob mob) {
            mob.setNoAi(false);
        }
    }

    private boolean apply(LivingEntity livingEntity) {
        if (BlackList.isEntityBlacklisted(BlackList.PULSE_EFFECT, livingEntity.getType())) return false;
        if (HealthCapability.has(livingEntity)) {
            HealthCapability.getAndSet(livingEntity, h -> {
                Head head = (Head) h.getComponent(BodyComponents.HEAD);
                head.injury(COMA, BodyCondition.get(COMA).healingSpeed() * 1.5f);
            });
        } else if (livingEntity instanceof Mob mob) {
            mob.setNoAi(true);
            mob.targetSelector.removeAllGoals(e -> true);
            mob.goalSelector.removeAllGoals(e -> true);
        }
        return true;
    }
}
