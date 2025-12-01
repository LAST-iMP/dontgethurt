
package com.lastimp.dgh.source.core.healingSystem;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.api.healingItems.AbstractDirectHealItems;
import com.lastimp.dgh.api.healingItems.AbstractHealingItem;
import com.lastimp.dgh.api.healingItems.AbstractPartlyHealItem;
import com.lastimp.dgh.api.tags.ModTags;
import com.lastimp.dgh.network.message.Network;
import com.lastimp.dgh.source.client.gui.HealthScreen;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.source.core.Utils;
import com.lastimp.dgh.source.item.medicine.Bandages;
import com.lastimp.dgh.network.message.MyHealingItemUseData;
import com.lastimp.dgh.source.item.medicine.Gypsum;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import static com.lastimp.dgh.api.tags.ModTags.MEDICAL_TOOLS_SHEARS;

@Mod.EventBusSubscriber(modid = DontGetHurt.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HealingHandler {
    private static HealthScreen healthScreen = null;

    @SubscribeEvent
    public static void onScannerHealing(ScreenEvent.MouseButtonPressed event) {
        if (event.getButton() != 1) return;
        if (!screenHealingCheck()) return;

        assert healthScreen.getSlotUnderMouse() != null;
        int index = healthScreen.getSlotUnderMouse().getSlotIndex();
        Network.SERVER_INSTANCE.sendToServer(MyHealingItemUseData.getInstance(
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
        if (itemStack.is(ModTags.MEDICINE)) return true;
        if (itemStack.is(ModTags.MEDICAL_TOOLS)) return true;

        return false;
    }

    public static void useItemOn(ItemStack itemStack, @NotNull ServerPlayer source, ServerPlayer target, BodyComponents component) {
        if (target == null) return;
        boolean success = false;
        if (itemStack.is(MEDICAL_TOOLS_SHEARS)) {
            success |= Bandages.cut(target, component);
            success |= Gypsum.cut(target, component);
        }
        if (itemStack.getItem() instanceof AbstractDirectHealItems item) {
            success = item.heal(source, target);
        } else if (itemStack.getItem() instanceof AbstractPartlyHealItem item) {
            success = item.heal(source, target, component);
        }
        if (success && itemStack.isDamageableItem()) {
            itemStack.hurtAndBreak(1, source, (player) -> {});
            if (itemStack.getDamageValue() >= itemStack.getMaxDamage())
                itemStack.shrink(1);
        } else if (success) {
            itemStack.shrink(1);
        }
    }

    public static void setHealthScreen(HealthScreen healthScreen) {
        HealingHandler.healthScreen = healthScreen;
    }
}
