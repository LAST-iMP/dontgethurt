package com.lastimp.dgh.source.item.tool;

import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.healingItems.AbstractPartlyHealItem;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.SURGERY_INCISION;

public class Scalpel extends AbstractPartlyHealItem {
    private static final Set<ResourceLocation> discover = new HashSet<>();

    public Scalpel(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean healOn(@NotNull LivingEntity source, @NotNull LivingEntity entity, BodyComponents component) {
        return HealthCapability.getAndApply(entity, (h) -> {
            AbstractVisibleBody body = (AbstractVisibleBody) h.getComponent(component);
            if (body.abnormal(SURGERY_INCISION)) return false;

            body.setConditionValue(SURGERY_INCISION, BodyCondition.get(SURGERY_INCISION).maxValue());

            for (var key : discover) {
                body.setConditionValue(key, body.getConditionHidden(key) + body.getConditionValue(key));
                body.setConditionHidden(key, BodyCondition.get(key).defaultValue());
            }
            return true;
        }, false);
    }

    public static void addDiscoverOnHeal(@NotNull ResourceLocation key) {
        discover.add(key);
    }

}
