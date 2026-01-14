package com.lastimp.dgh.source.entity.villager;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.config.Config;
import net.minecraft.world.entity.npc.Villager;
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

        villager.goalSelector.addGoal(2, new DoctorVillagerGoal(villager));
    }
}
