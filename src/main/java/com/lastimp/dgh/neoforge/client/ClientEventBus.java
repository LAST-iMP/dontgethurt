package com.lastimp.dgh.neoforge.client;

import com.lastimp.dgh.common.utils.Utils;
import com.lastimp.dgh.common.client.renderer.ClientRenderHandler;
import com.lastimp.dgh.common.client.eventHandler.ClientInputEventHandler;
import com.lastimp.dgh.common.client.eventHandler.ClientTickEventHandler;
import com.lastimp.dgh.common.client.eventHandler.GuiEventHandler;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = Utils.MODID, value = Dist.CLIENT)
public class ClientEventBus {
    @SubscribeEvent
    public static void onRenderPlayer(RenderLivingEvent.Pre<LivingEntity, EntityModel<LivingEntity>> event) {
        ClientRenderHandler.onRenderPlayer(event.getEntity(), event.getRenderer());
    }

    @SubscribeEvent
    public static void onRenderPlayerPost(RenderLivingEvent.Post<LivingEntity, EntityModel<LivingEntity>> event) {
        ClientRenderHandler.onRenderPlayerPost(event.getEntity(), event.getRenderer());
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
    public static void onRenderOverlayPre(RenderGuiLayerEvent.Pre event) {
        if (!GuiEventHandler.onRenderOverlay(event.getName()))
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
    public static void clientTick(ClientTickEvent.Post event) {
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
    public static void playerTick(PlayerTickEvent.Pre event) {
        ClientTickEventHandler.playerTick(event.getEntity());
    }

    @SubscribeEvent
    public static void playerTick(PlayerTickEvent.Post event) {
        ClientTickEventHandler.playerTick(event.getEntity());
    }
}
