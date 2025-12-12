package com.lastimp.dgh.source.buffs.buff;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.register.ModEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = DontGetHurt.MODID)
public class KeepLivingEffect extends MobEffect {
    public static String ID_MOVE = "efea5782-b172-4048-b0cb-48622290b4a4";
    public static String ID_ATTACK = "efea5782-b172-4048-b1cb-48622290b4a4";

    public KeepLivingEffect(int color) {
        super(MobEffectCategory.BENEFICIAL, color);
    }

    @Override
    public void addAttributeModifiers(LivingEntity livingEntity, AttributeMap attributeMap, int amplifier) {
        super.addAttributeModifiers(livingEntity, attributeMap, amplifier);

        AttributeInstance move = attributeMap.getInstance(Attributes.MOVEMENT_SPEED);
        AttributeInstance attack = attributeMap.getInstance(Attributes.ATTACK_SPEED);

        double amount = 0.015 * (amplifier + 1);
        AttributeModifier move_modifier = new AttributeModifier(
                UUID.fromString(ID_MOVE),
                "living_movement_effect",
                amount,
                AttributeModifier.Operation.MULTIPLY_TOTAL
        );
        AttributeModifier attack_modifier = new AttributeModifier(
                UUID.fromString(ID_ATTACK),
                "living_attack_effect",
                amount,
                AttributeModifier.Operation.MULTIPLY_TOTAL
        );

        if (move != null) {
            move.removeModifier(UUID.fromString(ID_MOVE));
            move.addTransientModifier(move_modifier);
        }
        if (attack != null) {
            attack.removeModifier(UUID.fromString(ID_ATTACK));
            attack.addTransientModifier(attack_modifier);
        }
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
        super.removeAttributeModifiers(entity, attributeMap, amplifier);

        AttributeInstance move = attributeMap.getInstance(Attributes.MOVEMENT_SPEED);
        AttributeInstance attack = attributeMap.getInstance(Attributes.ATTACK_SPEED);
        if (move != null)
            move.removeModifier(UUID.fromString(ID_MOVE));
        if (attack != null)
            attack.removeModifier(UUID.fromString(ID_ATTACK));
    }

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        if (player.hasEffect(ModEffects.KEEP_LIVING_EFFECT.get())) {
            int amplifier = player.getEffect(ModEffects.KEEP_LIVING_EFFECT.get()).getAmplifier();
            float original = event.getOriginalSpeed();

            float multiplier = 1.0f + (0.015f * (amplifier + 1));
            event.setNewSpeed(original * multiplier);
        }
    }
}
