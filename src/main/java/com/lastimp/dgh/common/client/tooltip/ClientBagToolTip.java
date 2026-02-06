package com.lastimp.dgh.common.client.tooltip;

import com.lastimp.dgh.common.item.tool.AutoUseBag;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ClientBagToolTip implements ClientTooltipComponent {
    private static final ResourceLocation BACKGROUND_SPRITE = ResourceLocation.withDefaultNamespace("container/bundle/background");
    private final NonNullList<ItemStack> items;
    
    public ClientBagToolTip(AutoUseBag.Tooltip tooltip) {
        this.items = tooltip.getItems();
    }

    public int getHeight() {
        return this.backgroundHeight() + 4;
    }

    public int getWidth(Font font) {
        return this.backgroundWidth();
    }

    private int backgroundWidth() {
        return this.gridSizeX() * 18 + 2;
    }

    private int backgroundHeight() {
        return this.gridSizeY() * 20 + 2;
    }

    public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
        int i = this.gridSizeX();
        int j = this.gridSizeY();
        guiGraphics.blitSprite(BACKGROUND_SPRITE, x, y, this.backgroundWidth(), this.backgroundHeight());
        int k = 0;

        for(int l = 0; l < j; ++l) {
            for(int i1 = 0; i1 < i; ++i1) {
                int j1 = x + i1 * 18 + 1;
                int k1 = y + l * 20 + 1;
                this.renderSlot(j1, k1, k++, guiGraphics, font);
            }
        }

    }

    private void renderSlot(int x, int y, int itemIndex, GuiGraphics guiGraphics, Font font) {
        if (itemIndex >= this.items.size()) {
            this.blit(guiGraphics, x, y, ClientBagToolTip.Texture.SLOT);
        } else {
            ItemStack itemstack = this.items.get(itemIndex);
            this.blit(guiGraphics, x, y, ClientBagToolTip.Texture.SLOT);
            guiGraphics.renderItem(itemstack, x + 1, y + 1, itemIndex);
            guiGraphics.renderItemDecorations(font, itemstack, x + 1, y + 1);
        }

    }

    private void blit(GuiGraphics guiGraphics, int x, int y, ClientBagToolTip.Texture texture) {
        guiGraphics.blitSprite(texture.sprite, x, y, 0, texture.w, texture.h);
    }

    private int gridSizeX() {
        return Math.max(2, (int)Math.ceil(Math.sqrt(this.items.size())));
    }

    private int gridSizeY() {
        return (int)Math.ceil((double)this.items.size() / (double)this.gridSizeX());
    }

    enum Texture {
        SLOT(ResourceLocation.withDefaultNamespace("container/bundle/slot"), 18, 20);

        public final ResourceLocation sprite;
        public final int w;
        public final int h;

        Texture(ResourceLocation sprite, int w, int h) {
            this.sprite = sprite;
            this.w = w;
            this.h = h;
        }
    }
}
