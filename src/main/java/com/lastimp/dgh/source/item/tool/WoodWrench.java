package com.lastimp.dgh.source.item.tool;

import com.lastimp.dgh.api.bodyPart.AbstractExtremities;
import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.healingItems.AbstractPartlyHealItem;
import com.lastimp.dgh.source.core.Utils;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.DISLOCATION;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.INTERNAL_INJURY;

public class WoodWrench extends AbstractPartlyHealItem {
    public WoodWrench(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean healOn(@NotNull LivingEntity source, @NotNull LivingEntity entity, BodyComponents component) {
        return HealthCapability.getAndApply(entity, health -> {
            AbstractExtremities body = (AbstractExtremities) health.getComponent(component);

            body.injury(INTERNAL_INJURY, 0.05f);
            if (Utils.randomCheck(0.2f, 0.0f, 1.0f, 0.0f, 1f))
                return true;
            body.healing(DISLOCATION, -BodyCondition.get(DISLOCATION).maxValue());
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

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("治疗"));
        tooltipComponents.add(Component.literal("·脱臼").withStyle(ChatFormatting.BLUE));
        tooltipComponents.add(Component.literal("造成"));
        tooltipComponents.add(Component.literal("·内伤").withStyle(ChatFormatting.RED));
        tooltipComponents.add(Component.literal("·剧痛").withStyle(ChatFormatting.RED));
    }
}
