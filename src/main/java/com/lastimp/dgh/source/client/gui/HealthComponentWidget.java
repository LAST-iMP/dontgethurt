/*
* MIT License

Copyright (c) 2023 NeoForged project

This license applies to the template files as supplied by github.com/NeoForged/MDK


Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package com.lastimp.dgh.source.client.gui;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class HealthComponentWidget extends Button {
    public static final ResourceLocation SPRITES_HEAD = ResourceLocation.fromNamespaceAndPath(DontGetHurt.MODID, "textures/gui/sprites/widget/health_hud_head.png");
    public static final ResourceLocation SPRITES_TORSO = ResourceLocation.fromNamespaceAndPath(DontGetHurt.MODID, "textures/gui/sprites/widget/health_hud_torso.png");
    public static final ResourceLocation SPRITES_LEFT_ARM = ResourceLocation.fromNamespaceAndPath(DontGetHurt.MODID, "textures/gui/sprites/widget/health_hud_left_arm.png");
    public static final ResourceLocation SPRITES_RIGHT_ARM = ResourceLocation.fromNamespaceAndPath(DontGetHurt.MODID, "textures/gui/sprites/widget/health_hud_right_arm.png");
    public static final ResourceLocation SPRITES_LEFT_LEG = ResourceLocation.fromNamespaceAndPath(DontGetHurt.MODID, "textures/gui/sprites/widget/health_hud_left_leg.png");
    public static final ResourceLocation SPRITES_RIGHT_LEG = ResourceLocation.fromNamespaceAndPath(DontGetHurt.MODID, "textures/gui/sprites/widget/health_hud_right_leg.png");
    public static final ResourceLocation SPRITES_HEAD_LIGHT = ResourceLocation.fromNamespaceAndPath(DontGetHurt.MODID, "textures/gui/sprites/widget/health_hud_head_lighted.png");
    public static final ResourceLocation SPRITES_TORSO_LIGHT = ResourceLocation.fromNamespaceAndPath(DontGetHurt.MODID, "textures/gui/sprites/widget/health_hud_torso_lighted.png");
    public static final ResourceLocation SPRITES_LEFT_ARM_LIGHT = ResourceLocation.fromNamespaceAndPath(DontGetHurt.MODID, "textures/gui/sprites/widget/health_hud_left_arm_lighted.png");
    public static final ResourceLocation SPRITES_RIGHT_ARM_LIGHT = ResourceLocation.fromNamespaceAndPath(DontGetHurt.MODID, "textures/gui/sprites/widget/health_hud_right_arm_lighted.png");
    public static final ResourceLocation SPRITES_LEFT_LEG_LIGHT = ResourceLocation.fromNamespaceAndPath(DontGetHurt.MODID, "textures/gui/sprites/widget/health_hud_left_leg_lighted.png");
    public static final ResourceLocation SPRITES_RIGHT_LEG_LIGHT = ResourceLocation.fromNamespaceAndPath(DontGetHurt.MODID, "textures/gui/sprites/widget/health_hud_right_leg_lighted.png");

    public final BodyComponents id;
    private final ResourceLocation resource;
    private final ResourceLocation resourceLighted;
    private float conditionValue;

    public HealthComponentWidget(int x, int y, int width, int height, Component message, OnPress onPress, BodyComponents id, ResourceLocation resource, ResourceLocation resourceLighted) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
        this.id = id;
        this.resource = resource;
        this.resourceLighted = resourceLighted;
    }

    @Override
    protected void renderWidget(GuiGraphics gui, int mouseX, int mouseY, float partialTick) {
        gui.setColor(1.0F, 0.2F, 0.2F, this.conditionValue);

        RenderSystem.enableBlend();
        gui.blit(this.resource, this.getX(), this.getY(), 0, 0, this.width, this.height, this.width, this.height);

        gui.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.isHoveredOrFocused())
            gui.blit(this.resourceLighted, this.getX(), this.getY(), 0, 0, this.width, this.height, this.width, this.height);
    }

    public void setConditionValue(float conditionValue) {
        this.conditionValue = conditionValue;
    }
}
