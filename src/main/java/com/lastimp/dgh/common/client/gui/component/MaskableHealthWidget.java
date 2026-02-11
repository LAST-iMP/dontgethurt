package com.lastimp.dgh.common.client.gui.component;

import com.lastimp.dgh.common.capability.bodyPart.base.BodyCondition;
import net.minecraft.client.gui.GuiGraphics;

public class MaskableHealthWidget extends HealthConditionWidget {
    protected int maskColor = 0x40000000;

    public MaskableHealthWidget(BodyCondition condition) {
        super(condition);
    }

    @Override
    protected void renderFiller(GuiGraphics guiGraphics) {
        super.renderFiller(guiGraphics);
        if (this.severity <= 1) return;

        var oriColor = this.fgColor;
        this.fgColor = maskColor;
        this.severity = Math.max(0, this.severity - 1);
        while (this.severity > 0) {
            super.renderFiller(guiGraphics);
            this.severity = Math.max(0, this.severity - 1);
        }
        this.fgColor = oriColor;
    }

    @Override
    protected float max() {
        return 256;
    }
}
