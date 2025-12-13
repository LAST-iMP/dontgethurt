package com.lastimp.dgh.source.core.damageSystem;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;

@EventBusSubscriber(modid = DontGetHurt.MODID)
public class MonsterEventHandler {
    @SubscribeEvent
    public static void onTargetSet(LivingChangeTargetEvent event) {
        LivingEntity newTarget = event.getNewAboutToBeSetTarget();
        if (newTarget instanceof Player player) {
            if (HealthCapability.isDying(player)) {
                event.setNewAboutToBeSetTarget(null); // 清除仇恨
            }
        }
    }
}
