
package com.lastimp.dgh.source.item.medicine;

import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.bodyPart.AbstractExtremities;
import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.healingItems.AbstractPartlyHealItem;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;

public class Sutures extends AbstractPartlyHealItem {
    private static final Set<ResourceLocation> cover = new HashSet<>();

    public Sutures(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean healOn(@NotNull ServerPlayer source, @NotNull LivingEntity entity, BodyComponents component) {
        return HealthCapability.getAndApply(entity, health -> {
            AbstractBody body = health.getComponent(component);

            boolean success = false;
            if (body.abnormal(OPEN_WOUND)) {
                body.healing(OPEN_WOUND, -0.2f);
                success = true;
            }
            if (body.abnormal(SURGERY_INCISION)) {
                body.setConditionValue(SURGERY_INCISION, BodyCondition.get(SURGERY_INCISION).defaultValue());
                body.setConditionValue(CLAMPED_BLEEDING, BodyCondition.get(CLAMPED_BLEEDING).defaultValue());
                success = true;
            }
            if (body.abnormal(RETRACTED_SKIN)) {
                body.setConditionValue(RETRACTED_SKIN, BodyCondition.get(RETRACTED_SKIN).defaultValue());
                body.setConditionValue(DRILLED_BONES, BodyCondition.get(DRILLED_BONES).defaultValue());
                body.setConditionValue(CLAMPED_ARTERIES, BodyCondition.get(CLAMPED_ARTERIES).defaultValue());
                if (body instanceof AbstractExtremities extremities)
                    extremities.setConditionValue(ARTERIAL_BLEEDING, BodyCondition.get(ARTERIAL_BLEEDING).defaultValue());
                success = true;
            }

            if (success) {
                for (var key : cover) {
                    body.setConditionHidden(key, body.getConditionValue(key) + body.getConditionHidden(key));
                    body.setConditionValue(key, BodyCondition.get(key).defaultValue());
                }
            }
            return success;
        }, false);
    }

    public static void addCoverOnHeal(@NotNull ResourceLocation key) {
        cover.add(key);
    }
}
