package com.lastimp.dgh.source.client.eventHandler;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.api.enums.KeyPressedType;
import com.lastimp.dgh.source.client.gui.GuiOpenWrapper;
import com.lastimp.dgh.source.client.hotkey.KeyBinding;
import com.lastimp.dgh.network.message.MyKeyPressedData;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = DontGetHurt.MODID,value = Dist.CLIENT)
public class ForgeClientEventHandler {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if(KeyBinding.OPEN_MENU_KEY.consumeClick()){
            PacketDistributor.sendToServer(MyKeyPressedData.getInstance(KeyPressedType.KEY_HEALTH_MENU, 0));
        }
    }

    @SubscribeEvent
    public static void onGuiRender(RenderGuiEvent.Pre event) {
        if (GuiOpenWrapper.MINECRAFT.get().player == null) return;
        if (HealthCapability.isDying(GuiOpenWrapper.MINECRAFT.get().player)) {
            var graphics = event.getGuiGraphics();
            GuiOpenWrapper.MINECRAFT.get().gui.getChat().render(graphics, 0, graphics.guiHeight(), graphics.guiWidth(), true);
            event.setCanceled(true);
        }
    }
}