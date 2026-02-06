package com.lastimp.dgh.common.item.medicine;

import com.lastimp.dgh.common.capability.bodyPart.ConditionAccessor;
import com.lastimp.dgh.common.capability.bodyPart.base.AbstractBody;
import com.lastimp.dgh.common.capability.bodyPart.base.AbstractExtremities;
import com.lastimp.dgh.common.enums.BodyComponents;
import com.lastimp.dgh.common.item.bases.AbstractPartlyHealItem;
import com.lastimp.dgh.common.capability.HealthCapability;
import com.lastimp.dgh.common.entry.register.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.lastimp.dgh.common.capability.bodyPart.base.BodyCondition.CLAMP_PLATE;

public class Clamp extends AbstractPartlyHealItem {
    public Clamp(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean healOn(@NotNull LivingEntity source, @NotNull LivingEntity entity, BodyComponents component) {
        return HealthCapability.getAndApply(entity, health -> {
            AbstractExtremities body = (AbstractExtremities) health.getComponent(component);
            if (body.abnormal(CLAMP_PLATE)) return false;

            body.healing(CLAMP_PLATE, ConditionAccessor.get(CLAMP_PLATE).maxValue());
            return true;
        }, false);
    }

    @Override
    protected void initComponents() {
        applicableComponents.add(BodyComponents.LEFT_ARM);
        applicableComponents.add(BodyComponents.RIGHT_ARM);
        applicableComponents.add(BodyComponents.LEFT_LEG);
        applicableComponents.add(BodyComponents.RIGHT_LEG);
    }

    public static boolean cut(LivingEntity target, BodyComponents component) {
        if (!ModItems.CLAMP.get().getApplicableComponents().contains(component)) return false;

        return HealthCapability.getAndApply(target, health -> {
            AbstractBody body = health.getComponent(component);
            if (body.abnormal(CLAMP_PLATE)) {
                body.setConditionValue(CLAMP_PLATE, ConditionAccessor.get(CLAMP_PLATE).defaultValue());
            } else {
                return false;
            }
            return true;
        }, false);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("稳定"));
        tooltipComponents.add(Component.literal("·骨折").withStyle(ChatFormatting.BLUE));
    }
}
