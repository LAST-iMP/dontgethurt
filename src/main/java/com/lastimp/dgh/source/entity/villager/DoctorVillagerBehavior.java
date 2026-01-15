package com.lastimp.dgh.source.entity.villager;

import com.google.common.collect.ImmutableMap;
import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.block.OperatingBedBlock;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.core.healingSystem.AiHealer;
import com.lastimp.dgh.source.register.ModVillagers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

public class DoctorVillagerBehavior extends Behavior<Villager> {
    private LivingEntity target = null;
    private int level = 0;
    private int coolDown = 0;

    public DoctorVillagerBehavior() {
        super(ImmutableMap.of(MemoryModuleType.JOB_SITE, MemoryStatus.VALUE_PRESENT));
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, Villager villager) {
        var sitePos = villager.getBrain().getMemory(MemoryModuleType.JOB_SITE).orElse(null);
        if (!this.villagerReady(villager, sitePos)) return false;
        //有受伤玩家
        assert sitePos != null;
        var size = OperatingBedBlock.AVA_DISTANCE * 2;
        var entityList = villager.level().getEntities(EntityTypeTest.forClass(LivingEntity.class), AABB.ofSize(sitePos.pos().getCenter(), size, size, size), this::livingTest);
        for (var entity : entityList) {
            if (this.validate(villager, entity, sitePos.pos())) {
                this.target = entity;
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean canStillUse(@NotNull ServerLevel level, Villager villager, long gameTime) {
        var sitePos = villager.getBrain().getMemory(MemoryModuleType.JOB_SITE).orElse(null);
        if (!this.villagerReady(villager, sitePos)) return false;
        assert sitePos != null;
        return this.validate(villager, this.target, sitePos.pos());
    }

    @Override
    protected void start(@NotNull ServerLevel level, Villager villager, long gameTime) {
        this.level = villager.getVillagerData().getLevel();
        villager.getNavigation().moveTo(this.target, 1.0);
        if (HealthCapability.has(this.target))
            HealthCapability.getAndApply(this.target, h -> h.currentHealer = villager);
    }

    @Override
    protected void stop(@NotNull ServerLevel level, @NotNull Villager villager, long gameTime) {
        HealthCapability.getAndApply(this.target, healthCapability -> {
            if (healthCapability.currentHealer == villager)
                healthCapability.currentHealer = null;
        });
        this.target = null;
    }

    @Override
    protected void tick(@NotNull ServerLevel level, @NotNull Villager villager, long gameTime) {
        if (this.target == null) return;
        villager.getLookControl().setLookAt(this.target);
        this.coolDown = Math.max(0, this.coolDown - 1);

        if (villager.distanceTo(this.target) > 2) {
            villager.getNavigation().moveTo(this.target, 0.75);
            return;
        }
        villager.getNavigation().stop();

        if (this.coolDown > 0) return;

        if (HealthCapability.has(this.target)) {
            this.coolDown += HealthCapability.getAndApply(this.target, (h) -> AiHealer.doHealing(villager, this.target, h, this.level), 0);
        } else {
            this.coolDown += 20;
            this.target.heal(this.level);
        }
        if (this.coolDown > 0) this.target.level().broadcastEntityEvent(this.target, (byte) 14);
    }

    private boolean livingTest(LivingEntity entity) {
        return !(entity instanceof Monster);
    }

    private boolean villagerReady(Villager villager, GlobalPos sitePos) {
        //是医生
        if (villager.getVillagerData().getProfession() != ModVillagers.DOCTOR_MAKER.get()) return false;
        //工作时间
        if (villager.getBrain().getSchedule().getActivityAt((int) (villager.level().dayTime() % 24000)) != Activity.WORK) return false;
        //有工作站
        return sitePos != null && sitePos.dimension() == villager.level().dimension();
    }

    private boolean validate(Villager villager, LivingEntity entity, BlockPos sitePos) {
        boolean closeEnoughAndAlive = entity != null && entity.isAlive() &&
                entity.level().dimension() == villager.level().dimension() &&
                entity.position().distanceTo(sitePos.getCenter()) < OperatingBedBlock.AVA_DISTANCE;
        if (!closeEnoughAndAlive) return false;
        if (entity.getHealth() < entity.getMaxHealth()) return true;
        return HealthCapability.has(entity) && HealthCapability.getAndApply(entity, h -> h.abnormal() && (h.currentHealer == null || h.currentHealer == villager), false);
    }
}
