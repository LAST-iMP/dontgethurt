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

package com.lastimp.dgh.source.core.healingSystem;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.api.healingItems.AbstractDirectHealItems;
import com.lastimp.dgh.api.healingItems.AbstractHealingItem;
import com.lastimp.dgh.api.healingItems.AbstractPartlyHealItem;
import com.lastimp.dgh.api.tags.ModTags;
import com.lastimp.dgh.source.client.gui.HealthScreen;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.source.item.Bandages;
import com.lastimp.dgh.network.message.MyHealingItemUseData;
import com.lastimp.dgh.source.item.Gypsum;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = DontGetHurt.MODID, bus = EventBusSubscriber.Bus.GAME)
public class HealingHandler {
    private static HealthScreen healthScreen = null;

    @SubscribeEvent
    public static void onScannerHealing(ScreenEvent.MouseButtonPressed.Pre event) {
        if (event.getButton() != 1) return;
        if (!screenHealingCheck()) return;

        assert healthScreen.getSlotUnderMouse() != null;
        int index = healthScreen.getSlotUnderMouse().getSlotIndex();
        PacketDistributor.sendToServer(MyHealingItemUseData.getInstance(
                healthScreen.getMenu().targetPlayer, index, healthScreen.getSelectedComponent()
        ));
        event.setCanceled(true);
    }

    private static boolean screenHealingCheck() {
        Minecraft mc = Minecraft.getInstance();
        if (!mc.level.isClientSide()) return false;
        if (mc.player == null) return false;
        if (healthScreen == null) return false;

        var slot = healthScreen.getSlotUnderMouse();
        if (slot == null) return false;

        var itemStack = slot.getItem();
        if (itemStack.isEmpty()) return false;
        if (itemStack.is(ModTags.SHEARS)) return true;
        if (!(itemStack.getItem() instanceof AbstractHealingItem)) return false;

        return true;
    }

    public static void useItemOn(ItemStack itemStack, @NotNull ServerPlayer source, ServerPlayer target, BodyComponents component) {
        if (target == null) return;
        if (itemStack.is(ModTags.SHEARS)) {
            Bandages.cut(target, component);
            Gypsum.cut(target, component);
            return;
        }
        boolean consume = false;
        if (itemStack.getItem() instanceof AbstractDirectHealItems item) {
            consume = item.heal(source, target);
        } else if (itemStack.getItem() instanceof AbstractPartlyHealItem item) {
            consume = item.heal(source, target, component);
        }
        if (consume) {
            itemStack.consume(1, target);
        }
    }

    public static void setHealthScreen(HealthScreen healthScreen) {
        HealingHandler.healthScreen = healthScreen;
    }
}
