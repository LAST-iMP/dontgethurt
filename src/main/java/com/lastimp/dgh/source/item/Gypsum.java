package com.lastimp.dgh.source.item;

import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.healingItems.AbstractPartlyHealItem;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

import static com.lastimp.dgh.api.enums.BodyCondition.FRACTURE;
import static com.lastimp.dgh.api.enums.BodyCondition.PLASTER_CAST;

public class Gypsum extends AbstractPartlyHealItem {
    private static final HashSet<BodyComponents> applicableComponents = new HashSet<>();

    static {
        applicableComponents.add(BodyComponents.HEAD);
        applicableComponents.add(BodyComponents.TORSO);
        applicableComponents.add(BodyComponents.LEFT_ARM);
        applicableComponents.add(BodyComponents.RIGHT_ARM);
        applicableComponents.add(BodyComponents.LEFT_LEG);
        applicableComponents.add(BodyComponents.RIGHT_LEG);
    }

    public Gypsum(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean healOn(@NotNull ServerPlayer source, @NotNull ServerPlayer target, BodyComponents component) {
        if (!this.getApplicableComponents().contains(component)) return false;

        return PlayerHealthCapability.getAndSet(target, health -> {
            AbstractBody body = health.getComponent(component);
            if (PLASTER_CAST.abnormal(body.getConditionValue(PLASTER_CAST))) return false;
            if (FRACTURE.abnormal(body.getConditionValue(PLASTER_CAST))) return false;

            body.healing(PLASTER_CAST, PLASTER_CAST.maxValue);
            return true;
        });
    }

    @Override
    public HashSet<BodyComponents> getApplicableComponents() {
        return applicableComponents;
    }

    public static boolean cut(ServerPlayer target, BodyComponents component) {
        return PlayerHealthCapability.getAndSet(target, health -> {
            AbstractBody body = health.getComponent(component);
            if (PLASTER_CAST.abnormal(body.getConditionValue(PLASTER_CAST))) {
                body.setConditionValue(PLASTER_CAST, PLASTER_CAST.defaultValue);
            } else {
                return false;
            }
            return true;
        });
    }
}
