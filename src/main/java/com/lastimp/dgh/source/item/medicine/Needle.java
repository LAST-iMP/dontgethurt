package com.lastimp.dgh.source.item.medicine;

import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.bodyPart.BodyCondition;
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

import static com.lastimp.dgh.api.bodyPart.BodyCondition.PNEUMOTHORAX;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.PNEUMOTHORAX_NEEDLE;

public class Needle extends AbstractPartlyHealItem {
    public Needle(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean healOn(@NotNull LivingEntity source, @NotNull LivingEntity entity, BodyComponents component) {
        return HealthCapability.getAndApply(entity, health -> {
            AbstractBody body = health.getComponent(component);
            if (!body.abnormal(PNEUMOTHORAX)) return false;
            body.setConditionValue(PNEUMOTHORAX_NEEDLE, BodyCondition.get(PNEUMOTHORAX_NEEDLE).maxValue());
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
        tooltipComponents.add(Component.literal("·气胸").withStyle(ChatFormatting.BLUE));
    }
}
