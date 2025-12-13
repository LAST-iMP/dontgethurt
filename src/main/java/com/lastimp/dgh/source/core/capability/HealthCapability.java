
package com.lastimp.dgh.source.core.capability;

import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.bodyPart.AbstractExtremities;
import com.lastimp.dgh.source.register.ModCapabilities;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.source.core.bodyPart.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.function.Function;

import static com.lastimp.dgh.api.enums.BodyComponents.*;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.INTENSE_PAIN;

public class HealthCapability implements INBTSerializable<CompoundTag> {
    private static final HealthCapability EMPTY = new HealthCapability();
    private final WholeBody body = new WholeBody();
    private float vitality = 1.0f;
    private int slowDown = 0;
    private int armBreak = 0;
    private long livingTick = 0;
    private float almostDead = 1.0f;
    private int nearBedTick = 0;

    public static HealthCapability get(LivingEntity entity) {
        return entity.getCapability(ModCapabilities.HEALTH).orElse(new HealthCapability());
    }

    public static void reset(LivingEntity entity) {
        var health = get(entity);
        health.deserializeNBT(EMPTY.serializeNBT());
    }

    public static <T> T getAndSet(LivingEntity entity, Function<HealthCapability, T> function) {
        HealthCapability health = HealthCapability.get(entity);
        return function.apply(health);
    }

    public AbstractBody getComponent(BodyComponents component) {
        return this.body.getComponent(component);
    }

    public HealthCapability update(LivingEntity entity) {
        this.body.update(this, entity);
        this.updateLabels(entity);
        return this;
    }

    private void updateLabels(LivingEntity entity) {
        if (this.livingTick + 1 > 0) this.livingTick++;
        this.armBreak = (AbstractExtremities.available(this, LEFT_ARM) ? 0 : 1) + (AbstractExtremities.available(this, RIGHT_ARM) ? 0 : 1);
        this.slowDown = this.body.slowDownLevel(this);
        this.vitality = 1.0f - this.body.updateVitalityLost(this, entity);

        float bloodVitalityLost = this.getComponent(BLOOD).updateVitalityLost(this, entity);
        if (this.vitality + bloodVitalityLost < this.almostDead)
            this.almostDead = this.vitality + bloodVitalityLost;
        this.nearBedTick--;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = this.body.serializeNBT();
        tag.putFloat("playerVitality", this.vitality);
        tag.putInt("slowDown", this.slowDown);
        tag.putInt("armBreak", this.armBreak);
        tag.putLong("livingTick", this.livingTick);
        tag.putFloat("almostDead", this.almostDead);
        tag.putInt("nearBedTick", this.nearBedTick);

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (nbt == null) return;
        this.body.deserializeNBT(nbt);
        this.vitality = nbt.getFloat("playerVitality");
        this.slowDown = nbt.getInt("slowDown");
        this.armBreak = nbt.getInt("armBreak");
        this.livingTick = nbt.getLong("livingTick");
        this.almostDead = nbt.getFloat("almostDead");
        this.nearBedTick = nbt.getInt("nearBedTick");
    }

    public boolean intensePain() {
        return  this.getComponent(LEFT_ARM).abnormal(INTENSE_PAIN) ||
                this.getComponent(RIGHT_ARM).abnormal(INTENSE_PAIN) ||
                this.getComponent(LEFT_LEG).abnormal(INTENSE_PAIN) ||
                this.getComponent(RIGHT_LEG).abnormal(INTENSE_PAIN);
    }

    public static boolean isDying(Player player) {
        return player.getHealth() < 0.05 && !player.isDeadOrDying();
    }

    public boolean safeSurgery() {
        return this.nearBedTick > 0 || ((Torso)this.getComponent(TORSO)).safeSurgery();
    }

    public float vitality() {
        return vitality;
    }

    public int slowDown() {
        return slowDown;
    }

    public float almostDead() {
        return almostDead;
    }

    public void resetAlmostDead() {
        this.almostDead = 1.0f;
    }

    public long livingTick() {
        return livingTick;
    }

    public int armBreak() {
        return armBreak;
    }

    public int nearBedTick() {
        return nearBedTick;
    }

    public void setNearBedTick(int nearBedTick) {
        this.nearBedTick = nearBedTick;
    }
}
