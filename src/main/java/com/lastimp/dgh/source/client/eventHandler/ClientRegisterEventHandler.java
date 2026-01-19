package com.lastimp.dgh.source.client.eventHandler;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.neoforge.Common;
import com.lastimp.dgh.source.client.gui.screen.BagScreen;
import com.lastimp.dgh.source.client.gui.screen.HealthScreen;
import com.lastimp.dgh.source.client.render.renderer.OperatingBedRenderer;
import com.lastimp.dgh.source.client.hotkey.KeyBinding;
import com.lastimp.dgh.source.client.render.renderer.OperatingBedSpecialRenderer;
import com.lastimp.dgh.source.client.render.renderer.StretcherRenderer;
import com.lastimp.dgh.source.register.ModBlocks;
import com.lastimp.dgh.source.client.render.MyModelLayers;
import com.lastimp.dgh.source.register.ModEntities;
import com.lastimp.dgh.source.register.ModMenus;
import net.minecraft.client.renderer.special.BedSpecialRenderer;
import net.minecraft.client.renderer.special.HangingSignSpecialRenderer;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent;

@EventBusSubscriber(modid = DontGetHurt.MODID, value = Dist.CLIENT)
public class ClientRegisterEventHandler {
    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        event.registerCategory(KeyBinding.KEY_CATEGORY_DGH);
        for (var key : KeyBinding.keys)
            event.register(key);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlocks.OPERATING_BED_ENTITY.get(), OperatingBedRenderer::new);
        event.registerEntityRenderer(ModEntities.STRETCHER.get(), (context) -> new StretcherRenderer(context, MyModelLayers.STRETCHER));
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        for (var key : MyModelLayers.layers.keySet()) {
            event.registerLayerDefinition(key, MyModelLayers.layers.get(key));
        }
    }

    @SubscribeEvent
    public static void registerScreens(final RegisterMenuScreensEvent event) {
        event.register(ModMenus.HEALTH_MENU.get(), HealthScreen::new);
        event.register(ModMenus.HEALTH_CARE_BAG_MENU.get(), BagScreen::new);
        event.register(ModMenus.SURGERY_TOOL_BAG_MENU.get(), BagScreen::new);
        event.register(ModMenus.LIMB_REF_BAG_MENU.get(), BagScreen::new);
    }

    @SubscribeEvent
    public static void registerSpecialRenderer(RegisterSpecialModelRendererEvent event) {
        event.register(Common.getId(DontGetHurt.MODID, "operating_bed"), OperatingBedSpecialRenderer.Unbaked.MAP_CODEC);
    }
}
