package com.lastimp.dgh.source.client.gui;

import com.google.common.collect.ImmutableSet;
import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.config.Config;
import com.lastimp.dgh.mixin.client.LocalPlayerAccessor;
import com.lastimp.dgh.mixin.client.MinecraftAccessor;
import com.lastimp.dgh.source.client.ClientAccessor;
import com.lastimp.dgh.source.client.gui.screen.HealthScreen;
import com.lastimp.dgh.source.client.hotkey.KeyBinding;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.register.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
        if (player == null || !HealthCapability.isDown(player)) return;

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
        return player.hasEffect(ModEffects.ADRENALINE_EFFECT.get()) && screen instanceof HealthScreen<?>;
    }

    @SubscribeEvent
    public static void onGuiRender(RenderGuiEvent.Pre event) {
        ClientAccessor.getPlayer().ifPresent(player -> {
            if (HealthCapability.isDown(player)) {
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
            var graphics = event.getGuiGraphics();
            renderEyeOverlay(player, graphics);
            if (!mc.options.hideGui) {
                renderDyingOverlay(player, graphics);
            }
        });
    }

    private static void renderEyeOverlay(LocalPlayer player, GuiGraphics graphics) {
        if (player.isDeadOrDying()) return;
        HealthCapability.getAndApply(player, h -> {
            int avaEye = h.availableEye();
            if (avaEye >= 2) return;
            int color = avaEye == 1 ? 0x80000000 : 0xEF000000;
            graphics.fill(0, 0, graphics.guiWidth(), graphics.guiHeight(), color);
        });
    }

    private static void renderDyingOverlay(LocalPlayer player, GuiGraphics graphics) {
        if (!HealthCapability.isDown(player)) return;
        graphics.drawCenteredString(ClientAccessor.mc().font,
                Component.literal("按下鼠标求救"),
                graphics.guiWidth() / 2, graphics.guiHeight() / 2 - 50, 0xFFFFFFFF
        );
        if (Config.enable_self_suicide) {
            graphics.drawCenteredString(ClientAccessor.mc().font,
                    Component.literal("按住").append(KeyBinding.GIVE_UP.getTranslatedKeyMessage()).append("键5秒放弃治疗"),
                    graphics.guiWidth() / 2, graphics.guiHeight() / 2 + 15 - 50, 0xFFFFFFFF
            );
        }
    }
}
