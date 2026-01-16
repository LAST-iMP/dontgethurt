package com.lastimp.dgh.source.client.gui;

import com.lastimp.dgh.source.client.ClientAccessor;
import com.lastimp.dgh.source.client.gui.screen.HealthScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(value = Dist.CLIENT)
public class GuiOpenWrapper {
    private static HealthScreen healthScreen = null;

    public static HealthScreen healthScreen() {
        return healthScreen;
    }

    public static void setHealthScreen(HealthScreen healthScreen) {
        GuiOpenWrapper.healthScreen = healthScreen;
    }

    public static void closeScreen() {
        ClientAccessor.mc().setScreen(null);
    }
}
