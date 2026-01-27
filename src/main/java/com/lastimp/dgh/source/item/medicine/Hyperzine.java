package com.lastimp.dgh.source.item.medicine;

import com.lastimp.dgh.api.healingItems.AbstractDirectHealItems;
import com.lastimp.dgh.source.core.bodyPart.Blood;
import com.lastimp.dgh.source.core.bodyPart.Head;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;
import static com.lastimp.dgh.api.enums.BodyComponents.*;

public class Hyperzine extends AbstractDirectHealItems {
    public Hyperzine(Properties properties) {
        super(properties);
    }

    @Override
    public boolean heal(@NotNull LivingEntity source, @NotNull LivingEntity entity) {
        return HealthCapability.getAndApply(entity, h -> {
            Head head = (Head) h.getComponent(HEAD);
            Blood blood = (Blood) h.getComponent(BLOOD);

            head.healing(WITHDRAW, -0.9f);
            blood.injury(OPIATE_ADDICTED, 0.18f);
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 60 * 20, 10), source);
            entity.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 60 * 20, 10), source);
            for (var components : VISIBLE_BODIES) {
                var body = h.getComponent(components);
                var hidden = body.getConditionHidden(INTERNAL_INJURY);
                body.healingHidden(INTERNAL_INJURY, -Math.min(hidden, 0.12f));
                body.healing(INTERNAL_INJURY, -Math.max(0, 0.12f - hidden));
            }
            return true;
        }, false);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("治疗"));
        tooltipComponents.add(Component.literal("·戒断").withStyle(ChatFormatting.BLUE));
        tooltipComponents.add(Component.literal("·内伤").withStyle(ChatFormatting.BLUE));
        tooltipComponents.add(Component.literal("造成"));
        tooltipComponents.add(Component.literal("·增加移速").withStyle(ChatFormatting.GREEN));
        tooltipComponents.add(Component.literal("·增加攻速").withStyle(ChatFormatting.GREEN));
        tooltipComponents.add(Component.literal("·增加挖掘速度").withStyle(ChatFormatting.GREEN));
        tooltipComponents.add(Component.literal("·阿片成瘾").withStyle(ChatFormatting.RED));
    }
}
