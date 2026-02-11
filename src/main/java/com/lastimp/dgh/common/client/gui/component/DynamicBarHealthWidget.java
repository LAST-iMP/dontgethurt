package com.lastimp.dgh.common.client.gui.component;

import com.lastimp.dgh.common.capability.bodyPart.base.BodyCondition;
import net.minecraft.client.gui.GuiGraphics;

public class DynamicBarHealthWidget extends MaskableHealthWidget{
    protected int barColor;
    protected float barSeverity = 0;

    public DynamicBarHealthWidget(BodyCondition condition, int barColor) {
        super(condition);
        this.barColor = barColor;
    }

    @Override
    protected void renderBorder(GuiGraphics guiGraphics) {
        super.renderBorder(guiGraphics);
        int length = (int) (this.width * Math.min(this.barSeverity, 1.0));
        if (length >= 1) {
//            guiGraphics.fill(this.getX(), this.getY(), this.height, this.width, this.barColor);
            guiGraphics.fill(this.getX(), this.getY(), this.getX() + 1, this.getY() + this.height, this.barColor);
        }
        if (length > 1) {
            guiGraphics.fill(this.getX(), this.getY(), this.getX() + length, this.getY() + 1, this.barColor);
            guiGraphics.fill(this.getX(), this.getY() + this.height, this.getX() + length, this.getY() + 1, this.barColor);
        }
        if (length >= this.width) {
            guiGraphics.fill(this.getX() + this.width, this.getY(), this.getX() + 1, this.getY() + this.height, this.barColor);
        }
    }

    public void setBarColor(int barColor) {
        this.barColor = barColor;
    }

    public void setBarSeverity(float barSeverity) {
        this.barSeverity = barSeverity;
    }
}
