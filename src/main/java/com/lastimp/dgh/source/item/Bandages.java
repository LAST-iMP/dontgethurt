
package com.lastimp.dgh.source.item;

import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.bodyPart.ConditionState;
import com.lastimp.dgh.api.enums.BodyCondition;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.healingItems.AbstractPartlyHealItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

import static com.lastimp.dgh.DontGetHurt.DELTA;
import static com.lastimp.dgh.api.enums.BodyCondition.*;

public class Bandages extends AbstractPartlyHealItem {
    private static final HashSet<BodyComponents> applicableComponents = new HashSet<>();

    static {
        applicableComponents.add(BodyComponents.HEAD);
        applicableComponents.add(BodyComponents.TORSO);
        applicableComponents.add(BodyComponents.LEFT_ARM);
        applicableComponents.add(BodyComponents.RIGHT_ARM);
        applicableComponents.add(BodyComponents.LEFT_LEG);
        applicableComponents.add(BodyComponents.RIGHT_LEG);
    }

    public Bandages(Properties properties) {
        super(properties);
    }

    @Override
    public HashSet<BodyComponents> getApplicableComponents() {
        return Bandages.applicableComponents;
    }

    @Override
    protected boolean healOn(@NotNull ServerPlayer source, @NotNull ServerPlayer target, BodyComponents component) {
        if (!this.getApplicableComponents().contains(component)) return false;

        return PlayerHealthCapability.getAndSet(target, health -> {
            AbstractBody body = health.getComponent(component);
            float currCondition = body.getConditionValue(BANDAGED);
            if (currCondition > BANDAGED.healingTS) return false;

            body.healing(BANDAGED, 0.5f);
            body.setConditionValue(BANDAGED_DIRTY, BANDAGED_DIRTY.defaultValue);

            this.coverCondition(body, BURN);
            this.coverCondition(body, OPEN_WOUND);
            this.coverCondition(body, FRACTURE);
            return true;
        });
    }

    protected void coverCondition(AbstractBody body, BodyCondition condition) {
        body.injuryHidden(condition, body.getConditionValue(condition));
        body.setConditionValue(condition, condition.defaultValue);
    }

    public static boolean cut(ServerPlayer target, BodyComponents component) {
        return PlayerHealthCapability.getAndSet(target, health -> {
            AbstractBody body = health.getComponent(component);
            if (BANDAGED.abnormal(body.getConditionValue(BANDAGED))) {
                body.setConditionValue(BANDAGED, BANDAGED.defaultValue);
            } else if (BANDAGED_DIRTY.abnormal(body.getConditionValue(BANDAGED_DIRTY))) {
                body.setConditionValue(BANDAGED_DIRTY, BANDAGED_DIRTY.defaultValue);
            } else {
                return false;
            }
            return true;
        });
    }
}
