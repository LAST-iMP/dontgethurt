package com.lastimp.dgh.source.item.medicine;

import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.healingItems.AbstractPartlyHealItem;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.PNEUMOTHORAX;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.PNEUMOTHORAX_NEEDLE;

public class Needle extends AbstractPartlyHealItem {
    public Needle(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean healOn(@NotNull LivingEntity source, @NotNull LivingEntity entity, BodyComponents component) {
        return HealthCapability.getAndApply(entity, health -> {
            AbstractBody body = health.getComponent(component);
            if (!body.abnormal(PNEUMOTHORAX)) return false;
            body.setConditionValue(PNEUMOTHORAX_NEEDLE, BodyCondition.get(PNEUMOTHORAX_NEEDLE).maxValue());
            return true;
        }, false);
    }

    @Override
    protected void initComponents() {
        applicableComponents.add(BodyComponents.TORSO);
    }
}
