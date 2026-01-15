
package com.lastimp.dgh.source.client.eventHandler;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.api.enums.KeyPressedType;
import com.lastimp.dgh.api.tags.ModTags;
import com.lastimp.dgh.network.message.MyHealingItemUseData;
import com.lastimp.dgh.source.client.ClientAccessor;
import com.lastimp.dgh.source.client.gui.GuiOpenWrapper;
import com.lastimp.dgh.source.client.gui.screen.HealthScreen;
import com.lastimp.dgh.source.client.hotkey.KeyBinding;
import com.lastimp.dgh.network.message.MyKeyPressedData;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.core.menu.component.DynamicSlot;
import com.lastimp.dgh.source.register.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

@OnlyIn(value = Dist.CLIENT)
@EventBusSubscriber(modid = DontGetHurt.MODID,value = Dist.CLIENT)
public class ClientInputEventHandler {
    private static HealthScreen healthScreen = null;
    private static int giveUpTick = 0;
    private static int callForHelpTick = 0;

    @SubscribeEvent
    public static void onScannerHealing(ScreenEvent.MouseButtonPressed.Pre event) {
        if (event.getButton() != 1) return;
        if (!screenHealingCheck()) return;

        assert healthScreen.getSlotUnderMouse() != null;
        var slot = healthScreen.getSlotUnderMouse();
        int index = slot.getSlotIndex();
        if (slot instanceof DynamicSlot)
            index += 36;
        PacketDistributor.sendToServer(MyHealingItemUseData.getInstance(
                healthScreen.getMenu().targetEntity, index, healthScreen.getSelectedComponent()
        ));

        ItemStack stack = GuiOpenWrapper.MINECRAFT.get().player.getInventory().getItem(index);
        if (stack.is(ModTags.MEDICAL_TOOLS_BAGS) && event.getScreen() instanceof HealthScreen healthScreen) {
            healthScreen.getMenu().openBag(stack);
        }
        event.setCanceled(true);
    }

    private static boolean screenHealingCheck() {
        Minecraft mc = GuiOpenWrapper.MINECRAFT.get();
        if (mc.level == null) return false;
        if (!mc.level.isClientSide()) return false;
        if (mc.player == null) return false;
        if (healthScreen == null) return false;

        var slot = healthScreen.getSlotUnderMouse();
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
        PacketDistributor.sendToServer(MyKeyPressedData.getInstance(KeyPressedType.KEY_SLOT_USE, index));
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
        if (itemStack.is(ModItems.HEALTH_SCANNER)) return true;
        return false;
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (KeyBinding.OPEN_MENU_KEY.consumeClick()){
            PacketDistributor.sendToServer(MyKeyPressedData.getInstance(KeyPressedType.KEY_HEALTH_MENU, 0));
        }
    }

    @SubscribeEvent
    public static void onMouseInput(InputEvent.MouseButton.Pre event) {
        if (event.getAction() == GLFW.GLFW_PRESS) return;
        if (callForHelpTick > 0) return;
        ClientAccessor.getPlayer().ifPresent(player -> {
            if (HealthCapability.isDying(player)) {
                PacketDistributor.sendToServer(MyKeyPressedData.getInstance(KeyPressedType.CALL_FOR_HELP, 0));
                callForHelpTick = 80;
            }
        });
    }

    @SubscribeEvent
    public static void playerTick(PlayerTickEvent.Pre event) {
        if (HealthCapability.isDying(event.getEntity()))
            event.getEntity().setPose(Pose.SWIMMING);
    }

    @SubscribeEvent
    public static void playerTick(PlayerTickEvent.Post event) {
        if (HealthCapability.isDying(event.getEntity()))
            event.getEntity().setPose(Pose.SWIMMING);

        ClientAccessor.getPlayer().ifPresent(player -> {
            if (!event.getEntity().getUUID().equals(player.getUUID())) return;

            if (KeyBinding.GIVE_UP.isDown() && HealthCapability.isDying(event.getEntity())) {
                giveUpTick++;
                if (giveUpTick >= 100) {
                    PacketDistributor.sendToServer(MyKeyPressedData.getInstance(KeyPressedType.GIVE_UP, 0));
                }
            } else {
                giveUpTick = 0;
            }
        });
        callForHelpTick--;
    }

    public static void setHealthScreen(HealthScreen healthScreen) {
        ClientInputEventHandler.healthScreen = healthScreen;
    }

}