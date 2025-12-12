
package com.lastimp.dgh.source.core.damageSystem;

import com.lastimp.dgh.Config;
import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.tags.ModDamageType;
import com.lastimp.dgh.source.core.Utils;
import com.lastimp.dgh.source.core.bodyPart.PlayerBlood;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingHurtEvent;

import static com.lastimp.dgh.api.enums.BodyComponents.*;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;

@EventBusSubscriber(modid = DontGetHurt.MODID, bus = EventBusSubscriber.Bus.GAME)
public class InjuryEventHandler {

    @SubscribeEvent
    public static void onPlayerInjury(LivingDamageEvent event) {
        if (event.getEntity().level().isClientSide) return;
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        float damageAmount = event.getAmount();
        float absorption = player.getAbsorptionAmount();
        DamageSource source = event.getSource();

        if (source.is(ModDamageType.FINAL_HEALTH_DAMAGE)) return;
        if (absorption >= damageAmount) return;
        else if (absorption > 0) {
            damageAmount = damageAmount - absorption;
            player.setAbsorptionAmount(0);
        }

        damageAmount /= player.getMaxHealth() * Config.body_life_factor;
        damageAmount /= PlayerHealthCapability.isDying(player)? 10f : 1f;

        if (source.is(DamageTypeTags.IS_FALL)) {
            handleFalling(damageAmount, player, event);
        } else if (source.is(DamageTypeTags.IS_FIRE)) {
            handleBurning(damageAmount, player, event);
        } else if (source.is(DamageTypeTags.IS_DROWNING)) {
            handleDrowning(damageAmount, player, event);
        } else if (source.is(DamageTypeTags.IS_EXPLOSION)) {
            handleExplosion(damageAmount, player, event);
        } else if (source.getEntity() != null && source.getEntity() instanceof LivingEntity) {
            handleEntityAttack(damageAmount, player, event);
        } else if (source.is(DamageTypes.INDIRECT_MAGIC) || source.is(DamageTypes.MAGIC)) {
            handleMagicDamage(damageAmount, player, event);
        } else if (!source.is(DamageTypes.GENERIC_KILL)) {
            handleDefaultDamage(damageAmount, player, event);
        }
    }

    public static void handleFalling(float damageAmount, Player player, LivingDamageEvent event) {
        PlayerHealthCapability.getAndSet(player, h -> {
            float[] weight = Utils.getRandom(1, 1);
            for (int i = 0; i < LEGS.size(); i++) {
                var leg = h.getComponent(LEGS.get(i));
                InternalInjuryHandler.handleBluntTrauma(leg, damageAmount * weight[i]);
            }
            return h;
        });
        event.setAmount(0f);
    }

    public static void handleBurning(float damageAmount, Player player, LivingDamageEvent event) {
        PlayerHealthCapability.getAndSet(player, h -> {
            BodyComponents randomComponent = BodyComponents.random();

            BurnHandler.handle(h, h.getComponent(randomComponent), damageAmount);
            return h;
        });
        event.setAmount(0f);
    }

    public static void handleDrowning(float damageAmount, Player player, LivingDamageEvent event) {
        PlayerHealthCapability.getAndSet(player, h -> {
            PlayerBlood blood = (PlayerBlood) h.getComponent(BLOOD);
            blood.injury(OXYGEN, damageAmount / 20);
            return h;
        });
        event.setAmount(0f);
    }

    public static void handleExplosion(float damageAmount, Player player, LivingDamageEvent event) {
        PlayerHealthCapability.getAndSet(player, h -> {
            float[] weight = Utils.getRandom(1.5f,3,2,2,1.5f,1.5f);
            for (int i = 0; i < VISIBLE_BODIES.size(); i++) {
                var body = h.getComponent(VISIBLE_BODIES.get(i));
                OpenWoundHandler.handleExplosion(body, 0.5f * damageAmount * weight[i]);
                InternalInjuryHandler.handleExplosion(body, 0.5f * damageAmount * weight[i]);
            }
            return h;
        });
        event.setAmount(0f);
    }

    public static void handleEntityAttack(float damageAmount, Player player, LivingDamageEvent event) {
        PlayerHealthCapability.getAndSet(player, h -> {
            var body = h.getComponent(VISIBLE_BODIES.get(Utils.getRandomIndex(1,2,2,2,0.5f,0.5f)));
            OpenWoundHandler.handleEntityAttack(body, damageAmount);
            return h;
        });
        event.setAmount(0f);
    }

    public static void handleMagicDamage(float damageAmount, Player player, LivingDamageEvent event) {
        handleDefaultDamage(damageAmount, player, event);
        event.setAmount(0f);
    }

    public static void handleDefaultDamage(float damageAmount, Player player, LivingDamageEvent event) {
        PlayerHealthCapability.getAndSet(player, h -> {
            var body = h.getComponent(VISIBLE_BODIES.get(Utils.getRandomIndex(1,2,2,2,0.5f,0.5f)));
            InternalInjuryHandler.handleBluntTrauma(body, damageAmount);
            return h;
        });
        event.setAmount(0f);
    }
}
