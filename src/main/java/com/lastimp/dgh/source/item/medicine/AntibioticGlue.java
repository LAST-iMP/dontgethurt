package com.lastimp.dgh.source.item.medicine;

import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.healingItems.AbstractPartlyHealItem;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;

public class AntibioticGlue extends AbstractPartlyHealItem {
    public AntibioticGlue(Item.Properties properties) {
        super(properties);
    }

    @Override
    protected boolean healOn(@NotNull ServerPlayer source, @NotNull LivingEntity entity, BodyComponents component) {
        return HealthCapability.getAndSet(entity, h -> {
            var body = h.getComponent(component);
            if (body.getConditionValue(OINTMENT) > 0.95f) return false;
            body.healing(BURN, -0.15f);
            body.healing(INFECTION, -1f);
            return true;
        });
    }
}
