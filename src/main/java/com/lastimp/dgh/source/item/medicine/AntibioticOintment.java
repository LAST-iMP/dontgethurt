package com.lastimp.dgh.source.item.medicine;

import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.healingItems.AbstractPartlyHealItem;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

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
    public @NotNull ItemStack getCraftingRemainder(ItemStack itemStack) {
        var remaining = itemStack.copy();
        remaining.setDamageValue(itemStack.getDamageValue() + 1);
        return remaining.getDamageValue() >= remaining.getMaxDamage()
                ? ItemStack.EMPTY
                : remaining;
    }
}
