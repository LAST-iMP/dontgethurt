
package com.lastimp.dgh.common.capability;

import com.lastimp.dgh.common.PlatformService;
import com.lastimp.dgh.common.capability.bodyPart.base.ArmorData;
import com.lastimp.dgh.common.capability.bodyPart.bodies.Torso;
import com.lastimp.dgh.common.capability.bodyPart.WholeBody;
import com.lastimp.dgh.common.capability.bodyPart.base.AbstractBody;
import com.lastimp.dgh.common.capability.bodyPart.base.AbstractExtremities;
import com.lastimp.dgh.common.capability.bodyPart.ConditionAccessor;
import com.lastimp.dgh.common.capability.healthCore.dyingSystem.HealthPhase;
import com.lastimp.dgh.common.config.impl.HealthLivingEntityList;
import com.lastimp.dgh.common.item.bases.AbstractHealingEquipment;
import com.lastimp.dgh.common.tags.ModTags;
import com.lastimp.dgh.common.utils.BookHelper;
import com.lastimp.dgh.common.utils.Serializable;
import com.lastimp.dgh.common.network.message.MyReadAllConditionData;
import com.lastimp.dgh.common.utils.Utils;
import com.lastimp.dgh.common.container.DynamicItemHandler;
import com.lastimp.dgh.common.enums.BodyComponents;
import com.lastimp.dgh.common.entry.register.ModEffects;
import com.lastimp.dgh.common.entry.register.ModItems;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.lastimp.dgh.common.capability.bodyPart.base.BodyCondition.*;
import static com.lastimp.dgh.common.enums.BodyComponents.*;
import static com.lastimp.dgh.common.enums.OperationType.SYN;
import static com.lastimp.dgh.common.utils.Utils.EPS;

public class HealthCapability implements Serializable {
    public static final String HEALTH_RECORD = "health_record";
    //持久属性
    private final WholeBody body = new WholeBody();
    @Getter
    private final DynamicItemHandler oxygenMask = new DynamicItemHandler();
    @Getter
    private final DynamicItemHandler autoPulse = new DynamicItemHandler();
    @Getter
    private float vitality = 1.0f;
    @Getter
    private int slowDown = 0;
    @Getter
    private long livingTick = 0;
    @Getter
    private float minVitBeforeCured = 1.0f;
    @Setter
    private int nearBedTick = 0;
    @Setter
    @Getter
    private float outerHealing = 0;
    private float outerHealingDelta = 0;
    @Setter
    @Getter
    private UUID lastHealer = UUID.randomUUID();
    private final ArmorData armorData = new ArmorData();
    //同步属性
    @Getter
    private int armBreak = 0;
    @Getter
    private int availableEye = 2;
    @Getter
    private int maxAirSupply = 300;
    @Getter
    private boolean leftArmVisible = true, rightArmVisible = true, leftLegVisible = true, rightLegVisible = true;
    @Getter
    private boolean abnormal = true;
    @Getter
    private HealthPhase healthPhase = HealthPhase.NORMAL;
    //临时属性
    private boolean isDirty = true;
    @Getter
    private boolean haveKidney = true;
    @Getter
    private boolean isInfected = false;
    @Getter
    private int oxygenMaskCoolDown = 0;
    @Getter
    private int autoPulseCoolDown = 0;
    public LivingEntity currentHealer = null;
    @Setter
    @Getter
    private DamageSource lastEntityDamage;
    @Getter
    private final List<InjuryRecord> directInjury = new LinkedList<>();
    @Getter
    private final List<InjuryRecord> lastDeathDirectInjury = new LinkedList<>();


    public HealthCapability() {
        oxygenMask.addAllowed(ModTags.OXYGEN_SUPPLIERS);
        autoPulse.addAllowed(ModTags.AUTOPULSE);
    }

    //*********************全局方法*********************//
    public static boolean has(LivingEntity entity) {
        return PlatformService.CAPABILITY_HELPER.hasHealth(entity);
    }

    private static Optional<HealthCapability> get(LivingEntity entity) {
        return PlatformService.CAPABILITY_HELPER.getHealth(entity);
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

    public static boolean isDown(LivingEntity entity) {
        return HealthCapability.getAndApply(entity, h -> h.healthPhase != HealthPhase.NORMAL, entity.hasEffect(ModEffects.ANALGESIA_POISON_EFFECT.get()));
    }

    public static boolean canApplyHealing(LivingEntity entity) {
        return !isDown(entity) || entity.hasEffect(ModEffects.ADRENALINE_EFFECT.get()) || PlatformService.CONFIG.HARDCORE_CAN_SELFHEAL_WHEN_DOWN();
    }

    public static boolean isFootLostDown(LivingEntity entity) {
        return HealthCapability.getAndApply(entity, h -> !h.rightLegVisible && !h.leftLegVisible, false);
    }

    public boolean isDying() {
        return this.healthPhase == HealthPhase.DYING;
    }

    public static void recordEntityDamage(LivingEntity livingEntity, DamageSource source) {
        if (source == null || source.getEntity() == null) return;
        HealthCapability.getAndApply(livingEntity, h -> h.setLastEntityDamage(source));
    }

    //*********************查询方法*********************//
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

    public boolean intensePain() {
        return  this.getComponent(LEFT_ARM).abnormal(INTENSE_PAIN) ||
                this.getComponent(RIGHT_ARM).abnormal(INTENSE_PAIN) ||
                this.getComponent(LEFT_LEG).abnormal(INTENSE_PAIN) ||
                this.getComponent(RIGHT_LEG).abnormal(INTENSE_PAIN) ||
                this.getComponent(HEAD).abnormal(INTENSE_PAIN) ||
                this.getComponent(TORSO).abnormal(INTENSE_PAIN);
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

    public boolean isFrozen() {
        return this.autoPulse.getStackInSlot(0).is(ModItems.STASIS_BAG.get());
    }

    //*********************状态更新*********************//
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
        //持久属性
        this.minVitBeforeCured = Math.min(this.minVitBeforeCured, this.vitality);
        this.vitality = 1.0f - this.body.updateVitalityLost(this, entity);
        this.vitality = (this.vitality > 0.999f) ? 1.0f : this.vitality;
        this.slowDown = this.body.slowDownLevel(this);
        if (entity instanceof Player player) {
            if (player.getMainHandItem().is(ModItems.WALKING_STICK.get()))
                this.slowDown = Math.max(0, this.slowDown - 3);
            if (player.getOffhandItem().is(ModItems.WALKING_STICK.get()))
                this.slowDown = Math.max(0, this.slowDown - 3);
        }
        this.livingTick = Math.min(Integer.MAX_VALUE, this.livingTick + 1);
        this.nearBedTick = Math.max(0 ,this.nearBedTick);
        this.outerHealing = Math.max(0, this.outerHealing - this.outerHealingDelta);
        this.outerHealingDelta = this.outerHealing <= 0 ? 0 : this.outerHealingDelta + 1.0f / PlatformService.CONFIG.HEALTH_SHIELD_REDU() / 20;
        this.armorData.update(entity);
        //同步属性
        var newArmBreak = (AbstractExtremities.available(this, LEFT_ARM) ? 0 : 1) + (AbstractExtremities.available(this, RIGHT_ARM) ? 0 : 1);
        this.armBreak = updateIfDirty(newArmBreak, this.armBreak);
        this.leftArmVisible = updateIfDirty(AbstractExtremities.visible(this, LEFT_ARM), this.leftArmVisible);
        this.rightArmVisible = updateIfDirty(AbstractExtremities.visible(this, RIGHT_ARM), this.rightArmVisible);
        this.leftLegVisible = updateIfDirty(AbstractExtremities.visible(this, LEFT_LEG), this.leftLegVisible);
        this.rightLegVisible = updateIfDirty(AbstractExtremities.visible(this, RIGHT_LEG), this.rightLegVisible);
        this.availableEye = updateIfDirty(this.getComponent(HEAD).countOrganMatch(ModTags.EYE), this.availableEye);
        this.maxAirSupply = updateIfDirty(((Torso)this.getComponent(TORSO)).additionAir() + 150, this.maxAirSupply);
        this.abnormal = updateIfDirty(this.body.abnormal(), this.abnormal);
        this.healthPhase = updateIfDirty(this.updateHealthPhase(entity), this.healthPhase);
        //临时属性
        this.handleAdrenaline(entity);
        this.isInfected = this.body.isInfected();
        this.haveKidney = this.getComponent(TORSO).countOrganMatch(ModTags.KIDNEY) > 0;
        if (!this.abnormal) {
            this.directInjury.clear();
            this.currentHealer = null;
            this.lastEntityDamage = null;
        }
    }

    public <T> T updateIfDirty(T value, T oldValue) {
        if (value != oldValue) {
            this.isDirty = true;
        }
        return value;
    }

    public void SYNIfDirty(LivingEntity livingEntity) {
        if (!this.isDirty) return;
        PlatformService.NETWORK.sendToPlayersNear(
                (ServerLevel) livingEntity.level(), null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 64,
                MyReadAllConditionData.getInstance(livingEntity.getUUID(), livingEntity.getId(), this.lightSerializeNBT(), SYN)
        );
        this.isDirty = false;
    }

    private HealthPhase updateHealthPhase(LivingEntity livingEntity) {
        if (livingEntity instanceof ServerPlayer player && Utils.checkPlayerInvincible(player)) return HealthPhase.NORMAL;

        HealthPhase nextPhase;
        float maxHealth = this.getHealthWithOuterHealing(livingEntity);
        if (maxHealth <= -livingEntity.getMaxHealth() + EPS) {
            nextPhase = HealthPhase.DEAD;
        } else if (maxHealth <= 0) {
            nextPhase = HealthPhase.DYING;
        } else if (this.healthPhase == HealthPhase.DYING && this.vitality < 0.3) {
            nextPhase = HealthPhase.DYING;
        } else if (livingEntity.hasEffect(ModEffects.ANALGESIA_POISON_EFFECT.get()) || this.getComponent(HEAD).getConditionValue(COMA) > 0.5f || this.isFrozen()) {
            nextPhase = HealthPhase.COMMA;
        } else {
            nextPhase = HealthPhase.NORMAL;
        }

        if (nextPhase == this.healthPhase || !HealthLivingEntityList.callWhenDying(livingEntity.getType())) {
            return nextPhase;
        }

        var name = livingEntity instanceof Player ? livingEntity.getScoreboardName() : livingEntity.getName().getString();
        if (nextPhase == HealthPhase.DYING) {
            Utils.broadcastMessageToTeam(livingEntity, livingEntity.getCombatTracker().getDeathMessage());
            Utils.broadcastMessageToTeam(livingEntity, Component.literal(name + "重伤濒死！"));
        } else if (this.healthPhase == HealthPhase.NORMAL && nextPhase == HealthPhase.COMMA) {
            Utils.broadcastMessageToTeam(livingEntity, Component.literal(name + "晕倒了！"));
        }
        return nextPhase;
    }

    public float getHealthWithOuterHealing(LivingEntity entity) {
        float maxHealth = entity.getMaxHealth() * this.getVitality() + this.getOuterHealing();
        return Math.min(maxHealth, entity.getMaxHealth());
    }

    public void handPulse() {
        if (this.getAutoPulseCoolDown() > 0) return;
        if (this.isFrozen()) return;

        this.autoPulseCoolDown = 20;
        var blood = this.getComponent(BodyComponents.BLOOD);
        Torso torso = (Torso) this.getComponent(BodyComponents.TORSO);
        blood.healing(OXYGEN, -ConditionAccessor.get(OXYGEN).healingSpeed());
        blood.healing(BLOOD_PRESSURE, Utils.randomBetween(0.01f, 0.1f));
        torso.addHeartRate(-Utils.randomBetween(0.01f, 0.2f));
    }

    private void handleAdrenaline(LivingEntity entity) {
        if (this.vitality < 0.3 && this.minVitBeforeCured > 0.3) {
            entity.addEffect(new MobEffectInstance(ModEffects.ADRENALINE_EFFECT.get(), 20 * 20, 1));
        } else if (this.vitality < 0.8 && this.minVitBeforeCured > 0.8) {
            entity.addEffect(new MobEffectInstance(ModEffects.ADRENALINE_EFFECT.get(), 40 * 20, 0));
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
        var bookTag = BookHelper.getBookTag(title, author, recordList);
        stack.setTag(bookTag);
        return true;
    }

    public void healingAll(boolean healPain) {
        this.body.healingAll(healPain);
        this.resetAlmostDead();
        this.healthPhase = this.updateIfDirty(HealthPhase.NORMAL, this.healthPhase);
    }

    public float hurtAfterArmor(BodyComponents component, ResourceLocation resistType, float amount) {
        return this.armorData.hurt(component, resistType, amount);
    }

    public float armorResist(BodyComponents component, ResourceLocation resistType) {
        return this.armorData.getResist(component, resistType);
    }

    public void refreshArmor() {
        this.armorData.setDirty();
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

    public void addToLastDeathDirectInjury(Collection<InjuryRecord> lastDeathDirectInjury) {
        this.lastDeathDirectInjury.addAll(lastDeathDirectInjury);
    }

    //*********************序列化*********************//
    public CompoundTag lightSerializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("armBreak", this.armBreak);
        tag.putBoolean("leftArmVisible", this.leftArmVisible);
        tag.putBoolean("rightArmVisible", this.rightArmVisible);
        tag.putBoolean("leftLegVisible", this.leftLegVisible);
        tag.putBoolean("rightLegVisible", this.rightLegVisible);
        tag.putInt("availableEye", this.availableEye);
        tag.putInt("maxAirSupply", this.maxAirSupply);
        tag.putBoolean("abnormal", this.abnormal);
        tag.putInt("healthPhase", this.healthPhase.ordinal());
        tag.put("body", this.body.lightSerializeNBT());
        return tag;
    }

    public CompoundTag deathSerializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.put("body", this.body.deathSerializeNBT());
        serializeRecord("directInjury", this.directInjury, tag);
        return tag;
    }

    @Override
    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        tag.put("body", this.body.serialize());
        tag.putFloat("playerVitality", this.vitality);
        tag.putLong("livingTick", this.livingTick);
        tag.putFloat("minVitBeforeCured", this.minVitBeforeCured);
        tag.putInt("nearBedTick", this.nearBedTick);
        tag.putFloat("outerHealing", this.outerHealing);
        tag.putFloat("outerHealingDelta", this.outerHealingDelta);
        tag.put("oxygenMask", this.oxygenMask.serialize());
        tag.put("autoPulse", this.autoPulse.serialize());
        tag.putInt("oxygenMaskCoolDown", this.oxygenMaskCoolDown);
        tag.putInt("autoPulseCoolDown", this.autoPulseCoolDown);
        tag.putUUID("lastHealer", this.lastHealer);
        tag.put("armor", this.armorData.serialize());
        tag.putInt("healthPhase", this.healthPhase.ordinal());

        serializeRecord("directInjury", this.directInjury, tag);
        serializeRecord("lastDeathDirectInjury", this.lastDeathDirectInjury, tag);
        return tag;
    }

    private static void serializeRecord(String key, List<InjuryRecord> recordList, CompoundTag tag) {
        ListTag listTag = new ListTag();
        recordList.forEach((record) -> listTag.add(listTag.size(), record.serialize()));
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
        this.maxAirSupply = nbt.getInt("maxAirSupply");
        this.abnormal = nbt.getBoolean("abnormal");
        this.body.lightDeserializeNBT(nbt.getCompound("body"));
        this.healthPhase = HealthPhase.values()[nbt.getInt("healthPhase")];
    }

    public void respawnDeserializeNBT(CompoundTag nbt) {
        this.body.respawnDeserializeNBT(nbt.getCompound("body"));
        deserializeRecord("directInjury", this.lastDeathDirectInjury, nbt);
        this.isDirty = true;
    }

    @Override
    public void deserialize(CompoundTag nbt) {
        this.body.deserialize(nbt.getCompound("body"));
        this.vitality = nbt.getFloat("playerVitality");
        this.livingTick = nbt.getLong("livingTick");
        this.minVitBeforeCured = nbt.getFloat("minVitBeforeCured");
        this.nearBedTick = nbt.getInt("nearBedTick");
        this.outerHealing = nbt.getFloat("outerHealing");
        this.outerHealingDelta = nbt.getFloat("outerHealingDelta");
        this.oxygenMask.deserialize(nbt.getCompound("oxygenMask"));
        this.autoPulse.deserialize(nbt.getCompound("autoPulse"));
        this.oxygenMaskCoolDown = nbt.getInt("oxygenMaskCoolDown");
        this.autoPulseCoolDown = nbt.getInt("autoPulseCoolDown");
        if (nbt.get("lastHealer") != null)
            this.lastHealer = nbt.getUUID("lastHealer");
        this.armorData.deserialize(nbt.getCompound("armor"));
        this.healthPhase = HealthPhase.values()[nbt.getInt("healthPhase")];

        deserializeRecord("directInjury", this.directInjury, nbt);
        deserializeRecord("lastDeathDirectInjury", this.lastDeathDirectInjury, nbt);
        this.isDirty = true;
    }

    private static void deserializeRecord(String key, List<InjuryRecord> recordList, CompoundTag nbt) {
        ListTag listTag = nbt.getList(key, ListTag.TAG_COMPOUND);
        recordList.clear();
        listTag.forEach((tag -> recordList.add(InjuryRecord.phrase((CompoundTag) tag))));
    }

    public void resetAlmostDead() {
        this.minVitBeforeCured = 1.0f;
    }

    public void addDirectInjury(Component source, Component body, Component condition, float value) {
        this.directInjury.add(new InjuryRecord(source.getString(), body.getString(), condition.getString(), value));
    }

    public void clearDirectInjury() {
        this.directInjury.clear();
    }

    public void clearLastDeathDirectInjury() {
        this.lastDeathDirectInjury.clear();
    }
}
