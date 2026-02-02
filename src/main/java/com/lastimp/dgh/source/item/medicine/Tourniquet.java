package com.lastimp.dgh.source.item.medicine;

import com.lastimp.dgh.api.bodyPart.ConditionAccessor;
import com.lastimp.dgh.source.core.bodyPart.base.AbstractBody;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.healingItems.AbstractPartlyHealItem;
import com.lastimp.dgh.source.core.bodyPart.Head;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.lastimp.dgh.source.core.bodyPart.base.BodyCondition.BRAIN_DAMAGE;
import static com.lastimp.dgh.source.core.bodyPart.base.BodyCondition.CLAMPED_ARTERIES;

public class Tourniquet extends AbstractPartlyHealItem {
    public Tourniquet(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean healOn(@NotNull LivingEntity source, @NotNull LivingEntity entity, BodyComponents component) {
        return HealthCapability.getAndApply(entity, health -> {
            AbstractBody body = health.getComponent(component);
            if (body.abnormal(CLAMPED_ARTERIES)) return false;

            body.setConditionValue(CLAMPED_ARTERIES, ConditionAccessor.get(CLAMPED_ARTERIES).maxValue());
            if (body instanceof Head head) {
                head.injury(BRAIN_DAMAGE, 0.15f);
                entity.setAirSupply(0);
            }
            return true;
        }, false);
    }

    @Override
    protected void initComponents() {
        applicableComponents.add(BodyComponents.HEAD);
        applicableComponents.add(BodyComponents.LEFT_ARM);
        applicableComponents.add(BodyComponents.RIGHT_ARM);
        applicableComponents.add(BodyComponents.LEFT_LEG);
        applicableComponents.add(BodyComponents.RIGHT_LEG);
    }

    public static boolean cut(LivingEntity target, BodyComponents component) {
        return HealthCapability.getAndApply(target, health -> {
            AbstractBody body = health.getComponent(component);
            if (body.abnormal(CLAMPED_ARTERIES)) {
                body.setConditionValue(CLAMPED_ARTERIES, ConditionAccessor.get(CLAMPED_ARTERIES).defaultValue());
            } else {
                return false;
            }
            return true;
        }, false);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("治疗"));
        tooltipComponents.add(Component.literal("·动脉出血").withStyle(ChatFormatting.BLUE));
        tooltipComponents.add(Component.literal("造成"));
        tooltipComponents.add(Component.literal("·坏疽").withStyle(ChatFormatting.RED));
        tooltipComponents.add(Component.literal("·脑损伤（头）").withStyle(ChatFormatting.RED));
        tooltipComponents.add(Component.literal("·缺氧（头）").withStyle(ChatFormatting.RED));
    }
}
