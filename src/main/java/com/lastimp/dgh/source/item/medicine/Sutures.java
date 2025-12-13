
package com.lastimp.dgh.source.item.medicine;

import com.lastimp.dgh.api.bodyPart.AbstractBody;
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
        return HealthCapability.getAndSet(entity, health -> {
            AbstractBody body = health.getComponent(component);

            boolean success = false;
            if (success |= body.abnormal(OPEN_WOUND))
                body.healing(OPEN_WOUND, -0.2f);
            if (success |= body.abnormal(SURGERY_INCISION))
                body.setConditionValue(SURGERY_INCISION, BodyCondition.get(SURGERY_INCISION).defaultValue());
            if (success |= body.abnormal(CLAMPED_BLEEDING))
                body.setConditionValue(CLAMPED_BLEEDING, BodyCondition.get(CLAMPED_BLEEDING).defaultValue());
            if (success |= body.abnormal(RETRACTED_SKIN))
                body.setConditionValue(RETRACTED_SKIN, BodyCondition.get(RETRACTED_SKIN).defaultValue());
            if (success |= body.abnormal(DRILLED_BONES))
                body.setConditionValue(DRILLED_BONES, BodyCondition.get(DRILLED_BONES).defaultValue());

            if (success) {
                for (var key : cover) {
                    body.setConditionHidden(key, body.getConditionValue(key) + body.getConditionHidden(key));
                    body.setConditionValue(key, BodyCondition.get(key).defaultValue());
                }
            }
            return success;
        });
    }

    public static void addCoverOnHeal(@NotNull ResourceLocation key) {
        cover.add(key);
    }
}
