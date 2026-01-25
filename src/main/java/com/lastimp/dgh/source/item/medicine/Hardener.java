package com.lastimp.dgh.source.item.medicine;

import com.lastimp.dgh.api.healingItems.AbstractDirectHealItems;
import com.lastimp.dgh.source.core.bodyPart.Blood;
import com.lastimp.dgh.source.core.bodyPart.Head;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;
import static com.lastimp.dgh.api.enums.BodyComponents.BLOOD;
import static com.lastimp.dgh.api.enums.BodyComponents.HEAD;

public class Hardener extends AbstractDirectHealItems {
    public Hardener(Properties properties) {
        super(properties);
    }

    @Override
    public boolean heal(@NotNull LivingEntity source, @NotNull LivingEntity entity) {
        return HealthCapability.getAndApply(entity, h -> {
            Head head = (Head) h.getComponent(HEAD);
            Blood blood = (Blood) h.getComponent(BLOOD);

            head.healing(WITHDRAW, -0.9f);
            blood.injury(OPIATE_ADDICTED, 0.18f);
            blood.setConditionValue(HARDENER, 1.0f);
            return true;
        }, false);
    }
}
