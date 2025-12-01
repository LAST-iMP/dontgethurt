package com.lastimp.dgh.source.core.player;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.api.enums.KeyPressedType;
import com.lastimp.dgh.network.message.MyKeyPressedData;
import com.lastimp.dgh.network.message.Network;
import com.lastimp.dgh.source.register.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.entity.living.LivingBreatheEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.lastimp.dgh.api.enums.BodyComponents.TORSO;
import static com.lastimp.dgh.api.enums.BodyCondition.RESPIRATORY_ARREST;

@Mod.EventBusSubscriber(modid = DontGetHurt.MODID)
public class PlayerEventHandler {
    @SubscribeEvent
    public static void onUseMenuItem(ScreenEvent.MouseButtonPressed.Pre event) {
        if (event.getButton() != 1) return;
        if (!(event.getScreen() instanceof InventoryScreen screen)) return;

        var slot = screen.getSlotUnderMouse();
        if (!menuItemUseCheck(slot)) return;

        int index = slot.getSlotIndex();
        Network.SERVER_INSTANCE.sendToServer(MyKeyPressedData.getInstance(KeyPressedType.KEY_SLOT_USE, index));
        event.setCanceled(true);
    }

    private static boolean menuItemUseCheck(Slot slot) {
        Minecraft mc = Minecraft.getInstance();
        if (!mc.level.isClientSide()) return false;
        if (mc.player == null) return false;

        if (slot == null) return false;

        var itemStack = slot.getItem();
        if (itemStack.isEmpty()) return false;
        if (itemStack.is(ModItems.HEALTH_CARE_BAG.get())) return true;
        if (itemStack.is(ModItems.SURGERY_TOOL_BAG.get())) return true;
        if (itemStack.is(ModItems.HEALTH_SCANNER.get())) return true;
        return false;
    }

    @SubscribeEvent
    public static void onBreath(LivingBreatheEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        if (!(event.getEntity() instanceof Player player)) return;

        var health = PlayerHealthCapability.get(player);
        if (health.getComponent(TORSO).abnormal(RESPIRATORY_ARREST)) {
            event.setCanBreathe(false);
        }
    }
}
