package com.lastimp.dgh.source.client.gui;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.client.ClientAccessor;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@OnlyIn(value = Dist.CLIENT)
@EventBusSubscriber(modid = DontGetHurt.MODID, value = Dist.CLIENT)
public class GuiEventBus {

    @SubscribeEvent
    public static void onPlayerDead(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!player.level().isClientSide()) return;
        GuiOpenWrapper.closeScreen();
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        var player = event.getEntity();
        if (player.level().isClientSide) {
            if (player.getUUID().equals(GuiOpenWrapper.localPlayerUUID())){
                if (HealthCapability.isDying(player) && GuiOpenWrapper.canOpenDyingScreen()) {
                    GuiOpenWrapper.openDyingScreen();
                } else if (!HealthCapability.isDying(player)){
                    GuiOpenWrapper.closeDyingScreen();
                }
            }
        }
    }
}
