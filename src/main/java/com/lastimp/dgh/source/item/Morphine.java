
package com.lastimp.dgh.source.item;

import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.source.core.bodyPart.Head;
import com.lastimp.dgh.source.core.bodyPart.PlayerBlood;
import com.lastimp.dgh.source.core.bodyPart.Torso;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import com.lastimp.dgh.api.healingItems.AbstractDirectHealItems;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import static com.lastimp.dgh.api.enums.BodyComponents.*;
import static com.lastimp.dgh.api.enums.BodyCondition.*;

public class Morphine extends AbstractDirectHealItems {

    public Morphine(Properties properties) {
        super(properties);
    }

    @Override
    public boolean heal(@NotNull ServerPlayer source, @NotNull ServerPlayer target) {
        return PlayerHealthCapability.getAndSet(source, h -> {
            Torso torso = (Torso) h.getComponent(TORSO);
            Head head = (Head) h.getComponent(HEAD);
            PlayerBlood blood = (PlayerBlood) h.getComponent(BLOOD);

            torso.healing(ANALGESIA, 0.5f);
            head.healing(WITHDRAW, -0.3f);
            blood.injury(OPIATE_ADDICTED, 0.1f);
            blood.injury(OPIATE_OVERDOSE, 0.1f);
            return false;
        });
    }

    @Override
    protected BodyComponents getAvaComponent() {
        return TORSO;
    }
}
