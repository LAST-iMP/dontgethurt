package com.lastimp.dgh.common.client.eventHandler;

import com.google.common.collect.ImmutableSet;
import com.lastimp.dgh.common.PlatformService;

import com.lastimp.dgh.mixin.client.LocalPlayerAccessor;
import com.lastimp.dgh.mixin.client.MinecraftAccessor;
import com.lastimp.dgh.common.client.ClientAccessor;
import com.lastimp.dgh.common.client.gui.screen.HealthScreen;
import com.lastimp.dgh.common.client.hotkey.KeyBinding;
import com.lastimp.dgh.common.capability.HealthCapability;
import com.lastimp.dgh.common.entry.register.ModEffects;
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

import java.util.Set;

public class GuiEventHandler {
    public static final ResourceLocation PLAYER_HEALTH = ResourceLocation.withDefaultNamespace("player_health");
    public static final ResourceLocation HOTBAR = ResourceLocation.withDefaultNamespace("hotbar");
    public static final ResourceLocation CROSSHAIR = ResourceLocation.withDefaultNamespace("crosshair");
    public static final ResourceLocation ARMOR_LEVEL = ResourceLocation.withDefaultNamespace("armor_level");
    public static final ResourceLocation FOOD_LEVEL = ResourceLocation.withDefaultNamespace("food_level");
    public static final ResourceLocation AIR_LEVEL = ResourceLocation.withDefaultNamespace("air_level");
    public static final ResourceLocation VEHICLE_HEALTH = ResourceLocation.withDefaultNamespace("vehicle_health");
    public static final ResourceLocation JUMP_METER = ResourceLocation.withDefaultNamespace("jump_meter");
    public static final ResourceLocation EXPERIENCE_BAR = ResourceLocation.withDefaultNamespace("experience_bar");
    public static final ResourceLocation SELECTED_ITEM_NAME = ResourceLocation.withDefaultNamespace("selected_item_name");
    
    
    private static boolean onDying = false;
    private static boolean onPause = false;
    private static Set<ResourceLocation> BLOCKED_OVERLAYS;

    public static boolean screenOpen(Screen newScreen, Screen currentScreen) {
        Player player = Minecraft.getInstance().player;
        if (player == null || !HealthCapability.isDown(player)) return true;

        if (currentScreen == null) {
            onPause = newScreen instanceof PauseScreen;
        }
        return onPause || screenAvaWhenDying(player, newScreen);
    }

    private static boolean screenAvaWhenDying(Player player, Screen screen) {
        if (screen instanceof DeathScreen || screen instanceof ChatScreen || screen instanceof PauseScreen) return true;
        return player.hasEffect(ModEffects.ADRENALINE_EFFECT) && screen instanceof HealthScreen<?>;
    }

    public static void onGuiRender() {
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

    public static boolean onRenderOverlay(ResourceLocation overlay) {
        if (BLOCKED_OVERLAYS == null) {
            BLOCKED_OVERLAYS = ImmutableSet.of(
                    PLAYER_HEALTH,
                    HOTBAR,
                    CROSSHAIR,
                    ARMOR_LEVEL,
                    FOOD_LEVEL,
                    AIR_LEVEL,
                    VEHICLE_HEALTH,
                    JUMP_METER,
                    EXPERIENCE_BAR,
                    SELECTED_ITEM_NAME
            );
        }
        return ClientAccessor.getPlayer().map(
                player -> !HealthCapability.isDying(player) || !BLOCKED_OVERLAYS.contains(overlay)
        ).orElse(true);
    }

    public static void onRenderMyOverlay(GuiGraphics graphics) {
        ClientAccessor.getPlayer().ifPresent(player -> {
            Minecraft mc = ClientAccessor.mc();
            ModOverlay.renderEyeOverlay(player, graphics);
            ModOverlay.renderConditionOverlay(player, graphics);
            if (!mc.options.hideGui) {
                ModOverlay.renderDyingOverlay(player, graphics);
            }
        });
    }
}
