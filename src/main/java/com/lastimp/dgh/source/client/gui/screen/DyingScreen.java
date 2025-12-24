package com.lastimp.dgh.source.client.gui.screen;

import com.lastimp.dgh.api.enums.KeyPressedType;
import com.lastimp.dgh.network.message.MyKeyPressedData;
import com.lastimp.dgh.source.client.gui.GuiOpenWrapper;
import com.lastimp.dgh.source.client.hotkey.KeyBinding;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;

import java.awt.*;

@OnlyIn(value = Dist.CLIENT)
public class DyingScreen extends Screen {
    private static final int PANEL_WIDTH = 238;   // 面板宽度
    private static final int PANEL_HEIGHT = 214;  // 面板高度
    private final CameraType oldCameraType;
    private static int giveUpTick = 0;

    public DyingScreen(Component title) {
        super(title);
        oldCameraType = GuiOpenWrapper.MINECRAFT.get().options.getCameraType();
        GuiOpenWrapper.MINECRAFT.get().options.setCameraType(CameraType.THIRD_PERSON_BACK);
    }

    @Override
    protected void init () {
        this.width = PANEL_WIDTH;
        this.height = PANEL_HEIGHT;
        giveUpTick = 0;
        super.init();
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics,  int mouseX, int mouseY, float partialTick) {
        int panelX = (guiGraphics.guiWidth() - PANEL_WIDTH) / 2;
        int panelY = (guiGraphics.guiHeight() - PANEL_HEIGHT) / 2;
        guiGraphics.drawCenteredString(GuiOpenWrapper.MINECRAFT.get().font,
                "单击任意键求救, 按住ESC键(默认)5秒放弃治疗",
                guiGraphics.guiWidth() / 2, guiGraphics.guiHeight() / 2, 0xFF000000
        );
        guiGraphics.fill(panelX, panelY, panelX + PANEL_WIDTH, panelY + PANEL_HEIGHT, 0x00ffffff);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return false;

        if (KeyBinding.GIVE_UP.getKey().getValue() == keyCode) {
            giveUpTick ++;
            if (giveUpTick >= 100) {
                PacketDistributor.sendToServer(MyKeyPressedData.getInstance(KeyPressedType.GIVE_UP, 0));
            }
        }
        return true;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return false;

        if (KeyBinding.GIVE_UP.getKey().getValue() != keyCode) {
            PacketDistributor.sendToServer(MyKeyPressedData.getInstance(KeyPressedType.CALL_FOR_HELP, 0));
        }
        giveUpTick = 0;
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public void onClose() {
        GuiOpenWrapper.MINECRAFT.get().options.setCameraType(oldCameraType);
        super.onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
