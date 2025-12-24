
package com.lastimp.dgh.source.client.gui.component;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.neoforge.Common;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(value = Dist.CLIENT)
public class HealthComponentWidget extends Button {
    public static final ResourceLocation SPRITES_HEAD = Common.ResourceLocation(DontGetHurt.MODID, "textures/gui/sprites/widget/health_hud_head.png");
    public static final ResourceLocation SPRITES_TORSO = Common.ResourceLocation(DontGetHurt.MODID, "textures/gui/sprites/widget/health_hud_torso.png");
    public static final ResourceLocation SPRITES_LEFT_ARM = Common.ResourceLocation(DontGetHurt.MODID, "textures/gui/sprites/widget/health_hud_left_arm.png");
    public static final ResourceLocation SPRITES_RIGHT_ARM = Common.ResourceLocation(DontGetHurt.MODID, "textures/gui/sprites/widget/health_hud_right_arm.png");
    public static final ResourceLocation SPRITES_LEFT_LEG = Common.ResourceLocation(DontGetHurt.MODID, "textures/gui/sprites/widget/health_hud_left_leg.png");
    public static final ResourceLocation SPRITES_RIGHT_LEG = Common.ResourceLocation(DontGetHurt.MODID, "textures/gui/sprites/widget/health_hud_right_leg.png");
    public static final ResourceLocation SPRITES_HEAD_LIGHT = Common.ResourceLocation(DontGetHurt.MODID, "textures/gui/sprites/widget/health_hud_head_lighted.png");
    public static final ResourceLocation SPRITES_TORSO_LIGHT = Common.ResourceLocation(DontGetHurt.MODID, "textures/gui/sprites/widget/health_hud_torso_lighted.png");
    public static final ResourceLocation SPRITES_LEFT_ARM_LIGHT = Common.ResourceLocation(DontGetHurt.MODID, "textures/gui/sprites/widget/health_hud_left_arm_lighted.png");
    public static final ResourceLocation SPRITES_RIGHT_ARM_LIGHT = Common.ResourceLocation(DontGetHurt.MODID, "textures/gui/sprites/widget/health_hud_right_arm_lighted.png");
    public static final ResourceLocation SPRITES_LEFT_LEG_LIGHT = Common.ResourceLocation(DontGetHurt.MODID, "textures/gui/sprites/widget/health_hud_left_leg_lighted.png");
    public static final ResourceLocation SPRITES_RIGHT_LEG_LIGHT = Common.ResourceLocation(DontGetHurt.MODID, "textures/gui/sprites/widget/health_hud_right_leg_lighted.png");

    public final BodyComponents id;
    private final ResourceLocation resource;
    private final ResourceLocation resourceLighted;
    private float conditionValue;
    public float red;
    private float green;

    public HealthComponentWidget(int x, int y, int width, int height, Component message, OnPress onPress, BodyComponents id, ResourceLocation resource, ResourceLocation resourceLighted) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
        this.id = id;
        this.resource = resource;
        this.resourceLighted = resourceLighted;
    }

    @Override
    protected void renderWidget(GuiGraphics gui, int mouseX, int mouseY, float partialTick) {
        gui.setColor(this.red, this.green, 0.2F, this.conditionValue);

        RenderSystem.enableBlend();
        gui.blit(this.resource, this.getX(), this.getY(), 0, 0, this.width, this.height, this.width, this.height);

        gui.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.isHoveredOrFocused())
            gui.blit(this.resourceLighted, this.getX(), this.getY(), 0, 0, this.width, this.height, this.width, this.height);
    }

    public void setConditionValue(float conditionValue) {
        this.conditionValue = conditionValue;
    }

    public void setRedAndGreen(float red, float green) {
        this.red = red;
        this.green = green;
    }
}
