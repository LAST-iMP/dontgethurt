package com.lastimp.dgh.common.item.medicine;

import com.lastimp.dgh.common.item.bases.AbstractDirectHealItems;
import com.lastimp.dgh.common.capability.bodyPart.bodies.Blood;
import com.lastimp.dgh.common.capability.bodyPart.bodies.Torso;
import com.lastimp.dgh.common.capability.HealthCapability;
import com.lastimp.dgh.common.entry.register.ModEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import net.minecraft.world.level.Level;

import java.util.List;

import static com.lastimp.dgh.common.capability.bodyPart.base.BodyCondition.*;
import static com.lastimp.dgh.common.enums.BodyComponents.*;

public class Adrenaline extends AbstractDirectHealItems {
    public Adrenaline(Properties properties) {
        super(properties);
    }

    @Override
    public boolean heal(@NotNull LivingEntity source, @NotNull LivingEntity entity) {
        return HealthCapability.getAndApply(entity, h -> {
            Torso torso = (Torso) h.getComponent(TORSO);
            Blood blood = (Blood) h.getComponent(BLOOD);

            blood.injury(BLOOD_PRESSURE, 0.3f);
            if (torso.getHeartRateLevel() >= 2) {
                torso.setHeartRateLevel(torso.getHeartRateLevel() / 2);
            }

            if (entity.hasEffect(ModEffects.ADRENALINE_EFFECT.get())) {
                int newAmp = entity.getEffect(ModEffects.ADRENALINE_EFFECT.get()).getAmplifier() + 1;
                entity.addEffect(new MobEffectInstance(ModEffects.ADRENALINE_EFFECT.get(),  60 * 20, newAmp));
            } else {
                entity.addEffect(new MobEffectInstance(ModEffects.ADRENALINE_EFFECT.get(), 60 * 20));
            }
            return true;
        }, false);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("治疗"));
        tooltipComponents.add(Component.literal("·心脏骤停").withStyle(ChatFormatting.BLUE));
        tooltipComponents.add(Component.literal("造成"));
        tooltipComponents.add(Component.literal("·攻击+10%每级").withStyle(ChatFormatting.GREEN));
        tooltipComponents.add(Component.literal("·移速+10%每级").withStyle(ChatFormatting.GREEN));
        tooltipComponents.add(Component.literal("·心颤").withStyle(ChatFormatting.RED));
    }
}
