package com.lastimp.dgh.source.client.gui.component;

import com.lastimp.dgh.source.client.gui.GuiOpenWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(value = Dist.CLIENT)
public class HealthConditionWidget extends AbstractWidget {
    private final ResourceLocation texture;
    private final int iconSize = 12;
    private int fgColor;
    private float severity = 0f;

    public HealthConditionWidget(int width, int height, Component message, ResourceLocation texture, int fgColor) {
        super(0, 0, width, height, message);
        this.texture = texture;
        this.fgColor = fgColor;
        this.visible = false;
    }

    public void setSeverity(float severity) {
        this.severity = Mth.clamp(severity, 0f, 1f);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int i1, float v) {
        if (!this.visible || !this.active) return;
        // draw bar background
        int bgColor = 0xFF3A3C3B; // ARGB
        guiGraphics.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, bgColor);

        // optional border
        int borderColor = 0xFF000000;
        guiGraphics.renderOutline(this.getX(), this.getY(), this.width, this.height, borderColor);

        // draw filled portion
        int filled = (int) (this.width * Math.min(severity, 1.0));
        guiGraphics.fill(this.getX() + 1, this.getY() + 1, Mth.clamp(this.getX() + filled, this.getX() + 1, this.getX() + this.width - 1), this.getY() + this.height - 1, fgColor);
        if (severity > 1)
            guiGraphics.fill(this.getX() + 1, this.getY() + 1, Mth.clamp((int)(this.getX() + this.width * (severity - 1)), this.getX() + 1, this.getX() + this.width - 1), this.getY() + this.height - 1, 0xFF7E0000);

        // draw icon from texture (if you want to use atlas, supply proper tex size)

        guiGraphics.pose().pushPose();
        float scale = (float) iconSize / 64f;
        guiGraphics.pose().scale(scale, scale, 1f);

        int drawX = (int) ((this.getX() + 2) / scale);
        int drawY = (int) ((this.getY() + 2) / scale);
        guiGraphics.blit(
                texture,
                drawX, drawY,
                0,      // blitOffset
                0f, 0f, // uOffset, vOffset
                64, 64, // regionWidth, regionHeight = 整个纹理
                64, 64  // textureWidth, textureHeight
        );
        guiGraphics.pose().popPose();

        int stringColor = 0xFF000000;
        Minecraft mc = GuiOpenWrapper.MINECRAFT.get();
        guiGraphics.drawCenteredString(mc.font, this.getMessage(),
                this.getX() + 3 + (this.width + iconSize) / 2,
                this.getY() + (this.height - mc.font.lineHeight) / 2,
                stringColor);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    public void setPortionColor(int color) {
        this.fgColor = color;
    }
}
