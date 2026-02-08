package com.lastimp.dgh.fabric.client;

import com.lastimp.dgh.common.client.eventHandler.ClientInputEventHandler;
import com.lastimp.dgh.common.client.eventHandler.ClientTickEventHandler;
import com.lastimp.dgh.common.client.eventHandler.GuiEventHandler;
import com.lastimp.dgh.common.client.renderer.ModelRenderEventBus;
import com.lastimp.dgh.fabric.client.event.RenderLivingCallback;
import com.lastimp.dgh.fabric.client.event.ScreenCallback;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.client.ClientScreenInputEvent;

public class ClientEventBus {
    public static void onRenderPlayerPre() {
        RenderLivingCallback.Pre.EVENT.register(ModelRenderEventBus::onRenderPlayer);
    }

    public static void onRenderPlayerPost() {
        RenderLivingCallback.Post.EVENT.register(ModelRenderEventBus::onRenderPlayerPost);
    }

    public static void screenOpen() {
        ScreenCallback.Opening.EVENT.register(GuiEventHandler::screenOpen);
    }

    public static void onGuiRenderPre() {
        ClientGuiEvent.RENDER_PRE.register(((screen, guiGraphics, i, i1, v) -> {
            GuiEventHandler.onGuiRender();
            return EventResult.pass();
        }));
    }

    public static void onGuiRenderPost() {
        ClientGuiEvent.RENDER_POST.register(((screen, guiGraphics, i, i1, v) ->
                GuiEventHandler.onRenderMyOverlay(guiGraphics))
        );

    }

    @SubscribeEvent
    public static void onRenderOverlayPre(RenderGuiOverlayEvent.Pre event) {
        if (!GuiEventHandler.onRenderOverlay(event.getOverlay().id()))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onScannerPressPre(ScreenEvent.MouseButtonPressed.Pre event) {
        ClientScreenInputEvent.MOUSE_CLICKED_PRE.register(((minecraft, screen, v, v1, i) -> {
            boolean canceled = false;
            if (!ClientInputEventHandler.onScannerHealing(event.getButton(), event.getScreen()))
                canceled = true;
            if (!ClientInputEventHandler.onUseMenuItem(event.getButton(), event.getScreen()))
                canceled = true;
            return EventResult.interruptDefault()
        }));
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

    public static void init() {
        onRenderPlayerPre();
        onRenderPlayerPost();
        screenOpen();
        onGuiRenderPre();
        onGuiRenderPost();
    }
}
