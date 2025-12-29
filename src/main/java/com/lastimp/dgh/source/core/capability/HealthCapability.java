
package com.lastimp.dgh.source.core.capability;

import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.bodyPart.AbstractExtremities;
import com.lastimp.dgh.api.healingItems.AbstractHealingEquipment;
import com.lastimp.dgh.api.tags.ModTags;
import com.lastimp.dgh.network.message.MyReadAllConditionData;
import com.lastimp.dgh.network.message.Network;
import com.lastimp.dgh.source.core.menu.component.DynamicItemHandler;
import com.lastimp.dgh.source.register.ModCapabilities;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.source.core.bodyPart.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.network.PacketDistributor;

import java.util.UUID;
import java.util.function.Function;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.COMA;
import static com.lastimp.dgh.api.enums.BodyComponents.*;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.INTENSE_PAIN;
import static com.lastimp.dgh.api.enums.OperationType.SYN;

public class HealthCapability implements INBTSerializable<CompoundTag> {
    private static final HealthCapability EMPTY = new HealthCapability();
    private final WholeBody body = new WholeBody();
    private final DynamicItemHandler oxygenMask = new DynamicItemHandler();
    private final DynamicItemHandler autoPulse = new DynamicItemHandler();
    private float vitality = 1.0f;
    private int slowDown = 0;
    private long livingTick = 0;
    private float almostDead = 1.0f;
    private int nearBedTick = 0;
    private float outerHealing = 0;
    private float outerHealingDelta = 0;
    private boolean isInfected = false;
    private int oxygenMaskCoolDown = 0;
    private int autoPulseCoolDown = 0;

    private int armBreak = 0;
    private boolean leftArmVisible = true;
    private boolean rightArmVisible = true;
    private boolean leftLegVisible = true;
    private boolean rightLegVisible = true;

    private UUID lastHealer = UUID.randomUUID();

    private boolean isDirty = true;

    public HealthCapability() {
        oxygenMask.addAllowed(ModTags.OXYGEN_SUPPLIERS);
        autoPulse.addAllowed(ModTags.AUTOPULSE);
    }

    public static boolean has(LivingEntity entity) {
        return HealthProvider.has(entity);
    }

    public static HealthCapability get(LivingEntity entity) {
        if (has(entity)) return entity.getCapability(ModCapabilities.HEALTH, null).orElse(new HealthCapability());
        else return null;
    }

    public static <T> T getAndSet(LivingEntity entity, Function<HealthCapability, T> function) {
        HealthCapability health = HealthCapability.get(entity);
        return function.apply(health);
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
            this.oxygenMask.getStackInSlot(0).hurtAndBreak(1, entity, (i) -> {});
        } else if (this.oxygenMaskCoolDown > 0) {
            this.oxygenMaskCoolDown--;
        }
        if (updateEquipment(entity, this.autoPulse, this.autoPulseCoolDown)) {
            this.autoPulseCoolDown = this.getCoolDown(this.autoPulse);
            this.autoPulse.getStackInSlot(0).hurtAndBreak(1, entity, (i) -> {});
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
        var newArmBreak = (AbstractExtremities.available(this, LEFT_ARM) ? 0 : 1) + (AbstractExtremities.available(this, RIGHT_ARM) ? 0 : 1);
        this.armBreak = updateIfDirty(newArmBreak, this.armBreak);
        this.slowDown = this.body.slowDownLevel(this);
        this.vitality = 1.0f - this.body.updateVitalityLost(this, entity);
        this.vitality = (this.vitality > 0.999f) ? 1.0f : this.vitality;
        this.almostDead = Math.min(this.almostDead, this.vitality);
        this.nearBedTick--;
        this.outerHealing = Math.max(0, this.outerHealing - this.outerHealingDelta);
        this.outerHealingDelta = this.outerHealing <= 0 ? 0 : Math.min(1.0f / 20, this.outerHealing + 1.0f / 60 / 20);
        this.isInfected = this.body.isInfected();

        this.leftArmVisible = updateIfDirty(AbstractExtremities.visible(this, LEFT_ARM), this.leftArmVisible);
        this.rightArmVisible = updateIfDirty(AbstractExtremities.visible(this, RIGHT_ARM), this.rightArmVisible);
        this.leftLegVisible = updateIfDirty(AbstractExtremities.visible(this, LEFT_LEG), this.leftLegVisible);
        this.rightLegVisible = updateIfDirty(AbstractExtremities.visible(this, RIGHT_LEG), this.rightLegVisible);
    }

    private <T> T updateIfDirty(T value, T oldValue) {
        if (value != oldValue) {
            this.isDirty = true;
        }
        return value;
    }

    public void SYNIfDirty(LivingEntity livingEntity) {
        if (!this.isDirty) return;
        Network.CLIENT_INSTANCE.send(
                PacketDistributor.NEAR.with(() ->
                    new PacketDistributor.TargetPoint(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 64, livingEntity.level().dimension())
                ),
                MyReadAllConditionData.getInstance(livingEntity.getUUID(), livingEntity.getId(), this.lightSerializeNBT(), SYN)
        );
        this.isDirty = false;
    }

    public CompoundTag lightSerializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("armBreak", this.armBreak);
        tag.putBoolean("leftArmVisible", this.leftArmVisible);
        tag.putBoolean("rightArmVisible", this.rightArmVisible);
        tag.putBoolean("leftLegVisible", this.leftLegVisible);
        tag.putBoolean("rightLegVisible", this.rightLegVisible);
        return tag;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.put("body", this.body.serializeNBT());
        tag.putFloat("playerVitality", this.vitality);
        tag.putLong("livingTick", this.livingTick);
        tag.putFloat("almostDead", this.almostDead);
        tag.putInt("nearBedTick", this.nearBedTick);
        tag.putFloat("outerHealing", this.outerHealing);
        tag.putFloat("outerHealingDelta", this.outerHealingDelta);
        tag.put("oxygenMask", this.oxygenMask.serializeNBT());
        tag.put("autoPulse", this.autoPulse.serializeNBT());
        tag.putInt("oxygenMaskCoolDown", this.oxygenMaskCoolDown);
        tag.putInt("autoPulseCoolDown", this.autoPulseCoolDown);
        tag.putUUID("lastHealer", this.lastHealer);
        return tag;
    }

    public void lightDeserializeNBT(CompoundTag nbt) {
        if (nbt == null) return;
        this.armBreak = nbt.getInt("armBreak");
        this.leftArmVisible = nbt.getBoolean("leftArmVisible");
        this.rightArmVisible = nbt.getBoolean("rightArmVisible");
        this.leftLegVisible = nbt.getBoolean("leftLegVisible");
        this.rightLegVisible = nbt.getBoolean("rightLegVisible");
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (nbt == null) return;
        this.body.deserializeNBT(nbt.getCompound("body"));
        this.vitality = nbt.getFloat("playerVitality");
        this.livingTick = nbt.getLong("livingTick");
        this.almostDead = nbt.getFloat("almostDead");
        this.nearBedTick = nbt.getInt("nearBedTick");
        this.outerHealing = nbt.getFloat("outerHealing");
        this.outerHealingDelta = nbt.getFloat("outerHealingDelta");
        this.oxygenMask.deserializeNBT(nbt.getCompound("oxygenMask"));
        this.autoPulse.deserializeNBT(nbt.getCompound("autoPulse"));
        this.oxygenMaskCoolDown = nbt.getInt("oxygenMaskCoolDown");
        this.autoPulseCoolDown = nbt.getInt("autoPulseCoolDown");
        if (nbt.get("lastHealer") != null)
            this.lastHealer = nbt.getUUID("lastHealer");
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

    public boolean leftArmVisible() {
        return leftArmVisible;
    }

    public boolean leftLegVisible() {
        return leftLegVisible;
    }

    public boolean rightArmVisible() {
        return rightArmVisible;
    }

    public boolean rightLegVisible() {
        return rightLegVisible;
    }

    public void setLastHealer(UUID lastHealer) {
        this.lastHealer = lastHealer;
    }

    public UUID lastHealer() {
        return lastHealer;
    }
}
