
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

import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;

public class Bandages extends AbstractPartlyHealItem {

    public Bandages(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean healOn(@NotNull ServerPlayer source, @NotNull LivingEntity entity, BodyComponents component) {
        return HealthCapability.getAndSet(entity, health -> {
            AbstractBody body = health.getComponent(component);
            float currCondition = body.getConditionValue(BANDAGED);
            if (body.abnormal(SURGERY_INCISION)) return false;
            if (currCondition > 0.75f) return false;

            body.healing(BANDAGED, 0.5f);
            body.setConditionValue(BANDAGED_DIRTY, BodyCondition.get(BANDAGED_DIRTY).defaultValue());

            this.coverCondition(body, BURN);
            this.coverCondition(body, OPEN_WOUND);
            this.coverCondition(body, FRACTURE);
            return true;
        });
    }

    protected void coverCondition(AbstractBody body, ResourceLocation condition) {
        body.injuryHidden(condition, body.getConditionValue(condition));
        body.setConditionValue(condition, BodyCondition.get(condition).defaultValue());
    }

    public static boolean cut(LivingEntity target, BodyComponents component) {
        return HealthCapability.getAndSet(target, health -> {
            AbstractBody body = health.getComponent(component);
            if (body.abnormal(BANDAGED)) {
                body.setConditionValue(BANDAGED, BodyCondition.get(BANDAGED).defaultValue());
            } else if (body.abnormal(BANDAGED_DIRTY)) {
                body.setConditionValue(BANDAGED_DIRTY, BodyCondition.get(BANDAGED_DIRTY).defaultValue());
            } else {
                return false;
            }
            return true;
        });
    }
}
