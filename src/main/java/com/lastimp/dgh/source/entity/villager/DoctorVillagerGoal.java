package com.lastimp.dgh.source.entity.villager;

import com.lastimp.dgh.source.core.healingSystem.AiHealer;
import com.lastimp.dgh.source.block.OperatingBedBlock;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.register.ModVillagers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;

import java.util.EnumSet;

public class DoctorVillagerGoal extends Goal {
    private final Villager villager;
    private LivingEntity target = null;
    private int level = 0;
    private int coolDown = 0;

    public DoctorVillagerGoal(Villager villager) {
        this.villager = villager;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK, Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        var sitePos = this.villager.getBrain().getMemory(MemoryModuleType.JOB_SITE).orElse(null);
        if (!this.villagerReady(sitePos)) return false;
        //有受伤玩家
        assert sitePos != null;
        var size = OperatingBedBlock.AVA_DISTANCE * 2;
        var entityList = this.villager.level().getEntities(EntityTypeTest.forClass(LivingEntity.class), AABB.ofSize(sitePos.pos().getCenter(), size, size, size), this::livingTest);
        for (var entity : entityList) {
            if (this.validate(entity, sitePos.pos())) {
                this.target = entity;
                return true;
            }
        }
        return false;
    }

    private boolean livingTest(LivingEntity entity) {
        return !(entity instanceof Monster);
    }

    @Override
    public boolean canContinueToUse() {
        var sitePos = this.villager.getBrain().getMemory(MemoryModuleType.JOB_SITE).orElse(null);
        if (!this.villagerReady(sitePos)) return false;
        assert sitePos != null;
        return this.validate(this.target, sitePos.pos());
    }

    private boolean villagerReady(GlobalPos sitePos) {
        //是医生
        if (this.villager.getVillagerData().getProfession() != ModVillagers.DOCTOR_MAKER.get()) return false;
        //工作时间
        if (villager.getBrain().getSchedule().getActivityAt((int) (this.villager.level().dayTime() % 24000)) != Activity.WORK) return false;
        //有工作站
        return sitePos != null && sitePos.dimension() == this.villager.level().dimension();
    }

    private boolean validate(LivingEntity entity, BlockPos sitePos) {
        boolean closeEnoughAndAlive = entity != null && entity.isAlive() &&
                entity.level().dimension() == this.villager.level().dimension() &&
                entity.position().distanceTo(sitePos.getCenter()) < OperatingBedBlock.AVA_DISTANCE;
        if (!closeEnoughAndAlive) return false;
        if (entity.getHealth() < entity.getMaxHealth()) return true;
        return HealthCapability.has(entity) && HealthCapability.getAndApply(entity, h -> h.abnormal() && (h.currentHealer == null || h.currentHealer == this.villager), false);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void start() {
        this.level = this.villager.getVillagerData().getLevel();
        this.villager.getNavigation().moveTo(this.target, 1.0);
        if (HealthCapability.has(this.target))
            HealthCapability.getAndApply(this.target, h -> h.currentHealer = this.villager);
    }

    @Override
    public void tick() {
        if (this.target == null) return;
        this.villager.getLookControl().setLookAt(this.target);
        this.coolDown = Math.max(0, this.coolDown - 1);

        if (this.villager.distanceTo(this.target) > 2) {
            this.villager.getNavigation().moveTo(this.target, 1.0);
            return;
        }
        villager.getNavigation().stop();

        if (this.coolDown > 0) return;

        if (HealthCapability.has(this.target)) {
            this.coolDown += HealthCapability.getAndApply(this.target, (h) -> {
                this.target.level().broadcastEntityEvent(this.target, (byte) 14);
                return AiHealer.doHealing(this.villager, this.target, h, this.level);
            }, 0);
        } else {
            this.coolDown += 20;
            this.target.heal(this.level);
        }
    }

    @Override
    public void stop() {
        HealthCapability.getAndApply(this.target, healthCapability -> {
            if (healthCapability.currentHealer == this.villager)
                healthCapability.currentHealer = null;
        });
        this.target = null;
    }
}
