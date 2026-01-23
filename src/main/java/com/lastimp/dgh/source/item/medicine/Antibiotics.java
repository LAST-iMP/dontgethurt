package com.lastimp.dgh.source.item.medicine;

import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.healingItems.AbstractDirectHealItems;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.ANTIBIOTICS;

public class Antibiotics extends AbstractDirectHealItems {
    public Antibiotics(Properties properties) {
        super(properties);
    }

    @Override
    public boolean heal(@NotNull LivingEntity source, @NotNull LivingEntity entity) {
        return HealthCapability.getAndApply(entity, h -> {
            var blood = h.getComponent(BodyComponents.BLOOD);
            blood.healing(ANTIBIOTICS, 0.5f);
            return true;
        }, false);
    }
}
