package com.lastimp.dgh.source.item.tool;

import com.lastimp.dgh.api.bodyPart.ConditionAccessor;
import com.lastimp.dgh.source.core.bodyPart.base.AbstractExtremities;
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

import static com.lastimp.dgh.source.core.bodyPart.base.BodyCondition.*;

public class SurgicalDrill extends AbstractPartlyHealItem {
    public SurgicalDrill(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean healOn(@NotNull LivingEntity source, @NotNull LivingEntity entity, BodyComponents component) {
        return HealthCapability.getAndApply(entity, (h) -> {
            AbstractVisibleBody body = (AbstractVisibleBody) h.getComponent(component);
            if (!body.abnormal(RETRACTED_SKIN)) return false;
            if (body.abnormal(DRILLED_BONES)) return false;
            if (body.abnormal(SAWED_BONES)) return false;
            if (body instanceof AbstractExtremities extremities)
                if (extremities.abnormal(TRAUMATIC_AMPUTATION) || extremities.abnormal(SURGICAL_AMPUTATION)) return false;

            body.setConditionValue(DRILLED_BONES, ConditionAccessor.get(DRILLED_BONES).maxValue());
            return true;
        }, false);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("治疗"));
        tooltipComponents.add(Component.literal("·骨折").withStyle(ChatFormatting.BLUE));
        tooltipComponents.add(Component.literal("·骨损伤").withStyle(ChatFormatting.BLUE));
        tooltipComponents.add(Component.literal("·骨坏死").withStyle(ChatFormatting.BLUE));
        tooltipComponents.add(Component.literal("需要"));
        tooltipComponents.add(Component.literal("·皮肤牵开").withStyle(ChatFormatting.GREEN));
        tooltipComponents.add(Component.literal("造成"));
        tooltipComponents.add(Component.literal("·骨骼钻孔").withStyle(ChatFormatting.RED));
        tooltipComponents.add(Component.literal("·创伤性休克").withStyle(ChatFormatting.RED));
    }
}
