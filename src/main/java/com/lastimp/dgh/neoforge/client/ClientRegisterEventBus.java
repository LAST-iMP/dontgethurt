package com.lastimp.dgh.neoforge.client;

import com.lastimp.dgh.common.client.gui.screen.BagScreen;
import com.lastimp.dgh.common.client.gui.screen.HealthScreen;
import com.lastimp.dgh.common.client.gui.screen.MedicineBagScreen;
import com.lastimp.dgh.common.client.renderer.OperatingBedRenderer;
import com.lastimp.dgh.common.client.hotkey.KeyBinding;
import com.lastimp.dgh.common.client.renderer.StretcherRenderer;
import com.lastimp.dgh.common.client.tooltip.ClientBagToolTip;
import com.lastimp.dgh.common.menu.HealthMenu;
import com.lastimp.dgh.common.item.tool.AutoUseBag;
import com.lastimp.dgh.common.entry.register.ModBlocks;
import com.lastimp.dgh.common.client.renderer.MyModelLayers;
import com.lastimp.dgh.common.entry.register.ModEntities;
import com.lastimp.dgh.common.entry.register.ModMenus;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import com.lastimp.dgh.common.utils.Utils;

@OnlyIn(value = Dist.CLIENT)
@EventBusSubscriber(modid = Utils.MODID, value = Dist.CLIENT)
public class ClientRegisterEventBus {
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
    public static void registerScreens(final RegisterMenuScreensEvent event) {
        event.register(ModMenus.HEALTH_MENU.get(), HealthScreen<HealthMenu>::new);
        event.register(ModMenus.HEALTH_SMALL_BAG_MENU.get(), BagScreen::new);
        event.register(ModMenus.HEALTH_SMALL_MEDICINE_BAG_MENU.get(), MedicineBagScreen::new);
    }

    @SubscribeEvent
    public static void registerToolTips(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(AutoUseBag.Tooltip.class, ClientBagToolTip::new);
    }
}
