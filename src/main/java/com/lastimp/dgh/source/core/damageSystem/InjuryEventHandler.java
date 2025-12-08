/*
* MIT License

Copyright (c) 2023 NeoForged project

This license applies to the template files as supplied by github.com/NeoForged/MDK


Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

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
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;
import static com.lastimp.dgh.api.enums.BodyComponents.*;
import static com.lastimp.dgh.api.enums.BodyComponents.VISIBLE_BODIES;

@Mod.EventBusSubscriber(modid = DontGetHurt.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class InjuryEventHandler {

    @SubscribeEvent
    public static void onPlayerInjury(LivingDamageEvent event) {
        if (event.getEntity().level().isClientSide) return;
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        float damageAmount = event.getAmount();
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
