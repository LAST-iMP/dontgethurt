package com.lastimp.dgh.source.item.medicine;

import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.bodyPart.AbstractExtremities;
import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.healingItems.AbstractPartlyHealItem;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import com.lastimp.dgh.source.register.ModItems;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.PLASTER_CAST;

public class Gypsum extends AbstractPartlyHealItem {
    public Gypsum(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean healOn(@NotNull ServerPlayer source, @NotNull ServerPlayer target, BodyComponents component) {
        return PlayerHealthCapability.getAndSet(target, health -> {
            AbstractExtremities body = (AbstractExtremities) health.getComponent(component);
            if (body.abnormal(PLASTER_CAST)) return false;
            if (!body.isBandaged()) return false;
            if (body.boneCrafted() != null) return false;

            body.healing(PLASTER_CAST, BodyCondition.get(PLASTER_CAST).maxValue());
            return true;
        });
    }

    @Override
    protected void initComponents() {
        applicableComponents.add(BodyComponents.LEFT_ARM);
        applicableComponents.add(BodyComponents.RIGHT_ARM);
        applicableComponents.add(BodyComponents.LEFT_LEG);
        applicableComponents.add(BodyComponents.RIGHT_LEG);
    }

    public static boolean cut(ServerPlayer target, BodyComponents component) {
        if (!((Gypsum)ModItems.GYPSUM.get()).getApplicableComponents().contains(component)) return false;

        return PlayerHealthCapability.getAndSet(target, health -> {
            AbstractBody body = health.getComponent(component);
            if (body.abnormal(PLASTER_CAST)) {
                body.setConditionValue(PLASTER_CAST, BodyCondition.get(PLASTER_CAST).defaultValue());
            } else {
                return false;
            }
            return true;
        });
    }
}
