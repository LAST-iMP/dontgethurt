package com.lastimp.dgh.source.item.tool;

import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.healingItems.AbstractPartlyHealItem;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.INFECTION;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.OINTMENT;

public class AntisepticSprayer extends AbstractPartlyHealItem {
    public AntisepticSprayer(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean healOn(@NotNull ServerPlayer source, @NotNull LivingEntity entity, BodyComponents component) {
        return HealthCapability.getAndSet(entity, h -> {
            var body = h.getComponent(component);
            body.healing(OINTMENT, 0.05f);
            body.healing(INFECTION, -BodyCondition.get(INFECTION).maxValue());
            return true;
        });
    }

    @Override
    public boolean available(ItemStack stack) {
        if (stack.getDamageValue() >= stack.getMaxDamage() - 1) {
            return false;
        }
        return super.available(stack);
    }
}
