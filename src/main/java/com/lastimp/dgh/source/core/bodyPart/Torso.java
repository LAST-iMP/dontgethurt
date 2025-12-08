
package com.lastimp.dgh.source.core.bodyPart;

import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.ANALGESIA;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.RESPIRATORY_ARREST;

public class Torso extends AbstractVisibleBody {
    private static List<ResourceLocation> TORSO_CONDITIONS;
    public Torso() {
        super();
    }

    public Torso (Void v) {
        this();
    }

    @Override
    public List<ResourceLocation> getBodyConditions() {
        if (TORSO_CONDITIONS == null) {
            TORSO_CONDITIONS = new ArrayList<>(super.getBodyConditions());
            TORSO_CONDITIONS.addAll(List.of(
                    ANALGESIA,
                    RESPIRATORY_ARREST
            ));
        }
        return TORSO_CONDITIONS;
    }

    @Override
    public float getVitalityWeight() {
        return 0.8f;
    }

    @Override
    public AbstractBody update(PlayerHealthCapability health, Player player) {
        super.update(health, player);
        return this;
    }

    public boolean safeSurgery() {
        return this.abnormal(ANALGESIA);
    }
}
