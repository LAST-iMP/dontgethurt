package com.lastimp.dgh.forge.client;

import com.lastimp.dgh.common.client.eventHandler.ClientInputEventHandler;
import com.lastimp.dgh.common.client.eventHandler.ClientTickEventHandler;
import com.lastimp.dgh.common.client.eventHandler.GuiEventHandler;
import com.lastimp.dgh.common.client.renderer.ModelRenderEventBus;
import com.lastimp.dgh.common.utils.Utils;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Utils.MODID, value = Dist.CLIENT)
public class ClientEventBus {
    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {

    }

    @SubscribeEvent
    public static void onRenderPlayer(RenderLivingEvent.Pre<LivingEntity, EntityModel<LivingEntity>> event) {
        ModelRenderEventBus.onRenderPlayer(event.getEntity(), event.getRenderer());
    }

    @SubscribeEvent
    public static void onRenderPlayerPost(RenderLivingEvent.Post<LivingEntity, EntityModel<LivingEntity>> event) {
        ModelRenderEventBus.onRenderPlayerPost(event.getEntity(), event.getRenderer());
    }

    @SubscribeEvent
    public static void screenOpen(ScreenEvent.Opening event) {
        if (!GuiEventHandler.screenOpen(event.getNewScreen(), event.getCurrentScreen()))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onGuiRenderPre(RenderGuiEvent.Pre event) {
        GuiEventHandler.onGuiRender();
    }

    @SubscribeEvent
    public static void onGuiRenderPost(RenderGuiEvent.Post event) {
        GuiEventHandler.onRenderMyOverlay(event.getGuiGraphics());
    }

    @SubscribeEvent
    public static void onRenderOverlayPre(RenderGuiOverlayEvent.Pre event) {
        if (!GuiEventHandler.onRenderOverlay(event.getOverlay().id()))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onScannerPressPre(ScreenEvent.MouseButtonPressed.Pre event) {
        if (!ClientInputEventHandler.onScannerHealing(event.getButton(), event.getScreen()))
            event.setCanceled(true);
        if (!ClientInputEventHandler.onUseMenuItem(event.getButton(), event.getScreen()))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        ClientInputEventHandler.openHealthMenu();
    }

    @SubscribeEvent
    public static void onMouseInput(InputEvent.MouseButton.Pre event) {
        if (!ClientInputEventHandler.callForHelp(event.getAction())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        ClientInputEventHandler.playerTick();
    }

    @SubscribeEvent
    public static void onMovementInput(MovementInputUpdateEvent event) {
        ClientInputEventHandler.onMovementInput(event.getEntity(), event.getInput());
    }

    @SubscribeEvent
    public static void onInteractWithLiving(PlayerInteractEvent.EntityInteractSpecific event) {
        ClientInputEventHandler.onInteractWithLiving(event.getEntity(), event.getTarget());
    }

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        ClientTickEventHandler.playerTick(event.player);
    }
}
