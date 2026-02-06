package com.lastimp.dgh.common.client.tooltip;

import com.lastimp.dgh.common.item.tool.AutoUseBag;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.client.gui.screens.inventory.tooltip.ClientBundleTooltip.TEXTURE_LOCATION;

public class ClientBagToolTip implements ClientTooltipComponent {
    private final NonNullList<ItemStack> items;
    
    public ClientBagToolTip(AutoUseBag.Tooltip tooltip) {
        this.items = tooltip.getItems();
    }

    public int getHeight() {
        return this.gridSizeY() * 20 + 2 + 4;
    }

    public int getWidth(@NotNull Font font) {
        return this.gridSizeX() * 18 + 2;
    }

    public void renderImage(@NotNull Font font, int x, int y, GuiGraphics gui) {
        int i = this.gridSizeX();
        int j = this.gridSizeY();
        int k = 0;

        for(int l = 0; l < j; ++l) {
            for(int i1 = 0; i1 < i; ++i1) {
                int j1 = x + i1 * 18 + 1;
                int k1 = y + l * 20 + 1;
                this.renderSlot(j1, k1, k++, gui, font);
            }
        }

        this.drawBorder(x, y, i, j, gui);
    }

    private void renderSlot(int x, int y, int index, GuiGraphics gui, Font font) {
        if (index >= this.items.size()) {
            this.blit(gui, x, y, ClientBagToolTip.Texture.SLOT);
        } else {
            ItemStack itemstack = this.items.get(index);
            this.blit(gui, x, y, ClientBagToolTip.Texture.SLOT);
            gui.renderItem(itemstack, x + 1, y + 1, index);
            gui.renderItemDecorations(font, itemstack, x + 1, y + 1);
        }
    }

    private void drawBorder(int x, int y, int size_x, int size_y, GuiGraphics gui) {
        this.blit(gui, x, y, ClientBagToolTip.Texture.BORDER_CORNER_TOP);
        this.blit(gui, x + size_x * 18 + 1, y, ClientBagToolTip.Texture.BORDER_CORNER_TOP);

        for(int i = 0; i < size_x; ++i) {
            this.blit(gui, x + 1 + i * 18, y, ClientBagToolTip.Texture.BORDER_HORIZONTAL_TOP);
            this.blit(gui, x + 1 + i * 18, y + size_y * 20, ClientBagToolTip.Texture.BORDER_HORIZONTAL_BOTTOM);
        }

        for(int j = 0; j < size_y; ++j) {
            this.blit(gui, x, y + j * 20 + 1, ClientBagToolTip.Texture.BORDER_VERTICAL);
            this.blit(gui, x + size_x * 18 + 1, y + j * 20 + 1, ClientBagToolTip.Texture.BORDER_VERTICAL);
        }

        this.blit(gui, x, y + size_y * 20, ClientBagToolTip.Texture.BORDER_CORNER_BOTTOM);
        this.blit(gui, x + size_x * 18 + 1, y + size_y * 20, ClientBagToolTip.Texture.BORDER_CORNER_BOTTOM);
    }

    private void blit(GuiGraphics gui, int x, int y, ClientBagToolTip.Texture texture) {
        gui.blit(TEXTURE_LOCATION, x, y, 0, (float)texture.x, (float)texture.y, texture.w, texture.h, 128, 128);
    }

    private int gridSizeX() {
        return Math.max(2, (int)Math.ceil(Math.sqrt(this.items.size())));
    }

    private int gridSizeY() {
        return (int)Math.ceil((double)this.items.size() / (double)this.gridSizeX());
    }

    enum Texture {
        SLOT(0, 0, 18, 20),
        BORDER_VERTICAL(0, 18, 1, 20),
        BORDER_HORIZONTAL_TOP(0, 20, 18, 1),
        BORDER_HORIZONTAL_BOTTOM(0, 60, 18, 1),
        BORDER_CORNER_TOP(0, 20, 1, 1),
        BORDER_CORNER_BOTTOM(0, 60, 1, 1);

        public final int x;
        public final int y;
        public final int w;
        public final int h;

        Texture(int p_169928_, int p_169929_, int p_169930_, int p_169931_) {
            this.x = p_169928_;
            this.y = p_169929_;
            this.w = p_169930_;
            this.h = p_169931_;
        }
    }
}
