package com.lastimp.dgh.source.item.medicine;

import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.healingItems.AbstractPartlyHealItem;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.INFECTION;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.OINTMENT;

public class AntibioticOintment extends AbstractPartlyHealItem {
    public AntibioticOintment(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean healOn(@NotNull ServerPlayer source, @NotNull LivingEntity entity, BodyComponents component) {
        return HealthCapability.getAndSet(entity, h -> {
            var body = h.getComponent(component);
            if (body.getConditionValue(OINTMENT) > 0.8f) return false;
            body.healing(OINTMENT, BodyCondition.get(OINTMENT).maxValue());
            body.healing(INFECTION, -0.6f);
            return true;
        });
    }
}
