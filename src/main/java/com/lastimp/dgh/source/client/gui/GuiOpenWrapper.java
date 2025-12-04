package com.lastimp.dgh.source.client.gui;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.client.gui.screen.DyingScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.util.Lazy;

public class GuiOpenWrapper {
    // 懒加载Minecraft实例（避免提前初始化）
    public static final Lazy<Minecraft> MINECRAFT = Lazy.of(Minecraft::getInstance);

    public static void openDyingScreen() {
        MINECRAFT.get().forceSetScreen(new DyingScreen(Component.translatable("gui." + DontGetHurt.MODID + "dying_screen.title")));
    }

    public static void closeDyingScreen() {
        if (MINECRAFT.get().screen instanceof DyingScreen)
            MINECRAFT.get().setScreen(null);
    }
}
