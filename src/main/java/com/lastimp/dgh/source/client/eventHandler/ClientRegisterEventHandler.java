package com.lastimp.dgh.source.client.eventHandler;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.client.gui.screen.BagScreen;
import com.lastimp.dgh.source.client.gui.screen.HealthScreen;
import com.lastimp.dgh.source.client.gui.screen.MedicineBagScreen;
import com.lastimp.dgh.source.client.renderer.OperatingBedRenderer;
import com.lastimp.dgh.source.client.hotkey.KeyBinding;
import com.lastimp.dgh.source.client.renderer.StretcherRenderer;
import com.lastimp.dgh.source.client.tooltip.ClientBagToolTip;
import com.lastimp.dgh.source.item.tool.AutoUseBag;
import com.lastimp.dgh.source.register.ModBlocks;
import com.lastimp.dgh.source.client.renderer.MyModelLayers;
import com.lastimp.dgh.source.register.ModEntities;
import com.lastimp.dgh.source.register.ModMenus;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(value = Dist.CLIENT)
@Mod.EventBusSubscriber(modid = DontGetHurt.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientRegisterEventHandler {
    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        for (var key : KeyBinding.keys)
            event.register(key);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlocks.OPERATING_BED_ENTITY.get(), OperatingBedRenderer::new);
        event.registerEntityRenderer(ModEntities.STRETCHER.get(), StretcherRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        for (var key : MyModelLayers.layers.keySet()) {
            event.registerLayerDefinition(key, MyModelLayers.layers.get(key));
        }
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(ModMenus.HEALTH_MENU.get(), HealthScreen::new);
            MenuScreens.register(ModMenus.HEALTH_SMALL_BAG_MENU.get(), BagScreen::new);
            MenuScreens.register(ModMenus.HEALTH_SMALL_MEDICINE_BAG_MENU.get(), MedicineBagScreen::new);
        });
    }

    @SubscribeEvent
    public static void registerToolTips(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(AutoUseBag.Tooltip.class, ClientBagToolTip::new);
    }
}
