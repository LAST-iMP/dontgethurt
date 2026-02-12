package com.lastimp.dgh.common.client.gui.component;

import com.lastimp.dgh.common.capability.bodyPart.base.BodyCondition;
import net.minecraft.client.gui.GuiGraphics;

public class DynamicBarHealthWidget extends MaskableHealthWidget{
    protected int[] barColor;
    protected float[] barSeverity;

    public DynamicBarHealthWidget(BodyCondition condition, int ...barColor) {
        super(condition);
        this.barColor = barColor;
    }

    @Override
    protected void renderBorder(GuiGraphics guiGraphics) {
        super.renderBorder(guiGraphics);
        int level = 0;
        for (int i = 0; i < barSeverity.length; i++) {
            if (this.barSeverity[i] > 0) {
                this.renderOutLine(guiGraphics, this.barSeverity[i], level++, this.barColor[i]);
            }
        }
    }

    protected void renderOutLine(GuiGraphics guiGraphics, float ratio, int shrink, int color) {
        int x = this.getX() + shrink;
        int y = this.getY() + shrink;
        int height = this.height - shrink * 2;
        int width = this.width - shrink * 2;
        int length = (int) ((this.width - shrink * 2) * Math.min(ratio, 1.0));
        if (length >= 1) {
            guiGraphics.fill(x, y, x + 1, y + height, color);
        }
        if (length > 1) {
            guiGraphics.fill(x, y, x + length, y + 1, color);
            guiGraphics.fill(x, y + height - 1, x + length, y + height, color);
        }
        if (length >= width) {
            guiGraphics.fill(x + width, y, x + width + 1, y + height, color);
        }

        ratio = Math.max(0, ratio - 1);
        while (ratio > 0) {
            renderOutLine(guiGraphics, ratio, shrink, this.maskColor);
            ratio = Math.max(0, ratio - 1);
        }
    }

    public void setBarColor(int ...barColor) {
        this.barColor = barColor;
    }

    public void setBarSeverity(float ...barSeverity) {
        this.barSeverity = barSeverity;
    }
}
