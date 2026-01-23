package com.lastimp.dgh.source.client.gui;

import com.google.common.collect.ImmutableSet;
import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.mixin.client.LocalPlayerAccessor;
import com.lastimp.dgh.mixin.client.MinecraftAccessor;
import com.lastimp.dgh.source.client.ClientAccessor;
import com.lastimp.dgh.source.client.gui.screen.HealthScreen;
import com.lastimp.dgh.source.client.hotkey.KeyBinding;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.register.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import java.util.Set;

@OnlyIn(value = Dist.CLIENT)
@EventBusSubscriber(modid = DontGetHurt.MODID, value = Dist.CLIENT)
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
        if (!onPause && !screenAvaWhenDying(player, newScreen)) {
            event.setCanceled(true);
        }
    }

    private static boolean screenAvaWhenDying(Player player, Screen screen) {
        if (screen instanceof DeathScreen || screen instanceof ChatScreen || screen instanceof PauseScreen) return true;
        return player.hasEffect(ModEffects.ADRENALINE_EFFECT) && screen instanceof HealthScreen<?>;
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
    public static void onRenderOverlay(RenderGuiLayerEvent.Pre event) {
        if (BLOCKED_OVERLAYS == null) {
            BLOCKED_OVERLAYS = ImmutableSet.of(
                    VanillaGuiLayers.PLAYER_HEALTH,
                    VanillaGuiLayers.HOTBAR,
                    VanillaGuiLayers.CROSSHAIR,
                    VanillaGuiLayers.ARMOR_LEVEL,
                    VanillaGuiLayers.FOOD_LEVEL,
                    VanillaGuiLayers.AIR_LEVEL,
                    VanillaGuiLayers.VEHICLE_HEALTH,
                    VanillaGuiLayers.JUMP_METER,
                    VanillaGuiLayers.EXPERIENCE_BAR,
                    VanillaGuiLayers.SELECTED_ITEM_NAME
            );
        }
        ClientAccessor.getPlayer().ifPresent(player -> {
            if (HealthCapability.isDying(player) && BLOCKED_OVERLAYS.contains(event.getName()))
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
