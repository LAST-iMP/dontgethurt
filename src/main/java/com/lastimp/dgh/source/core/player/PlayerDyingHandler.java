package com.lastimp.dgh.source.core.player;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.client.gui.GuiOpenWrapper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Pose;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = DontGetHurt.MODID)
public class PlayerDyingHandler {
    private static boolean showingScreen = false;

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        var player = event.getEntity();

        if (event.getEntity().level().isClientSide) {
            if (player.getUUID().equals(GuiOpenWrapper.MINECRAFT.get().player.getUUID())){
                if (PlayerHealthCapability.isDying(player) && !showingScreen()) {
                    GuiOpenWrapper.openDyingScreen();
                    setShowingScreen(true);
                } else if (!PlayerHealthCapability.isDying(player) && showingScreen()){
                    GuiOpenWrapper.closeDyingScreen();
                    setShowingScreen(false);
                }
            }
        } else {
            if (PlayerHealthCapability.isDying(player)) {
                if (player.isSleeping()) player.stopSleeping();
                if (player.isFallFlying()) player.stopFallFlying();
                player.stopRiding();
                player.stopUsingItem();
                player.setForcedPose(Pose.SWIMMING);
            } else {
                player.setForcedPose(null);
            }
        }
    }

    public static void setShowingScreen(boolean showingScreen) {
        PlayerDyingHandler.showingScreen = showingScreen;
    }

    public static boolean showingScreen() {
        return PlayerDyingHandler.showingScreen;
    }

    @SubscribeEvent
    public static void onJump(LivingEvent.LivingJumpEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        PlayerHealthCapability health = PlayerHealthCapability.get(player);
        if (true || health.playerVitality() < 0) {
            player.setDeltaMovement(0, 0, 0); // 阻止起跳速度
        }
    }
}
