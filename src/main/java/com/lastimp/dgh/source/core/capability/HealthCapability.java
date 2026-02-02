
package com.lastimp.dgh.source.core.capability;

import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.bodyPart.AbstractExtremities;
import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.api.healingItems.AbstractHealingEquipment;
import com.lastimp.dgh.api.tags.ModTags;
import com.lastimp.dgh.config.Config;
import com.lastimp.dgh.neoforge.Common;
import com.lastimp.dgh.network.message.MyReadAllConditionData;
import com.lastimp.dgh.source.core.Utils;
import com.lastimp.dgh.source.core.menu.component.DynamicItemHandler;
import com.lastimp.dgh.source.register.ModCapabilities;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.source.core.bodyPart.*;
import com.lastimp.dgh.source.register.ModEffects;
import com.lastimp.dgh.source.register.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.BLOOD_PRESSURE;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.OXYGEN;
import static com.lastimp.dgh.api.enums.BodyComponents.*;
import static com.lastimp.dgh.api.enums.OperationType.SYN;

public class HealthCapability implements INBTSerializable<CompoundTag> {
    public static final String HEALTH_RECORD = "health_record";

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
    private boolean abnormal = true;
    public LivingEntity currentHealer = null;
    private boolean haveKidney = true;

    private int armBreak = 0;
    private int availableEye = 2;
    private int maxAirSupply = 300;
    private boolean leftArmVisible = true;
    private boolean rightArmVisible = true;
    private boolean leftLegVisible = true;
    private boolean rightLegVisible = true;
    private boolean isDown = false;

    private UUID lastHealer = UUID.randomUUID();

    private boolean isDirty = true;

    private final List<InjuryRecord> directInjury = new LinkedList<>();
    private final List<InjuryRecord> lastDeathDirectInjury = new LinkedList<>();

    public HealthCapability() {
        oxygenMask.addAllowed(ModTags.OXYGEN_SUPPLIERS);
        autoPulse.addAllowed(ModTags.AUTOPULSE);
    }

    public static boolean has(LivingEntity entity) {
        return HealthProvider.has(entity);
    }

    private static LazyOptional<HealthCapability> get(LivingEntity entity) {
        return entity.getCapability(ModCapabilities.HEALTH, null);
    }

    public static <T> T getAndApply(LivingEntity entity, Function<HealthCapability, T> function, T orElse) {
        return HealthCapability.get(entity).map(function::apply).orElse(orElse);
    }

    public static void getAndApply(LivingEntity entity, Consumer<HealthCapability> function) {
        HealthCapability.get(entity).ifPresent(function::accept);
    }

    public static void handPulse(LivingEntity entity) {
        if (has(entity)) {
            HealthCapability.getAndApply(entity, HealthCapability::handPulse);
        }
    }

    public AbstractBody getComponent(BodyComponents component) {
        return this.body.getComponent(component);
    }

    public BodyComponents findFor(ResourceLocation condition) {
        for (var component : VISIBLE_BODIES) {
            if (this.getComponent(component).abnormal(condition))
                return component;
        }
        if (this.getComponent(BLOOD).abnormal(condition))
            return BLOOD;
        return null;
    }

    public BodyComponents findForHidden(ResourceLocation condition) {
        for (var component : VISIBLE_BODIES) {
            if (this.getComponent(component).abnormalWithHidden(condition))
                return component;
        }
        if (this.getComponent(BLOOD).abnormalWithHidden(condition))
            return BLOOD;
        return null;
    }

    public HealthCapability update(LivingEntity entity) {
        this.updateALLEquipments(entity);
        if (!this.isFrozen())
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
        if (entity instanceof Player player) {
            if (player.getMainHandItem().is(ModItems.WALKING_STICK.get()))
                this.slowDown = Math.max(0, this.slowDown - 3);
            if (player.getOffhandItem().is(ModItems.WALKING_STICK.get()))
                this.slowDown = Math.max(0, this.slowDown - 3);
        }
        this.vitality = 1.0f - this.body.updateVitalityLost(this, entity);
        this.vitality = (this.vitality > 0.999f) ? 1.0f : this.vitality;
        this.handleAdrenaline(entity);
        this.almostDead = Math.min(this.almostDead, this.vitality);
        this.nearBedTick--;
        this.outerHealing = Math.max(0, this.outerHealing - this.outerHealingDelta);
        this.outerHealingDelta = this.outerHealing <= 0 ? 0 : this.outerHealingDelta + 1.0f / Config.health_shield_redu / 20;
        this.isInfected = this.body.isInfected();
        this.haveKidney = this.getComponent(TORSO).countOrganMatch(ModTags.KIDNEY) > 0;

        this.leftArmVisible = updateIfDirty(AbstractExtremities.visible(this, LEFT_ARM), this.leftArmVisible);
        this.rightArmVisible = updateIfDirty(AbstractExtremities.visible(this, RIGHT_ARM), this.rightArmVisible);
        this.leftLegVisible = updateIfDirty(AbstractExtremities.visible(this, LEFT_LEG), this.leftLegVisible);
        this.rightLegVisible = updateIfDirty(AbstractExtremities.visible(this, RIGHT_LEG), this.rightLegVisible);
        this.availableEye = updateIfDirty(this.getComponent(HEAD).countOrganMatch(ModTags.EYE), this.availableEye);
        this.maxAirSupply = updateIfDirty(((Torso)this.getComponent(TORSO)).additionAir() + 150, this.maxAirSupply);
        this.abnormal = this.body.abnormal();
        if (!this.abnormal) {
            this.directInjury.clear();
            this.currentHealer = null;
        }
        this.isDown = updateIfDirty(isDown(entity), this.isDown);
    }

    private <T> T updateIfDirty(T value, T oldValue) {
        if (value != oldValue) {
            this.isDirty = true;
        }
        return value;
    }

    public void SYNIfDirty(LivingEntity livingEntity) {
        if (!this.isDirty) return;
        Common.sendToPlayersNear(
                (ServerLevel) livingEntity.level(), null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 64,
                MyReadAllConditionData.getInstance(livingEntity.getUUID(), livingEntity.getId(), this.lightSerializeNBT(), SYN)
        );
        this.isDirty = false;
    }

    public void handPulse() {
        if (this.autoPulseCoolDown() > 0) return;
        if (this.isFrozen()) return;

        this.autoPulseCoolDown = 20;
        var blood = this.getComponent(BodyComponents.BLOOD);
        Torso torso = (Torso) this.getComponent(BodyComponents.TORSO);
        blood.healing(OXYGEN, -BodyCondition.get(OXYGEN).healingSpeed());
        blood.healing(BLOOD_PRESSURE, Utils.randomBetween(0.01f, 0.1f));
        torso.addHeartRate(-Utils.randomBetween(0.01f, 0.2f));
    }

    private void handleAdrenaline(LivingEntity entity) {
        if (this.vitality < 0.8 && this.almostDead > 0.8) {
            entity.addEffect(new MobEffectInstance(ModEffects.ADRENALINE_EFFECT.get(), 40 * 20, 0));
        } else if (this.vitality < 0.3 && this.almostDead > 0.3) {
            entity.addEffect(new MobEffectInstance(ModEffects.ADRENALINE_EFFECT.get(), 20 * 20, 1));
        }
    }

    public void addOriginOrganOnDeath(LivingEntity livingEntity) {
        this.body.addOriginOrgan(livingEntity, false);
    }

    public void addOriginOrganFully(LivingEntity livingEntity) {
        this.body.addOriginOrgan(livingEntity, true);
    }

    public boolean write(ItemStack stack, Component name, Component author) {
        if (!stack.is(Items.WRITTEN_BOOK)) return false;
        if (this.lastDeathDirectInjury.isEmpty() && this.directInjury.isEmpty()) return false;

        var recordList = this.lastDeathDirectInjury.isEmpty() ? this.directInjury : this.lastDeathDirectInjury;
        Component title = Component.literal(name.getString() + (this.lastDeathDirectInjury.isEmpty() ? "的病例" : "的尸检报告"));
        var bookTag = Common.getBookTag(title, author, recordList);
        stack.setTag(bookTag);
        return true;
    }

    public void healingAll(boolean healPain) {
        this.body.healingAll(healPain);
        this.resetAlmostDead();
    }

    public CompoundTag lightSerializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("armBreak", this.armBreak);
        tag.putBoolean("leftArmVisible", this.leftArmVisible);
        tag.putBoolean("rightArmVisible", this.rightArmVisible);
        tag.putBoolean("leftLegVisible", this.leftLegVisible);
        tag.putBoolean("rightLegVisible", this.rightLegVisible);
        tag.putInt("availableEye", this.availableEye);
        tag.putBoolean("isDown", this.isDown);
        tag.putInt("maxAirSupply", this.maxAirSupply);
        return tag;
    }

    public CompoundTag deathSerializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.put("body", this.body.deathSerializeNBT());
        serializeRecord("directInjury", this.directInjury, tag);
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

        serializeRecord("directInjury", this.directInjury, tag);
        serializeRecord("lastDeathDirectInjury", this.lastDeathDirectInjury, tag);
        return tag;
    }

    private static void serializeRecord(String key, List<InjuryRecord> recordList, CompoundTag tag) {
        ListTag listTag = new ListTag();
        recordList.forEach((record) -> listTag.add(listTag.size(), record.serializeNBT()));
        tag.put(key, listTag);
    }

    public void lightDeserializeNBT(CompoundTag nbt) {
        if (nbt == null) return;
        this.armBreak = nbt.getInt("armBreak");
        this.leftArmVisible = nbt.getBoolean("leftArmVisible");
        this.rightArmVisible = nbt.getBoolean("rightArmVisible");
        this.leftLegVisible = nbt.getBoolean("leftLegVisible");
        this.rightLegVisible = nbt.getBoolean("rightLegVisible");
        this.availableEye = nbt.getInt("availableEye");
        this.isDown = nbt.getBoolean("isDown");
        this.maxAirSupply = nbt.getInt("maxAirSupply");
    }

    public void respawnDeserializeNBT(CompoundTag nbt) {
        this.body.respawnDeserializeNBT(nbt.getCompound("body"));
        deserializeRecord("directInjury", this.lastDeathDirectInjury, nbt);
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

        deserializeRecord("directInjury", this.directInjury, nbt);
        deserializeRecord("lastDeathDirectInjury", this.lastDeathDirectInjury, nbt);
    }

    private static void deserializeRecord(String key, List<InjuryRecord> recordList, CompoundTag nbt) {
        ListTag listTag = nbt.getList(key, ListTag.TAG_COMPOUND);
        recordList.clear();
        listTag.forEach((tag -> recordList.add(InjuryRecord.phrase((CompoundTag) tag))));
    }

    public boolean intensePain() {
        return  this.getComponent(LEFT_ARM).abnormal(INTENSE_PAIN) ||
                this.getComponent(RIGHT_ARM).abnormal(INTENSE_PAIN) ||
                this.getComponent(LEFT_LEG).abnormal(INTENSE_PAIN) ||
                this.getComponent(RIGHT_LEG).abnormal(INTENSE_PAIN) ||
                this.getComponent(HEAD).abnormal(INTENSE_PAIN) ||
                this.getComponent(TORSO).abnormal(INTENSE_PAIN);
    }

    public static boolean isFootLostDown(LivingEntity entity) {
        return HealthCapability.getAndApply(entity, h -> !h.rightLegVisible && !h.leftLegVisible, false);
    }

    public static boolean isDown(LivingEntity entity) {
        return HealthCapability.getAndApply(entity, h -> {
            if (entity.level().isClientSide()){
                return h.isDown;
            } else {
                if (entity instanceof ServerPlayer player && Utils.checkPlayerInvincible(player)) return false;
                if (isDying(entity)) return true;
                if (entity.hasEffect(ModEffects.ANALGESIA_POISON_EFFECT.get())) return true;
                if (h.getComponent(HEAD).getConditionValue(COMA) > 0.5f) return true;
                if (h.isFrozen()) return true;
                return false;
            }
        }, false);
    }

    public static boolean isDying(LivingEntity entity) {
        return entity.getHealth() < 0.05 && !entity.isDeadOrDying();
    }

    public float bloodOxygenFactor() {
        return 300.f / this.maxAirSupply;
    }

    public boolean canEat() {
        return this.getComponent(TORSO).countOrganMatch(ModTags.STOMACH) > 0;
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

    public int availableEye() {return availableEye;}

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

    public boolean haveKidney() {
        return haveKidney;
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

    public int maxAirSupply() {
        return this.maxAirSupply;
    }

    public boolean isFrozen() {
        return this.autoPulse.getStackInSlot(0).is(ModItems.STASIS_BAG.get());
    }

    public void addDirectInjury(Component source, Component body, Component condition, float value) {
        this.directInjury.add(new InjuryRecord(source.getString(), body.getString(), condition.getString(), value));
    }

    public void addDirectInjury(Entity source, Component body, Component condition, float value) {
        this.addDirectInjury(source != null ? source.getName() : Component.literal("环境"), body, condition, value);
    }

    public void addDirectInjury(Component body, Component condition, float value, int level) {
        this.directInjury.add(new InjuryRecord("", body.getString(), condition.getString(), value, level));
    }

    public void addDirectInjury(Component body, Component condition, int level) {
        this.directInjury.add(new InjuryRecord("", body.getString(), condition.getString(), -1, level));
    }

    public List<InjuryRecord> directInjury() {
        return this.directInjury;
    }

    public void clearDirectInjury() {
        this.directInjury.clear();
    }

    public void addToLastDeathDirectInjury(Collection<InjuryRecord> lastDeathDirectInjury) {
        this.lastDeathDirectInjury.addAll(lastDeathDirectInjury);
    }

    public List<InjuryRecord> lastDeathDirectInjury() {
        return lastDeathDirectInjury;
    }

    public void clearLastDeathDirectInjury() {
        this.lastDeathDirectInjury.clear();
    }

    public boolean abnormal() {
        return abnormal;
    }
}
