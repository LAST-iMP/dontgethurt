
package com.lastimp.dgh.source.core.damageSystem;

import com.lastimp.dgh.Config;
import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.enums.BodyComponents;
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

import static com.lastimp.dgh.api.enums.BodyComponents.BLOOD;
import static com.lastimp.dgh.api.enums.BodyCondition.*;

@EventBusSubscriber(modid = DontGetHurt.MODID)
public class InjuryEventHandler {

    @SubscribeEvent
    public static void onPlayerInjury(LivingDamageEvent.Pre event) {
        if (event.getEntity().level().isClientSide) return;
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        float damageAmount = event.getNewDamage();
        float absorption = player.getAbsorptionAmount();
        DamageSource source = event.getSource();

        if (absorption >= damageAmount) return;
        else if (absorption > 0) {
            damageAmount = damageAmount - absorption;
            player.setAbsorptionAmount(0);
        }

        damageAmount /= player.getMaxHealth() * Config.body_life_factor;

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

    public static void handleFalling(float damageAmount, Player player, LivingDamageEvent.Pre event) {
        PlayerHealthCapability.getAndSet(player, h -> {
            AbstractBody[] legs = h.legs();
            float[] weight = Utils.getRandom(1, 1);
            for (int i = 0; i < legs.length; i++) {
                InternalInjuryHandler.handleBluntTrauma(h, legs[i], damageAmount * weight[i], player.getMaxHealth());
            }
            return h;
        });
        event.setNewDamage(0f);
    }

    public static void handleBurning(float damageAmount, Player player, LivingDamageEvent.Pre event) {
        PlayerHealthCapability.getAndSet(player, h -> {
            BodyComponents randomComponent = BodyComponents.random();

            BurnHandler.handle(h, h.getComponent(randomComponent), damageAmount);
            return h;
        });
        event.setNewDamage(0f);
    }

    public static void handleDrowning(float damageAmount, Player player, LivingDamageEvent.Pre event) {
        PlayerHealthCapability.getAndSet(player, h -> {
            PlayerBlood blood = (PlayerBlood) h.getComponent(BLOOD);
            blood.injury(OXYGEN, damageAmount / 20);
            return h;
        });
        event.setNewDamage(0f);
    }

    public static void handleExplosion(float damageAmount, Player player, LivingDamageEvent.Pre event) {
        PlayerHealthCapability.getAndSet(player, h -> {
            AbstractBody[] body = h.visibleParts();
            float[] weight = Utils.getRandom(1.5f,3,2,2,1.5f,1.5f);
            for (int i = 0; i < body.length; i++) {
                OpenWoundHandler.handleExplosion(h, body[i], 0.5f * damageAmount * weight[i], player.getMaxHealth());
                InternalInjuryHandler.handleExplosion(h, body[i], 0.5f * damageAmount * weight[i], player.getMaxHealth());
            }
            return h;
        });
        event.setNewDamage(0f);
    }

    public static void handleEntityAttack(float damageAmount, Player player, LivingDamageEvent.Pre event) {
        PlayerHealthCapability.getAndSet(player, h -> {
            AbstractBody[] body = h.visibleParts();
            int index = Utils.getRandomIndex(1,2,2,2,0.5f,0.5f);
            OpenWoundHandler.handleEntityAttack(h, body[index], damageAmount, player.getMaxHealth());
            return h;
        });
        event.setNewDamage(0f);
    }

    public static void handleMagicDamage(float damageAmount, Player player, LivingDamageEvent.Pre event) {
        handleDefaultDamage(damageAmount, player, event);
        event.setNewDamage(0f);
    }

    public static void handleDefaultDamage(float damageAmount, Player player, LivingDamageEvent.Pre event) {
        PlayerHealthCapability.getAndSet(player, h -> {
            AbstractBody[] body = h.visibleParts();
            int index = Utils.getRandomIndex(1,2,2,2,0.5f,0.5f);
            InternalInjuryHandler.handleBluntTrauma(h, body[index], damageAmount, player.getMaxHealth());
            return h;
        });
        event.setNewDamage(0f);
    }
}
