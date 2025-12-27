package com.lastimp.dgh.source.client.gui;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(value = Dist.CLIENT)
@Mod.EventBusSubscriber(modid = DontGetHurt.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class GuiEventBus {

    @SubscribeEvent
    public static void onPlayerDead(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!player.level().isClientSide()) return;
        GuiOpenWrapper.closeScreen();
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        var player = event.player;
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
