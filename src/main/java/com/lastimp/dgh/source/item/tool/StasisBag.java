package com.lastimp.dgh.source.item.tool;

import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.healingItems.AbstractHealingEquipment;
import com.lastimp.dgh.source.core.bodyPart.Torso;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.RESPIRATORY_ARREST;

public class StasisBag extends AbstractHealingEquipment {
    public StasisBag(Properties properties) {
        super(properties);
    }

    @Override
    protected BodyComponents getAvaComponent() {
        return BodyComponents.TORSO;
    }

    @Override
    public boolean heal(@NotNull LivingEntity entity) {
        return HealthCapability.getAndSet(entity, h -> {
            Torso torso = (Torso) h.getComponent(BodyComponents.TORSO);
            torso.addHeartRate(3);
            torso.injury(RESPIRATORY_ARREST, BodyCondition.get(RESPIRATORY_ARREST).maxValue());
            return true;
        });
    }

    @Override
    public int getMaxCooldown() {
        return 20;
    }
}
