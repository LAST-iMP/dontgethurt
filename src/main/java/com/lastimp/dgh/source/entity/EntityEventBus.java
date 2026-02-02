package com.lastimp.dgh.source.entity;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@EventBusSubscriber(modid = DontGetHurt.MODID)
public class EntityEventBus {
    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) return;
        if (!(event.getEntity() instanceof LivingEntity livingEntity)) return;

        var key = "dgh_version";
        var value = "version_1.3.0";
        var data = livingEntity.getPersistentData();

        if (livingEntity instanceof Player player) {
            var persistedTag = data.getCompound(Player.PERSISTED_NBT_TAG);
            if (!persistedTag.getString(key).equals(value)) {
                HealthCapability.getAndApply(player, h -> h.addOriginOrganFully(player));
                persistedTag.putString(key, value);
                data.put(Player.PERSISTED_NBT_TAG, persistedTag);
            }
        } else {
            if (!data.getString(key).equals(value)) {
                data.putString(key, value);
                HealthCapability.getAndApply(livingEntity, h -> h.addOriginOrganFully(livingEntity));
                data.putString(key, value);
            }
        }
    }
}
