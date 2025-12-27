package com.lastimp.dgh.source.client.gui;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.client.gui.screen.BagScreen;
import com.lastimp.dgh.source.client.gui.screen.DyingScreen;
import com.lastimp.dgh.source.client.gui.screen.HealthScreen;
import com.lastimp.dgh.source.register.ModMenus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.UUID;

@OnlyIn(value = Dist.CLIENT)
@Mod.EventBusSubscriber(modid = DontGetHurt.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class GuiOpenWrapper {
    // 懒加载Minecraft实例（避免提前初始化）
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
    static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(ModMenus.HEALTH_MENU.get(), HealthScreen::new);
            MenuScreens.register(ModMenus.HEALTH_CARE_BAG_MENU.get(), BagScreen::new);
            MenuScreens.register(ModMenus.SURGERY_TOOL_BAG_MENU.get(), BagScreen::new);
        });
    }
}
