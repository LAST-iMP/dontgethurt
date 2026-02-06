package com.lastimp.dgh.common.client.gui;

import com.lastimp.dgh.common.client.ClientAccessor;
import com.lastimp.dgh.common.client.gui.screen.HealthScreen;

public class GuiOpenWrapper {
    private static HealthScreen<?> healthScreen = null;

    public static HealthScreen<?> healthScreen() {
        return healthScreen;
    }

    public static void setHealthScreen(HealthScreen<?> healthScreen) {
        GuiOpenWrapper.healthScreen = healthScreen;
    }

    public static void closeScreen() {
        ClientAccessor.mc().setScreen(null);
    }
}
