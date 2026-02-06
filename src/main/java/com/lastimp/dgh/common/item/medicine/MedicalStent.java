package com.lastimp.dgh.common.item.medicine;

import com.lastimp.dgh.common.capability.bodyPart.ConditionAccessor;
import com.lastimp.dgh.common.capability.bodyPart.base.AbstractBody;
import com.lastimp.dgh.common.enums.BodyComponents;
import com.lastimp.dgh.common.item.bases.AbstractPartlyHealItem;
import com.lastimp.dgh.common.capability.HealthCapability;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.lastimp.dgh.common.capability.bodyPart.base.BodyCondition.AORTIC_RUPTURE;
import static com.lastimp.dgh.common.capability.bodyPart.base.BodyCondition.RETRACTED_SKIN;

public class MedicalStent extends AbstractPartlyHealItem {
    public MedicalStent(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean healOn(@NotNull LivingEntity source, @NotNull LivingEntity entity, BodyComponents component) {
        return HealthCapability.getAndApply(entity, health -> {
            AbstractBody body = health.getComponent(component);
            if (!body.abnormal(RETRACTED_SKIN) || !body.abnormal(AORTIC_RUPTURE)) return false;
            body.setConditionValue(AORTIC_RUPTURE, ConditionAccessor.get(AORTIC_RUPTURE).defaultValue());
            return true;
        }, false);
    }

    @Override
    protected void initComponents() {
        applicableComponents.add(BodyComponents.TORSO);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("治疗"));
        tooltipComponents.add(Component.literal("·主动脉破裂").withStyle(ChatFormatting.BLUE));
        tooltipComponents.add(Component.literal("需要"));
        tooltipComponents.add(Component.literal("·皮肤牵开").withStyle(ChatFormatting.GREEN));
    }
}
