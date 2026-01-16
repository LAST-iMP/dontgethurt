package com.lastimp.dgh.source.client.gui;

import com.google.common.collect.ImmutableSet;
import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.mixin.client.LocalPlayerAccessor;
import com.lastimp.dgh.mixin.client.MinecraftAccessor;
import com.lastimp.dgh.source.client.ClientAccessor;
import com.lastimp.dgh.source.client.hotkey.KeyBinding;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;

@OnlyIn(value = Dist.CLIENT)
@Mod.EventBusSubscriber(modid = DontGetHurt.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class GuiEventBus {
    private static boolean onDying = false;
    private static boolean onPause = false;
    private static Set<ResourceLocation> BLOCKED_OVERLAYS;

    @SubscribeEvent
    public static void screenOpen(ScreenEvent.Opening event) {
        Player player = Minecraft.getInstance().player;
        if (player == null || !HealthCapability.isDying(player)) return;

        var newScreen = event.getNewScreen();
        if (event.getCurrentScreen() == null) {
            onPause = newScreen instanceof PauseScreen;
        }
        if (!onPause && !(newScreen instanceof DeathScreen || newScreen instanceof ChatScreen || newScreen instanceof PauseScreen)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onGuiRender(RenderGuiEvent.Pre event) {
        ClientAccessor.getPlayer().ifPresent(player -> {
            if (HealthCapability.isDying(player)) {
                player.setPose(Pose.SWIMMING);
                ((LocalPlayerAccessor) player).setHandsBusy(true);
                ((MinecraftAccessor) Minecraft.getInstance()).setMissTime(2);
                onDying = true;
            } else if (onDying) {
                ((LocalPlayerAccessor) player).setHandsBusy(false);
                onDying = false;
            }
        });
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Pre event) {
        if (BLOCKED_OVERLAYS == null) {
            BLOCKED_OVERLAYS = ImmutableSet.of(
                    VanillaGuiOverlay.PLAYER_HEALTH.type().id(),
                    VanillaGuiOverlay.HOTBAR.type().id(),
                    VanillaGuiOverlay.CROSSHAIR.type().id(),
                    VanillaGuiOverlay.ARMOR_LEVEL.type().id(),
                    VanillaGuiOverlay.FOOD_LEVEL.type().id(),
                    VanillaGuiOverlay.AIR_LEVEL.type().id(),
                    VanillaGuiOverlay.MOUNT_HEALTH.type().id(),
                    VanillaGuiOverlay.JUMP_BAR.type().id(),
                    VanillaGuiOverlay.EXPERIENCE_BAR.type().id(),
                    VanillaGuiOverlay.ITEM_NAME.type().id()
            );
        }
        ClientAccessor.getPlayer().ifPresent(player -> {
            if (HealthCapability.isDying(player) && BLOCKED_OVERLAYS.contains(event.getOverlay().id()))
                event.setCanceled(true);
        });
    }

    @SubscribeEvent
    public static void onRenderMyOverlay(RenderGuiEvent.Post event) {
        ClientAccessor.getPlayer().ifPresent(player -> {
            Minecraft mc = ClientAccessor.mc();
            if (!mc.options.hideGui && mc.screen == null) {
                if (!HealthCapability.isDying(player)) return;
                var graphics = event.getGuiGraphics();

                graphics.drawCenteredString(mc.font,
                        Component.literal("按下鼠标求救"),
                        graphics.guiWidth() / 2, graphics.guiHeight() / 2 - 50, 0xFFFFFFFF
                );
                graphics.drawCenteredString(mc.font,
                        Component.literal("按住").append(KeyBinding.GIVE_UP.getTranslatedKeyMessage()).append("键5秒放弃治疗"),
                        graphics.guiWidth() / 2, graphics.guiHeight() / 2 + 15 - 50, 0xFFFFFFFF
                );
            }
        });
    }
}
