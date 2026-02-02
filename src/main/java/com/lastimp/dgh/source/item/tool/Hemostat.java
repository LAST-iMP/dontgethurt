package com.lastimp.dgh.source.item.tool;

import com.lastimp.dgh.api.bodyPart.ConditionAccessor;
import com.lastimp.dgh.source.core.bodyPart.base.AbstractVisibleBody;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.healingItems.AbstractPartlyHealItem;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.lastimp.dgh.source.core.bodyPart.base.BodyCondition.CLAMPED_BLEEDING;
import static com.lastimp.dgh.source.core.bodyPart.base.BodyCondition.SURGERY_INCISION;

public class Hemostat extends AbstractPartlyHealItem {
    public Hemostat(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean healOn(@NotNull LivingEntity source, @NotNull LivingEntity entity, BodyComponents component) {
        return HealthCapability.getAndApply(entity, (h) -> {
            AbstractVisibleBody body = (AbstractVisibleBody) h.getComponent(component);
            if (!body.abnormal(SURGERY_INCISION)) return false;
            if (body.abnormal(CLAMPED_BLEEDING)) return false;

            body.setConditionValue(CLAMPED_BLEEDING, ConditionAccessor.get(CLAMPED_BLEEDING).maxValue());
            return true;
        }, false);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("手术第二步"));
        tooltipComponents.add(Component.literal("·夹闭止血").withStyle(ChatFormatting.BLUE));
        tooltipComponents.add(Component.literal("需要"));
        tooltipComponents.add(Component.literal("·手术切口").withStyle(ChatFormatting.GREEN));
        tooltipComponents.add(Component.literal("造成"));
        tooltipComponents.add(Component.literal("·创伤性休克").withStyle(ChatFormatting.RED));
    }
}
