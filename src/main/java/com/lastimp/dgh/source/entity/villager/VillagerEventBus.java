package com.lastimp.dgh.source.entity.villager;

import com.lastimp.dgh.DontGetHurt;
import net.minecraft.world.entity.npc.Villager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@EventBusSubscriber(modid = DontGetHurt.MODID)
public class VillagerEventBus {
    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof Villager villager)) return;
        if (event.getLevel().isClientSide()) return;

        villager.goalSelector.addGoal(2, new DoctorVillagerGoal(villager));
    }
}
