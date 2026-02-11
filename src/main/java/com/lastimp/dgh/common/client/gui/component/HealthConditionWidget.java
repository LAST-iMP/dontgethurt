
package com.lastimp.dgh.common.client.gui.component;

import com.lastimp.dgh.common.capability.bodyPart.base.BodyCondition;
import com.lastimp.dgh.common.client.ClientAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class HealthConditionWidget extends AbstractWidget {
    protected final ResourceLocation texture;
    protected final int iconSize = 12;
    protected int fgColor;
    protected float severity = 0f;

    public HealthConditionWidget(BodyCondition condition) {
        this(70, 16, condition.getComponent(), condition.texture, condition.color());
    }

    public HealthConditionWidget(int width, int height, Component message, ResourceLocation texture, int fgColor) {
        super(0, 0, width, height, message);
        this.texture = texture;
        this.fgColor = fgColor;
        this.visible = false;
    }

    public void setSeverity(float severity) {
        this.severity = Mth.clamp(severity, 0f, this.max());
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    public void setPortionColor(int color) {
        this.fgColor = color;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int i1, float v) {
        if (!this.visible || !this.active) return;

        this.renderBackGround(guiGraphics);
        this.renderBorder(guiGraphics);
        this.renderFiller(guiGraphics);
        this.renderIcon(guiGraphics);
        this.renderText(guiGraphics);
    }

    protected void renderBackGround(GuiGraphics guiGraphics) {
        int bgColor = 0xFF3A3C3B; // ARGB
        guiGraphics.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, bgColor);
    }

    protected void renderBorder(GuiGraphics guiGraphics) {
        int borderColor = 0xFF000000;
        guiGraphics.renderOutline(this.getX(), this.getY(), this.width, this.height, borderColor);
    }

    protected void renderFiller(GuiGraphics guiGraphics) {
        int filled = (int) (this.width * Math.min(severity, 1.0));
        guiGraphics.fill(this.getX() + 1, this.getY() + 1, Mth.clamp(this.getX() + filled, this.getX() + 1, this.getX() + this.width - 1), this.getY() + this.height - 1, fgColor);
    }

    protected void renderIcon(GuiGraphics guiGraphics) {
        // draw icon from texture (if you want to use atlas, supply proper tex size)
        guiGraphics.blitSprite(texture, this.getX() + 2, this.getY() + 2, iconSize, iconSize);
    }

    protected void renderText(GuiGraphics guiGraphics) {
        int stringColor = 0xFF000000;
        Minecraft mc = ClientAccessor.mc();
        guiGraphics.drawCenteredString(mc.font, this.getMessage(),
                this.getX() + 3 + (this.width + iconSize) / 2,
                this.getY() + (this.height - mc.font.lineHeight) / 2,
                stringColor);
    }

    protected float max() {
        return 1;
    }
}
