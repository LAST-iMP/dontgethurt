package com.lastimp.dgh.source.item.medicine;

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

import static com.lastimp.dgh.api.bodyPart.BodyCondition.INFECTION;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.OINTMENT;

public class AntibioticOintment extends AbstractPartlyHealItem {
    public AntibioticOintment(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean healOn(@NotNull LivingEntity source, @NotNull LivingEntity entity, BodyComponents component) {
        return HealthCapability.getAndApply(entity, h -> {
            var body = h.getComponent(component);
            if (body.getConditionValue(OINTMENT) > 0.8f) return false;
            body.healing(OINTMENT, BodyCondition.get(OINTMENT).maxValue());
            body.healing(INFECTION, -0.6f);
            return true;
        }, false);
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return stack.getDamageValue() < stack.getMaxDamage() - 1;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        var remaining = itemStack.copy();
        remaining.setDamageValue(itemStack.getDamageValue() + 1);
        return remaining.getDamageValue() >= remaining.getMaxDamage()
                ? ItemStack.EMPTY
                : remaining;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("治疗"));
        tooltipComponents.add(Component.literal("·感染").withStyle(ChatFormatting.BLUE));
    }
}
