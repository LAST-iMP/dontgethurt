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

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.source.core.Utils;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.lastimp.dgh.api.enums.BodyCondition.*;

@Mod.EventBusSubscriber(modid = DontGetHurt.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class InjuryEventHandler {

    @SubscribeEvent
    public static void onPlayerInjury(LivingHurtEvent event) {
        if (event.getEntity().level().isClientSide) return;
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();

        float damageAmount = event.getAmount();
        DamageSource source = event.getSource();

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

    public static void handleFalling(float damageAmount, Player player, LivingHurtEvent event) {
        PlayerHealthCapability.getAndSet(player, h -> {
            AbstractBody[] legs = h.legs();
            float[] weight = Utils.getRandom(1, 1);
            for (int i = 0; i < legs.length; i++) {
                InternalInjuryHandler.handleBluntTrauma(h, legs[i], damageAmount * weight[i] / player.getMaxHealth(), player.getMaxHealth());
            }
            return h;
        });
        event.setAmount(0f);
    }

    public static void handleBurning(float damageAmount, Player player, LivingHurtEvent event) {
        PlayerHealthCapability.getAndSet(player, h -> {
            BodyComponents randomComponent = BodyComponents.random();
            AbstractBody body = h.getComponent(randomComponent);

            body.injury(BURN, damageAmount / player.getMaxHealth());
            return h;
        });
        event.setAmount(0f);
    }

    public static void handleDrowning(float damageAmount, Player player, LivingHurtEvent event) {
        PlayerHealthCapability.getAndSet(player, h -> {
            AbstractBody torso = h.getComponent(BodyComponents.TORSO);
            InternalInjuryHandler.handle(h, torso, damageAmount / player.getMaxHealth());
            return h;
        });
        event.setAmount(0f);
    }

    public static void handleExplosion(float damageAmount, Player player, LivingHurtEvent event) {
        PlayerHealthCapability.getAndSet(player, h -> {
            AbstractBody[] body = h.visibleParts();
            float[] weight = Utils.getRandom(1.5f,3,2,2,1.5f,1.5f);
            for (int i = 0; i < body.length; i++) {
                body[i].injury(OPEN_WOUND,      0.5f * damageAmount * weight[i] / player.getMaxHealth());
                InternalInjuryHandler.handleExplosion(h, body[i], 0.5f * damageAmount * weight[i] / player.getMaxHealth(), player.getMaxHealth());
            }
            return h;
        });
        event.setAmount(0f);
    }

    public static void handleEntityAttack(float damageAmount, Player player, LivingHurtEvent event) {
        PlayerHealthCapability.getAndSet(player, h -> {
            AbstractBody[] body = h.visibleParts();
            int index = Utils.getRandomIndex(1,2,2,2,0.5f,0.5f);
            body[index].injury(OPEN_WOUND, damageAmount / player.getMaxHealth());
            return h;
        });
        event.setAmount(0f);
    }

    public static void handleMagicDamage(float damageAmount, Player player, LivingHurtEvent event) {
        handleDefaultDamage(damageAmount / 2, player, event);
        event.setAmount(damageAmount / 2);
    }

    public static void handleDefaultDamage(float damageAmount, Player player, LivingHurtEvent event) {
        PlayerHealthCapability.getAndSet(player, h -> {
            AbstractBody[] body = h.visibleParts();
            int index = Utils.getRandomIndex(1,2,2,2,0.5f,0.5f);
            InternalInjuryHandler.handleBluntTrauma(h, body[index], damageAmount / player.getMaxHealth(), player.getMaxHealth());
            return h;
        });
        event.setAmount(0f);
    }
}
