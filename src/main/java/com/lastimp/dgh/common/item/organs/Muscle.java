package com.lastimp.dgh.common.item.organs;

import com.lastimp.dgh.common.capability.bodyPart.ConditionAccessor;
import com.lastimp.dgh.common.capability.bodyPart.base.AbstractBody;
import com.lastimp.dgh.common.capability.bodyPart.base.AbstractOrgan;
import com.lastimp.dgh.common.capability.bodyPart.base.AbstractVisibleBody;
import com.lastimp.dgh.common.tags.ModTags;
import com.lastimp.dgh.common.utils.Utils;
import com.lastimp.dgh.common.capability.HealthCapability;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

import static com.lastimp.dgh.common.capability.bodyPart.base.BodyCondition.*;

public class Muscle extends AbstractOrgan {
    public Muscle(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack update(ItemStack stack, HealthCapability health, AbstractBody body, LivingEntity entity) {
        AbstractVisibleBody visibleBody = (AbstractVisibleBody) body;
        int num = visibleBody.countOrganMatch(ModTags.MUSCLE);
        float factor = Utils.sqrtFactor(num, 1f) / num;

        var condition = ConditionAccessor.get(INTERNAL_INJURY);
        if (body.abnormal(INTERNAL_INJURY) && body.getConditionValue(INTERNAL_INJURY) < condition.healingTS() * num * factor) {
            body.healing(INTERNAL_INJURY, -ConditionAccessor.get(INTERNAL_INJURY).healingSpeed() * Utils.DELTA * factor);
        }
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("提供"));
        tooltipComponents.add(Component.literal("·基础内伤恢复").withStyle(ChatFormatting.BLUE));
    }
}
