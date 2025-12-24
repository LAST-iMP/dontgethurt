package com.lastimp.dgh.source.item.medicine;

import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.healingItems.AbstractPartlyHealItem;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;

public class Drainage extends AbstractPartlyHealItem {

    public Drainage(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean healOn(@NotNull ServerPlayer source, @NotNull LivingEntity entity, BodyComponents component) {
        return HealthCapability.getAndSet(entity, health -> {
            AbstractBody body = health.getComponent(component);
            if (!body.abnormal(RETRACTED_SKIN) || !body.abnormal(PNEUMOTHORAX)) return false;
            body.setConditionValue(PNEUMOTHORAX, BodyCondition.get(PNEUMOTHORAX).minValue());
            body.setConditionValue(PNEUMOTHORAX_NEEDLE, BodyCondition.get(PNEUMOTHORAX_NEEDLE).minValue());
            return true;
        });
    }

    @Override
    protected void initComponents() {
        applicableComponents.add(BodyComponents.TORSO);
    }
}
