package com.lastimp.dgh.api.healingItems;

import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.RETRACTED_SKIN;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.SURGICAL_AMPUTATION;

public abstract class AbstractLimbs extends AbstractPartlyHealItem {
    public AbstractLimbs(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean healOn(@NotNull LivingEntity source, @NotNull LivingEntity entity, BodyComponents component) {
        return HealthCapability.getAndApply(entity, h -> {
            AbstractVisibleBody body = (AbstractVisibleBody) h.getComponent(component);
            if (!body.abnormal(SURGICAL_AMPUTATION)) return false;
            if (!body.abnormal(RETRACTED_SKIN)) return false;
            body.healing(SURGICAL_AMPUTATION, -BodyCondition.get(SURGICAL_AMPUTATION).maxValue());
            this.addLimb(h, body);
            return true;
        }, false);
    }

    protected abstract void addLimb(@NotNull HealthCapability health, @NotNull AbstractVisibleBody body);
}
