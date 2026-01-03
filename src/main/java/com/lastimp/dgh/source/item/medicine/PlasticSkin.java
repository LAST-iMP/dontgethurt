package com.lastimp.dgh.source.item.medicine;

import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;

public class PlasticSkin extends Bandages {
    public PlasticSkin(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean healOn(@NotNull ServerPlayer source, @NotNull LivingEntity entity, BodyComponents component) {
        if (super.healOn(source, entity, component)) {
            return HealthCapability.getAndSet(entity, h -> {
                AbstractBody body = h.getComponent(component);
                body.healing(BANDAGED, 0.5f);
                return true;
            });
        } else {
            return HealthCapability.getAndSet(entity, h -> {
                AbstractBody body = h.getComponent(component);
                if (!body.abnormal(SURGERY_INCISION)) return false;
                body.healing(BURN, -0.25f);
                return true;
            });
        }
    }
}
