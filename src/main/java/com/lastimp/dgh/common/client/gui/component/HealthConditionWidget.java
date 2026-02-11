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
import org.jetbrains.annotations.NotNull;

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
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int i, int i1, float v) {
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
        guiGraphics.pose().pushPose();
        float scale = (float) iconSize / 64f;
        guiGraphics.pose().scale(scale, scale, 1f);

        guiGraphics.blit(
                texture,
                (int) ((this.getX() + 2) / scale), (int) ((this.getY() + 2) / scale),
                0,      // blitOffset
                0f, 0f, // uOffset, vOffset
                64, 64, // regionWidth, regionHeight = 整个纹理
                64, 64  // textureWidth, textureHeight
        );
        guiGraphics.pose().popPose();
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
