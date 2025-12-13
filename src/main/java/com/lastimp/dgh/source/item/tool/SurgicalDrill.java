package com.lastimp.dgh.source.item.tool;

import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.healingItems.AbstractPartlyHealItem;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.DRILLED_BONES;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.RETRACTED_SKIN;

public class SurgicalDrill extends AbstractPartlyHealItem {
    public SurgicalDrill(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean healOn(@NotNull ServerPlayer source, @NotNull ServerPlayer target, BodyComponents component) {
        return HealthCapability.getAndSet(target, (h) -> {
            AbstractVisibleBody body = (AbstractVisibleBody) h.getComponent(component);
            if (!body.abnormal(RETRACTED_SKIN)) return false;
            if (body.abnormal(DRILLED_BONES)) return false;

            body.setConditionValue(DRILLED_BONES, BodyCondition.get(DRILLED_BONES).maxValue());
            return true;
        });
    }
}
