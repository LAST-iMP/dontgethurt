package com.lastimp.dgh.source.item.medicine;

import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.bodyPart.AbstractExtremities;
import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.healingItems.AbstractPartlyHealItem;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.register.ModItems;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.CLAMP_PLATE;

public class Clamp extends AbstractPartlyHealItem {
    public Clamp(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean healOn(@NotNull LivingEntity source, @NotNull LivingEntity entity, BodyComponents component) {
        return HealthCapability.getAndApply(entity, health -> {
            AbstractExtremities body = (AbstractExtremities) health.getComponent(component);
            if (body.abnormal(CLAMP_PLATE)) return false;

            body.healing(CLAMP_PLATE, BodyCondition.get(CLAMP_PLATE).maxValue());
            return true;
        }, false);
    }

    @Override
    protected void initComponents() {
        applicableComponents.add(BodyComponents.LEFT_ARM);
        applicableComponents.add(BodyComponents.RIGHT_ARM);
        applicableComponents.add(BodyComponents.LEFT_LEG);
        applicableComponents.add(BodyComponents.RIGHT_LEG);
    }

    public static boolean cut(LivingEntity target, BodyComponents component) {
        if (!ModItems.CLAMP.get().getApplicableComponents().contains(component)) return false;

        return HealthCapability.getAndApply(target, health -> {
            AbstractBody body = health.getComponent(component);
            if (body.abnormal(CLAMP_PLATE)) {
                body.setConditionValue(CLAMP_PLATE, BodyCondition.get(CLAMP_PLATE).defaultValue());
            } else {
                return false;
            }
            return true;
        }, false);
    }
}
