package com.lastimp.dgh.source.item.tool;

import com.lastimp.dgh.api.bodyPart.ConditionAccessor;
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

import static com.lastimp.dgh.source.core.bodyPart.base.BodyCondition.INFECTION;
import static com.lastimp.dgh.source.core.bodyPart.base.BodyCondition.OINTMENT;

public class AntisepticSprayer extends AbstractPartlyHealItem {
    public AntisepticSprayer(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean healOn(@NotNull LivingEntity source, @NotNull LivingEntity entity, BodyComponents component) {
        return HealthCapability.getAndApply(entity, h -> {
            var body = h.getComponent(component);
            body.healing(OINTMENT, 0.05f);
            body.healing(INFECTION, -ConditionAccessor.get(INFECTION).maxValue());
            return true;
        }, false);
    }

    @Override
    public boolean available(LivingEntity target, ItemStack stack) {
        if (stack.getDamageValue() >= stack.getMaxDamage() - 1) {
            return false;
        }
        return super.available(target, stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("治疗"));
        tooltipComponents.add(Component.literal("·感染").withStyle(ChatFormatting.BLUE));
    }
}
