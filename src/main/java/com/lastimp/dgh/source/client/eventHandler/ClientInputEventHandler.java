
package com.lastimp.dgh.source.client.eventHandler;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.api.enums.KeyPressedType;
import com.lastimp.dgh.api.tags.ModTags;
import com.lastimp.dgh.network.message.MyHealingItemUseData;
import com.lastimp.dgh.network.message.Network;
import com.lastimp.dgh.source.client.ClientAccessor;
import com.lastimp.dgh.source.client.gui.GuiOpenWrapper;
import com.lastimp.dgh.source.client.gui.screen.HealthScreen;
import com.lastimp.dgh.source.core.menu.component.DynamicSlot;
import com.lastimp.dgh.source.client.hotkey.KeyBinding;
import com.lastimp.dgh.network.message.MyKeyPressedData;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.register.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

@OnlyIn(value = Dist.CLIENT)
@Mod.EventBusSubscriber(modid = DontGetHurt.MODID, value = Dist.CLIENT)
public class ClientInputEventHandler {
    private static int giveUpTick = 0;
    private static int callForHelpTick = 0;

    @SubscribeEvent
    public static void onScannerHealing(ScreenEvent.MouseButtonPressed.Pre event) {
        if (event.getButton() != 1) return;
        if (!(event.getScreen() instanceof HealthScreen)) return;
        if (!screenHealingCheck()) return;

        var healthScreen = GuiOpenWrapper.healthScreen();
        var slot = healthScreen.getSlotUnderMouse();
        int index = Objects.requireNonNull(slot).getSlotIndex();
        if (slot instanceof DynamicSlot)
            index += 36;
        Network.SERVER_INSTANCE.sendToServer(MyHealingItemUseData.getInstance(
                healthScreen.getMenu().targetEntity, index, healthScreen.getSelectedComponent()
        ));

        ItemStack stack = slot.getItem();
        if (stack.is(ModTags.MEDICAL_TOOLS_BAGS)) {
            healthScreen.getMenu().openBag(stack);
        }
        event.setCanceled(true);
    }

    private static boolean screenHealingCheck() {
        Minecraft mc = ClientAccessor.mc();
        if (mc.level == null) return false;
        if (!mc.level.isClientSide()) return false;
        if (mc.player == null) return false;
        if (GuiOpenWrapper.healthScreen() == null) return false;

        var slot = GuiOpenWrapper.healthScreen().getSlotUnderMouse();
        if (slot == null) return false;

        var itemStack = slot.getItem();
        if (itemStack.isEmpty()) return false;
        return ModTags.isHealthScreenAvaItem(itemStack);
    }

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
        if (mc.level == null) return false;
        if (!mc.level.isClientSide()) return false;
        if (mc.player == null) return false;

        if (slot == null) return false;

        var itemStack = slot.getItem();
        if (itemStack.isEmpty()) return false;
        if (itemStack.is(ModTags.MEDICAL_TOOLS_BAGS)) return true;
        if (itemStack.is(ModItems.HEALTH_SCANNER.get())) return true;
        return false;
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (KeyBinding.OPEN_MENU_KEY.consumeClick()){
            Network.SERVER_INSTANCE.sendToServer(MyKeyPressedData.getInstance(KeyPressedType.KEY_HEALTH_MENU, 0));
        }
    }

    @SubscribeEvent
    public static void onMouseInput(InputEvent.MouseButton.Pre event) {
        if (event.getAction() == GLFW.GLFW_PRESS) return;
        if (callForHelpTick > 0) return;
        ClientAccessor.getPlayer().ifPresent(player -> {
            if (HealthCapability.isDying(player)) {
                Network.SERVER_INSTANCE.sendToServer(MyKeyPressedData.getInstance(KeyPressedType.CALL_FOR_HELP, 0));
                callForHelpTick = 80;
            }
        });
    }

    @SubscribeEvent
    public static void onInputTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        ClientAccessor.getPlayer().ifPresent(player -> {
            if (!event.player.getUUID().equals(player.getUUID())) return;

            if (KeyBinding.GIVE_UP.isDown() && HealthCapability.isDying(event.player)) {
                giveUpTick++;
                if (giveUpTick >= 100) {
                    Network.SERVER_INSTANCE.sendToServer(MyKeyPressedData.getInstance(KeyPressedType.GIVE_UP, 0));
                }
            } else {
                giveUpTick = 0;
            }
        });
        callForHelpTick--;
    }
}