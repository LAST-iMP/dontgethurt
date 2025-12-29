
package com.lastimp.dgh.source.client.gui;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.client.gui.screen.BagScreen;
import com.lastimp.dgh.source.client.gui.screen.DyingScreen;
import com.lastimp.dgh.source.client.gui.screen.HealthScreen;
import com.lastimp.dgh.source.register.ModMenus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.util.Lazy;

import java.util.UUID;

@OnlyIn(value = Dist.CLIENT)
@EventBusSubscriber(modid = DontGetHurt.MODID, value = Dist.CLIENT)
public class GuiOpenWrapper {
    public static final Lazy<Minecraft> MINECRAFT = Lazy.of(Minecraft::getInstance);

    public static void closeScreen() {
        MINECRAFT.get().setScreen(null);
    }

    public static boolean canOpenDyingScreen() {
        var screen = MINECRAFT.get().screen;
        if (screen == null) return true;
        if (screen instanceof DyingScreen) return false;

        return screen instanceof AbstractContainerScreen<?>;
    }

    public static void openDyingScreen() {
        if (MINECRAFT.get().screen instanceof DyingScreen) return;
        closeScreen();
        MINECRAFT.get().forceSetScreen(new DyingScreen(Component.translatable("gui." + DontGetHurt.MODID + "dying_screen.title")));
    }

    public static void closeDyingScreen() {
        if (MINECRAFT.get().screen instanceof DyingScreen)
            closeScreen();
    }

    public static UUID localPlayerUUID() {
        return GuiOpenWrapper.MINECRAFT.get().player.getUUID();
    }

    @SubscribeEvent
    public static void registerScreens(final RegisterMenuScreensEvent event) {
        event.register(ModMenus.HEALTH_MENU.get(), HealthScreen::new);
        event.register(ModMenus.HEALTH_CARE_BAG_MENU.get(), BagScreen::new);
        event.register(ModMenus.SURGERY_TOOL_BAG_MENU.get(), BagScreen::new);
        event.register(ModMenus.LIMB_REF_BAG_MENU.get(), BagScreen::new);
    }
}
