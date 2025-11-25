package com.lastimp.dgh.api.bodyPart;

import com.lastimp.dgh.api.enums.BodyCondition;
import com.lastimp.dgh.source.core.bodyPart.Torso;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

import static com.lastimp.dgh.DontGetHurt.DELTA;
import static com.lastimp.dgh.api.enums.BodyComponents.*;
import static com.lastimp.dgh.api.enums.BodyCondition.*;

public abstract class AbstractExtremities extends AbstractVisibleBody {
    private static List<BodyCondition> EXTREMITY_CONDITIONS;

    public AbstractExtremities() {
        super();
    }

    public AbstractExtremities(Void unused) {
        this();
    }

    @Override
    public float getVitalityWeight() {
        return 0.25f;
    }

    @Override
    public List<BodyCondition> getBodyConditions() {
        if (EXTREMITY_CONDITIONS == null) {
            EXTREMITY_CONDITIONS = new ArrayList<>(super.getBodyConditions());
            EXTREMITY_CONDITIONS.addAll(List.of(
                    DISLOCATION,
                    FRACTURE,
                    INTENSE_PAIN,
                    PLASTER_CAST
            ));
        }
        return EXTREMITY_CONDITIONS;
    }


    @Override
    public AbstractBody update(PlayerHealthCapability health, Player player) {
        super.update(health, player);
        this.handleDislocation(health, player);
        this.handleFracture(health, player);
        this.handleIntensePain(health, player);
        this.handlePlasterCast(health, player);
        return this;
    }

    public int hurtWhenUse() {
        if (this.abnormalWithHidden(FRACTURE)) return 2;
        if (this.abnormal(DISLOCATION)) return 1;
        return 0;
    }

    @Override
    public int slowDownLevel() {
        int slowDown = super.slowDownLevel();
        slowDown += this.abnormal(PLASTER_CAST)? 1 : 0;
        return slowDown;
    }

    private void handleDislocation(PlayerHealthCapability health, Player player) {
        if (!this.abnormal(DISLOCATION)) return;

        Torso torso = (Torso) health.getComponent(TORSO);
        if (!torso.abnormal(ANALGESIA) && !this.isBandaged() && !this.isBadBandaged()) {
            this.setConditionValue(INTENSE_PAIN, INTENSE_PAIN.maxValue);
        }
    }

    private void handleFracture(PlayerHealthCapability health, Player player) {
        if (!this.abnormalWithHidden(FRACTURE)) return;
        this.handleCover(FRACTURE);

        Torso torso = (Torso) health.getComponent(TORSO);
        if (!torso.abnormal(ANALGESIA) && !this.isBandaged() && !this.isBadBandaged()) {
            this.setConditionValue(INTENSE_PAIN, INTENSE_PAIN.maxValue);
        }
    }

    private void handleIntensePain(PlayerHealthCapability health, Player player) {
        if (!this.abnormal(INTENSE_PAIN)) return;

        Torso torso = (Torso) health.getComponent(TORSO);
        if (torso.abnormal(ANALGESIA) || this.isBandaged() || this.isBadBandaged())
            this.healing(INTENSE_PAIN, -INTENSE_PAIN.healingSpeed * DELTA);
    }

    private void handlePlasterCast(PlayerHealthCapability health, Player player) {
        if (!this.abnormal(PLASTER_CAST) || !this.abnormalWithHidden(FRACTURE)) return;

        this.healingHidden(FRACTURE, -FRACTURE.healingSpeed * DELTA);
    }
}
