
package com.lastimp.dgh.source.core.capability;

import com.lastimp.dgh.Config;
import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.bodyPart.AbstractExtremities;
import com.lastimp.dgh.api.healingItems.AbstractHealingEquipment;
import com.lastimp.dgh.api.tags.ModTags;
import com.lastimp.dgh.source.core.menu.component.DynamicItemHandler;
import com.lastimp.dgh.source.core.Utils;
import com.lastimp.dgh.source.register.ModCapabilities;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.source.core.bodyPart.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.function.Function;

import static com.lastimp.dgh.DontGetHurt.DELTA;
import static com.lastimp.dgh.api.enums.BodyComponents.*;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;

public class HealthCapability implements INBTSerializable<CompoundTag> {
    private final WholeBody body = new WholeBody();
    private final DynamicItemHandler oxygenMask = new DynamicItemHandler();
    private final DynamicItemHandler autoPulse = new DynamicItemHandler();
    private float vitality = 1.0f;
    private int slowDown = 0;
    private int armBreak = 0;
    private long livingTick = 0;
    private float almostDead = 1.0f;
    private int nearBedTick = 0;
    private float outerHealing = 0;
    private float outerHealingDelta = 0;
    private boolean isInfected = false;
    private int oxygenMaskCoolDown = 0;
    private int autoPulseCoolDown = 0;

    public HealthCapability() {
        oxygenMask.addAllowed(ModTags.OXYGEN_SUPPLIERS);
        autoPulse.addAllowed(ModTags.AUTOPULSE);
    }

    public static boolean has(LivingEntity entity) {
        return HealthProvider.has(entity);
    }

    public static HealthCapability get(LivingEntity entity) {
        if (has(entity)) return entity.getData(ModCapabilities.HEALTH.get());
        else return null;
    }

    public static void set(LivingEntity entity, HealthCapability capability) {
        entity.setData(ModCapabilities.HEALTH, capability);
    }

    public static <T> T getAndSet(LivingEntity entity, Function<HealthCapability, T> function) {
        HealthCapability health = HealthCapability.get(entity);
        T result = function.apply(health);
        HealthCapability.set(entity, health);
        return result;
    }

    public AbstractBody getComponent(BodyComponents component) {
        return this.body.getComponent(component);
    }

    public HealthCapability update(LivingEntity entity) {
        this.updateALLEquipments(entity);
        this.body.update(this, entity);
        this.updateLabels(entity);
        return this;
    }

    public void updateALLEquipments(LivingEntity entity) {
        if (updateEquipment(entity, this.oxygenMask, this.oxygenMaskCoolDown)) {
            this.oxygenMaskCoolDown = this.getCoolDown(this.oxygenMask);
            this.oxygenMask.getStackInSlot(0).hurtAndBreak(1, Utils.randomSource, entity, () -> {});
        } else if (this.oxygenMaskCoolDown > 0) {
            this.oxygenMaskCoolDown--;
        }
        if (updateEquipment(entity, this.autoPulse, this.autoPulseCoolDown)) {
            this.autoPulseCoolDown = this.getCoolDown(this.autoPulse);
            this.autoPulse.getStackInSlot(0).hurtAndBreak(1, Utils.randomSource, entity, () -> {});
        } else if (this.autoPulseCoolDown > 0) {
            this.autoPulseCoolDown--;
        }
    }

    private boolean updateEquipment(LivingEntity entity, DynamicItemHandler handler, int cooldown) {
        var equip = handler.getStackInSlot(0);
        if (equip.isEmpty() || cooldown > 0) return false;
        if (equip.getDamageValue() >= equip.getMaxDamage()) return false;
        return ((AbstractHealingEquipment)equip.getItem()).heal(entity);
    }

    private int getCoolDown(DynamicItemHandler handler) {
        return ((AbstractHealingEquipment)handler.getStackInSlot(0).getItem()).getMaxCooldown();
    }

    private void updateLabels(LivingEntity entity) {
        if (this.livingTick + 1 > 0) this.livingTick++;
        this.armBreak = (AbstractExtremities.available(this, LEFT_ARM) ? 0 : 1) + (AbstractExtremities.available(this, RIGHT_ARM) ? 0 : 1);
        this.slowDown = this.body.slowDownLevel(this);
        this.vitality = 1.0f - this.body.updateVitalityLost(this, entity);
        this.vitality = (this.vitality > 0.999f) ? 1.0f : this.vitality;
        this.almostDead = Math.min(this.almostDead, this.vitality);
        this.nearBedTick--;
        this.outerHealing = Math.max(0, this.outerHealing - this.outerHealingDelta);
        this.outerHealingDelta = this.outerHealing <= 0 ? 0 : Math.min(DELTA, this.outerHealing + DELTA / Config.baseHealingShieldTime);
        this.isInfected = this.body.isInfected();
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = this.body.serializeNBT(provider);
        tag.putFloat("playerVitality", this.vitality);
        tag.putInt("slowDown", this.slowDown);
        tag.putInt("armBreak", this.armBreak);
        tag.putLong("livingTick", this.livingTick);
        tag.putFloat("almostDead", this.almostDead);
        tag.putInt("nearBedTick", this.nearBedTick);
        tag.putFloat("outerHealing", this.outerHealing);
        tag.putFloat("outerHealingDelta", this.outerHealingDelta);
        tag.putBoolean("isInfected", this.isInfected);
        tag.put("oxygenMask", this.oxygenMask.serializeNBT(provider));
        tag.put("autoPulse", this.autoPulse.serializeNBT(provider));
        tag.putInt("oxygenMaskCoolDown", this.oxygenMaskCoolDown);
        tag.putInt("autoPulseCoolDown", this.autoPulseCoolDown);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        if (nbt == null) return;
        this.body.deserializeNBT(provider, nbt);
        this.vitality = nbt.getFloat("playerVitality");
        this.slowDown = nbt.getInt("slowDown");
        this.armBreak = nbt.getInt("armBreak");
        this.livingTick = nbt.getLong("livingTick");
        this.almostDead = nbt.getFloat("almostDead");
        this.nearBedTick = nbt.getInt("nearBedTick");
        this.outerHealing = nbt.getFloat("outerHealing");
        this.outerHealingDelta = nbt.getFloat("outerHealingDelta");
        this.isInfected = nbt.getBoolean("isInfected");
        this.oxygenMask.deserializeNBT(provider, nbt.getCompound("oxygenMask"));
        this.autoPulse.deserializeNBT(provider, nbt.getCompound("autoPulse"));
        this.oxygenMaskCoolDown = nbt.getInt("oxygenMaskCoolDown");
        this.autoPulseCoolDown = nbt.getInt("autoPulseCoolDown");
    }

    public boolean intensePain() {
        return  this.getComponent(LEFT_ARM).abnormal(INTENSE_PAIN) ||
                this.getComponent(RIGHT_ARM).abnormal(INTENSE_PAIN) ||
                this.getComponent(LEFT_LEG).abnormal(INTENSE_PAIN) ||
                this.getComponent(RIGHT_LEG).abnormal(INTENSE_PAIN);
    }

    public static boolean isDying(LivingEntity entity) {
        var health = HealthCapability.get(entity);
        if (health != null && health.getComponent(HEAD).abnormal(COMA)) return true;
        return entity.getHealth() < 0.05 && !entity.isDeadOrDying();
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

    public float outerHealing() {
        return outerHealing;
    }

    public void setOuterHealing(float outerHealing) {
        this.outerHealing = outerHealing;
    }

    public boolean isInfected() {
        return this.isInfected;
    }

    public DynamicItemHandler oxygenMask() {
        return oxygenMask;
    }

    public DynamicItemHandler autoPulse() {
        return autoPulse;
    }

    public int autoPulseCoolDown() {
        return autoPulseCoolDown;
    }

    public int oxygenMaskCoolDown() {
        return oxygenMaskCoolDown;
    }
}
