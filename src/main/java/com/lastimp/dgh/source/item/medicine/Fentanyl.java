package com.lastimp.dgh.source.item.medicine;

import com.lastimp.dgh.api.healingItems.AbstractDirectHealItems;
import com.lastimp.dgh.source.core.bodyPart.Blood;
import com.lastimp.dgh.source.core.bodyPart.Head;
import com.lastimp.dgh.source.core.bodyPart.Torso;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;
import static com.lastimp.dgh.api.enums.BodyComponents.*;

public class Fentanyl extends AbstractDirectHealItems {
    public Fentanyl(Properties properties) {
        super(properties);
    }

    @Override
    public boolean heal(@NotNull LivingEntity source, @NotNull LivingEntity entity) {
        return HealthCapability.getAndApply(entity, h -> {
            Torso torso = (Torso) h.getComponent(TORSO);
            Head head = (Head) h.getComponent(HEAD);
            Blood blood = (Blood) h.getComponent(BLOOD);

            torso.healing(ANALGESIA, 1f);
            head.healing(WITHDRAW, -1f);
            blood.injury(OPIATE_ADDICTED, 0.15f);
            blood.injury(OPIATE_OVERDOSE, 0.225f);
            return true;
        }, false);
    }
}
