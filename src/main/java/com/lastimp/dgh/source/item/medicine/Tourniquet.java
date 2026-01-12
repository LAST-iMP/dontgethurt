package com.lastimp.dgh.source.item.medicine;

import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.healingItems.AbstractPartlyHealItem;
import com.lastimp.dgh.source.core.bodyPart.Head;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.BRAIN_DAMAGE;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.CLAMPED_ARTERIES;

public class Tourniquet extends AbstractPartlyHealItem {
    public Tourniquet(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean healOn(@NotNull ServerPlayer source, @NotNull LivingEntity entity, BodyComponents component) {
        return HealthCapability.getAndApply(entity, health -> {
            AbstractBody body = health.getComponent(component);
            if (body.abnormal(CLAMPED_ARTERIES)) return false;

            body.setConditionValue(CLAMPED_ARTERIES, BodyCondition.get(CLAMPED_ARTERIES).maxValue());
            if (body instanceof Head head) {
                head.injury(BRAIN_DAMAGE, 0.15f);
                entity.setAirSupply(0);
            }
            return true;
        }, false);
    }

    @Override
    protected void initComponents() {
        applicableComponents.add(BodyComponents.HEAD);
        applicableComponents.add(BodyComponents.LEFT_ARM);
        applicableComponents.add(BodyComponents.RIGHT_ARM);
        applicableComponents.add(BodyComponents.LEFT_LEG);
        applicableComponents.add(BodyComponents.RIGHT_LEG);
    }

    public static boolean cut(LivingEntity target, BodyComponents component) {
        return HealthCapability.getAndApply(target, health -> {
            AbstractBody body = health.getComponent(component);
            if (body.abnormal(CLAMPED_ARTERIES)) {
                body.setConditionValue(CLAMPED_ARTERIES, BodyCondition.get(CLAMPED_ARTERIES).defaultValue());
            } else {
                return false;
            }
            return true;
        }, false);
    }
}
