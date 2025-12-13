
package com.lastimp.dgh.source.client.eventHandler;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.api.enums.KeyPressedType;
import com.lastimp.dgh.network.message.Network;
import com.lastimp.dgh.source.client.gui.GuiOpenWrapper;
import com.lastimp.dgh.source.register.ModCapabilities;
import com.lastimp.dgh.source.client.hotkey.KeyBinding;
import com.lastimp.dgh.network.message.MyKeyPressedData;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DontGetHurt.MODID, value = Dist.CLIENT)
public class ForgeClientEventHandler {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if(KeyBinding.OPEN_MENU_KEY.consumeClick()){
            Network.SERVER_INSTANCE.sendToServer(MyKeyPressedData.getInstance(KeyPressedType.KEY_HEALTH_MENU, 0));
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) {
            LazyOptional<HealthCapability> oldHealth = event.getOriginal().getCapability(ModCapabilities.HEALTH);
            LazyOptional<HealthCapability> newHealth = event.getEntity().getCapability(ModCapabilities.HEALTH);
            if (oldHealth.isPresent() && newHealth.isPresent()) {
                newHealth.ifPresent((newCap) ->
                        oldHealth.ifPresent((oldCap) ->
                                newCap.deserializeNBT(oldCap.serializeNBT())));
            }
        }
    }

    @SubscribeEvent
    public static void onGuiRender(RenderGuiEvent.Pre event) {
        if (GuiOpenWrapper.MINECRAFT.get().player == null) return;
        if (HealthCapability.isDying(GuiOpenWrapper.MINECRAFT.get().player)) {
            var graphics = event.getGuiGraphics();
            GuiOpenWrapper.MINECRAFT.get().gui.getChat().render(graphics, 0, graphics.guiHeight(), graphics.guiWidth());
            event.setCanceled(true);
        }
    }
}