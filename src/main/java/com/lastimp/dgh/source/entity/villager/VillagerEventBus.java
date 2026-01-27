package com.lastimp.dgh.source.entity.villager;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.config.Config;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DontGetHurt.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VillagerEventBus {
    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof Villager villager)) return;
        if (event.getLevel().isClientSide()) return;
        if (!Config.player_doctor_healing) return;

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
