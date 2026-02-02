package com.lastimp.dgh.source.item.medicine;

import com.lastimp.dgh.api.healingItems.AbstractDirectHealItems;
import com.lastimp.dgh.source.core.bodyPart.Blood;
import com.lastimp.dgh.source.core.bodyPart.Torso;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.register.ModEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.lastimp.dgh.source.core.bodyPart.base.BodyCondition.BLOOD_PRESSURE;
import static com.lastimp.dgh.api.enums.BodyComponents.BLOOD;
import static com.lastimp.dgh.api.enums.BodyComponents.TORSO;

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

            if (entity.hasEffect(ModEffects.ADRENALINE_EFFECT)) {
                int newAmp = entity.getEffect(ModEffects.ADRENALINE_EFFECT).getAmplifier() + 1;
                entity.addEffect(new MobEffectInstance(ModEffects.ADRENALINE_EFFECT,  60 * 20, newAmp));
            } else {
                entity.addEffect(new MobEffectInstance(ModEffects.ADRENALINE_EFFECT, 60 * 20));
            }
            return true;
        }, false);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("治疗"));
        tooltipComponents.add(Component.literal("·心脏骤停").withStyle(ChatFormatting.BLUE));
        tooltipComponents.add(Component.literal("造成"));
        tooltipComponents.add(Component.literal("·攻击+10%每级").withStyle(ChatFormatting.GREEN));
        tooltipComponents.add(Component.literal("·移速+10%每级").withStyle(ChatFormatting.GREEN));
        tooltipComponents.add(Component.literal("·心颤").withStyle(ChatFormatting.RED));
    }
}
