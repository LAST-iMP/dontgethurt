
package com.lastimp.dgh.source.client.gui.component;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.neoforge.Common;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;

public class HealthComponentWidget extends Button {
    public static final Identifier SPRITES_HEAD = Common.getId(DontGetHurt.MODID, "textures/gui/sprites/widget/health_hud_head.png");
    public static final Identifier SPRITES_TORSO = Common.getId(DontGetHurt.MODID, "textures/gui/sprites/widget/health_hud_torso.png");
    public static final Identifier SPRITES_LEFT_ARM = Common.getId(DontGetHurt.MODID, "textures/gui/sprites/widget/health_hud_left_arm.png");
    public static final Identifier SPRITES_RIGHT_ARM = Common.getId(DontGetHurt.MODID, "textures/gui/sprites/widget/health_hud_right_arm.png");
    public static final Identifier SPRITES_LEFT_LEG = Common.getId(DontGetHurt.MODID, "textures/gui/sprites/widget/health_hud_left_leg.png");
    public static final Identifier SPRITES_RIGHT_LEG = Common.getId(DontGetHurt.MODID, "textures/gui/sprites/widget/health_hud_right_leg.png");
    public static final Identifier SPRITES_HEAD_LIGHT = Common.getId(DontGetHurt.MODID, "textures/gui/sprites/widget/health_hud_head_lighted.png");
    public static final Identifier SPRITES_TORSO_LIGHT = Common.getId(DontGetHurt.MODID, "textures/gui/sprites/widget/health_hud_torso_lighted.png");
    public static final Identifier SPRITES_LEFT_ARM_LIGHT = Common.getId(DontGetHurt.MODID, "textures/gui/sprites/widget/health_hud_left_arm_lighted.png");
    public static final Identifier SPRITES_RIGHT_ARM_LIGHT = Common.getId(DontGetHurt.MODID, "textures/gui/sprites/widget/health_hud_right_arm_lighted.png");
    public static final Identifier SPRITES_LEFT_LEG_LIGHT = Common.getId(DontGetHurt.MODID, "textures/gui/sprites/widget/health_hud_left_leg_lighted.png");
    public static final Identifier SPRITES_RIGHT_LEG_LIGHT = Common.getId(DontGetHurt.MODID, "textures/gui/sprites/widget/health_hud_right_leg_lighted.png");

    public final BodyComponents id;
    private final Identifier resource;
    private final Identifier resourceLighted;
    private float conditionValue;
    public float red;
    private float green;

    public HealthComponentWidget(int x, int y, int width, int height, Component message, OnPress onPress, BodyComponents id, Identifier resource, Identifier resourceLighted) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
        this.id = id;
        this.resource = resource;
        this.resourceLighted = resourceLighted;
    }

    @Override
    protected void renderContents(GuiGraphics gui, int mouseX, int mouseY, float partialTick) {
        int color = ARGB.color((int) (this.conditionValue * 255), (int) (this.conditionValue * this.red * 255), (int) (this.conditionValue * this.green * 255), (int) (this.conditionValue * 0.2F * 255));
        gui.blit(RenderPipelines.GUI_TEXTURED_PREMULTIPLIED_ALPHA, this.resource, this.getX(), this.getY(), 0, 0, this.width, this.height, this.width, this.height, color);

        if (this.isHoveredOrFocused())
            gui.blit(RenderPipelines.GUI_TEXTURED_PREMULTIPLIED_ALPHA, this.resourceLighted, this.getX(), this.getY(), 0, 0, this.width, this.height, this.width, this.height, ARGB.white(1));
    }

    public void setConditionValue(float conditionValue) {
        this.conditionValue = conditionValue;
    }

    public void setRedAndGreen(float red, float green) {
        this.red = red;
        this.green = green;
    }
}
