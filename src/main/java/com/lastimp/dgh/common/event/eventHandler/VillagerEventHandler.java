package com.lastimp.dgh.common.event.eventHandler;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.lastimp.dgh.common.PlatformService;
import com.lastimp.dgh.common.entity.villager.DoctorVillagerBehavior;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.schedule.Activity;

public class VillagerEventHandler {
    public static void addBrain(Villager villager) {
        if (villager.isClientSide()) return;
        if (!PlatformService.CONFIG.PLAYER_DOCTOR_HEALING()) return;

        villager.getBrain().addActivityWithConditions(
                Activity.WORK,
                ImmutableList.of(Pair.of(2, new DoctorVillagerBehavior())),
                ImmutableSet.of(Pair.of(MemoryModuleType.JOB_SITE, MemoryStatus.VALUE_PRESENT))
        );

        villager.getBrain().addActivityWithConditions(
                Activity.IDLE,
                ImmutableList.of(Pair.of(2, new DoctorVillagerBehavior())),
                ImmutableSet.of(Pair.of(MemoryModuleType.JOB_SITE, MemoryStatus.VALUE_PRESENT))
        );

        villager.getBrain().addActivityWithConditions(
                Activity.MEET,
                ImmutableList.of(Pair.of(2, new DoctorVillagerBehavior())),
                ImmutableSet.of(Pair.of(MemoryModuleType.JOB_SITE, MemoryStatus.VALUE_PRESENT))
        );
    }
}
