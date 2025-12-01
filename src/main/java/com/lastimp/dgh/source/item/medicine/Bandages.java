
package com.lastimp.dgh.source.item.medicine;

import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.enums.BodyCondition;
import com.lastimp.dgh.api.healingItems.AbstractPartlyHealItem;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import static com.lastimp.dgh.api.enums.BodyCondition.*;

public class Bandages extends AbstractPartlyHealItem {

    public Bandages(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean healOn(@NotNull ServerPlayer source, @NotNull ServerPlayer target, BodyComponents component) {
        return PlayerHealthCapability.getAndSet(target, health -> {
            AbstractBody body = health.getComponent(component);
            float currCondition = body.getConditionValue(BANDAGED);
            if (currCondition > 0.75f) return false;

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
