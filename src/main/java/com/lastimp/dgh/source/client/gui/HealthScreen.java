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
import com.lastimp.dgh.network.message.Network;
import com.lastimp.dgh.source.core.healingSystem.HealingHandler;
import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.enums.BodyCondition;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import com.lastimp.dgh.source.item.HealthScanner;
import com.lastimp.dgh.network.ClientPayloadHandler;
import com.lastimp.dgh.network.message.MyReadAllConditionData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

import java.util.HashMap;
import java.util.List;

import static com.lastimp.dgh.api.enums.BodyComponents.*;
import static com.lastimp.dgh.api.enums.OperationType.HEALTH_SCANN;
import static com.lastimp.dgh.source.client.gui.HealthComponentWidget.*;

public class HealthScreen extends AbstractContainerScreen<HealthMenu> {
    private static final ResourceLocation HUD_BACKGROUND = ResourceLocation.fromNamespaceAndPath(DontGetHurt.MODID, "textures/gui/health_hud.png");

    private static final int PANEL_WIDTH = 238;   // 面板宽度
    private static final int PANEL_HEIGHT = 214;  // 面板高度

    private final HashMap<BodyComponents, HealthComponentWidget> componentWidgets = new HashMap<>();
    private final HashMap<BodyCondition, HealthConditionWidget> conditionWidgets = new HashMap<>();
    private BodyComponents selectedComponent = null;
    private static PlayerHealthCapability healthData = null;

    public HealthScreen(HealthMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        HealingHandler.setHealthScreen(this);
        ClientPayloadHandler.setHealthScreen(this);
    }

    @Override
    protected void init () {
        this.imageWidth = PANEL_WIDTH;
        this.imageHeight = PANEL_HEIGHT;
        super.init();

        // 清理旧的
        componentWidgets.clear();
        this.addHealthWidget(31, 6, 34, 32, HEAD, SPRITES_HEAD, SPRITES_HEAD_LIGHT);
        this.addHealthWidget(32,40, 32, 39, TORSO, SPRITES_TORSO, SPRITES_TORSO_LIGHT);
        this.addHealthWidget(20, 41, 12, 46, LEFT_ARM, SPRITES_LEFT_ARM, SPRITES_LEFT_ARM_LIGHT);
        this.addHealthWidget(65, 41, 12, 46, RIGHT_ARM, SPRITES_RIGHT_ARM, SPRITES_RIGHT_ARM_LIGHT);
        this.addHealthWidget(29, 79, 18, 45, LEFT_LEG, SPRITES_LEFT_LEG, SPRITES_LEFT_LEG_LIGHT);
        this.addHealthWidget(49, 79, 18, 45, RIGHT_LEG, SPRITES_RIGHT_LEG, SPRITES_RIGHT_LEG_LIGHT);

        conditionWidgets.clear();
        for (BodyCondition condition : HealthScanner.healthScannerConditions()) {
            addConditionWidget(condition);
        }
    }

    private void addHealthWidget(int x, int y, int width, int height, BodyComponents idx, ResourceLocation resource, ResourceLocation resourceLighted) {
        HealthComponentWidget w = new HealthComponentWidget(
                this.leftPos + x, this.topPos + y, width, height,
                Component.literal(idx.toString()),
                (button) -> {
                    this.selectedComponent = idx;
                },
                idx, resource, resourceLighted
        );
        componentWidgets.put(idx, w);
        this.addRenderableWidget(w);
    }

    private void addConditionWidget(BodyCondition condition) {
        HealthConditionWidget w = new HealthConditionWidget(
                70, 16, condition.getComponent(), condition.texture, condition.color
        );
        conditionWidgets.put(condition, w);
        this.addRenderableWidget(w);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null || mc.options.hideGui) return;
        this.refreshComponent();
        this.refreshCondition();

        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    private void refreshComponent() {
        if (healthData == null) return;
        if (this.componentWidgets.isEmpty()) return;

        for (BodyComponents component : BodyComponents.getVisibleBodies()) {
            AbstractBody body = healthData.getComponent(component);
            float injury = 0.0f;
            float pain = 0.0f;
            float comfort = 0.0f;
            for (BodyCondition condition : body.getBodyConditions()) {
                if (!this.visibilityCheck(body, condition)) continue;
                float value = body.getCondition(condition).getDisplayValue();
                if (!condition.abnormal(value)) continue;
                if (condition.isInjury()) injury += Mth.abs(value - condition.defaultValue);
                else if (condition.isPain()) pain += Mth.abs(value - condition.defaultValue);
                else if (condition.isComfort()) comfort += Mth.abs(value - condition.defaultValue);
            }
            float condition = (injury >= 0.01) ? injury : (pain > 0.01)? pain : (comfort > 0.01)? comfort : 0.0f;
            this.componentWidgets.get(component).setConditionValue(Mth.clamp(condition, 0.0f, 1.0f) * 0.7f);
            if (injury >= 0.01)     this.componentWidgets.get(component).setRedAndGreen(1.0f, 0.0f);
            else if (pain > 0.01)   this.componentWidgets.get(component).setRedAndGreen(1.0f, 1.0f);
            else if (comfort > 0.01)this.componentWidgets.get(component).setRedAndGreen(0.0f, 1.0f);
        }
    }

    private void refreshCondition() {
        if (selectedComponent == null) return;
        if (healthData == null) return;

        for (HealthConditionWidget widget : this.conditionWidgets.values()){
            widget.visible = false;
        }

        int widgetCount = 0;
        AbstractBody bodyPart = healthData.getComponent(this.selectedComponent);
        for (BodyCondition condition : bodyPart.getBodyConditions()) {
            HealthConditionWidget widget = this.conditionWidgets.get(condition);
            if (!this.visibilityCheck(bodyPart, condition)) continue;
            if (widgetCount > 12) break;

            widget.setPosition(
                    this.leftPos + 85 + (widgetCount % 2) * 72,
                    this.topPos + 11 + (widgetCount / 2) * 18
            );
            if (this.menu.isDevice)
                widget.setSeverity(bodyPart.getCondition(condition).getDisplayValue());
            else
                widget.setSeverity(0);
            widget.visible = true;
            widgetCount += 1;
        }
    }

    private boolean visibilityCheck(AbstractBody body, BodyCondition condition) {
        if (!HealthScanner.healthScannerConditions().contains(condition)) return false;
        if (!this.menu.isDevice && !HealthScanner.eyesightConditions().contains(condition)) return false;
        if (!condition.abnormal(body.getCondition(condition).getDisplayValue())) return false;
        return true;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick,  int mouseX, int mouseY) {
        int panelX = (guiGraphics.guiWidth() - PANEL_WIDTH) / 2;
        int panelY = (guiGraphics.guiHeight() - PANEL_HEIGHT) / 2;

        guiGraphics.blit(HUD_BACKGROUND, panelX, panelY, 0, 0, PANEL_WIDTH, PANEL_HEIGHT);
    }

    @Override
    public void onClose() {
        GuiOpenWrapper.MINECRAFT.get().setScreen(null);

        setHealthData(null);
        HealingHandler.setHealthScreen(null);
        ClientPayloadHandler.setHealthScreen(null);
        super.onClose();
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    }

    @Override
    protected void containerTick() {
        Network.SERVER_INSTANCE.sendToServer(MyReadAllConditionData.getInstance(
                this.menu.targetPlayer, null, HEALTH_SCANN
        ));
    }

    public void setHealthData(PlayerHealthCapability healthData) {
        HealthScreen.healthData = healthData;
    }

    public static PlayerHealthCapability getHealthData() {
        return healthData;
    }

    public BodyComponents getSelectedComponent() {
        return selectedComponent;
    }
}
