
package com.lastimp.dgh.common.client.eventHandler;

import com.lastimp.dgh.common.enums.KeyPressedType;
import com.lastimp.dgh.common.tags.ModTags;
import com.lastimp.dgh.common.PlatformService;
import com.lastimp.dgh.common.container.DynamicSlot;

import com.lastimp.dgh.common.network.message.MyHealingItemUseData;
import com.lastimp.dgh.common.client.ClientAccessor;
import com.lastimp.dgh.common.client.gui.GuiOpenWrapper;
import com.lastimp.dgh.common.client.gui.screen.HealthScreen;
import com.lastimp.dgh.common.client.hotkey.KeyBinding;
import com.lastimp.dgh.common.network.message.MyKeyPressedData;
import com.lastimp.dgh.common.capability.HealthCapability;
import com.lastimp.dgh.common.entry.register.ModItems;
import com.lastimp.dgh.common.entry.register.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.Input;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

public class ClientInputEventHandler {
    private static int giveUpTick = 0;
    private static int callForHelpTick = 0;

    public static boolean onScannerHealing(int button, Screen screen) {
        if (button != 1) return true;
        if (!(screen instanceof HealthScreen)) return true;
        if (!screenHealingCheck()) return true;

        var healthScreen = GuiOpenWrapper.healthScreen();
        var slot = healthScreen.getSlotUnderMouse();
        int index = Objects.requireNonNull(slot).getSlotIndex();
        if (slot instanceof DynamicSlot)
            index += 36;
        PlatformService.NETWORK.sendToServer(MyHealingItemUseData.getInstance(
                healthScreen.getMenu().targetEntity, index, healthScreen.getSelectedComponent()
        ));

        ItemStack stack = slot.getItem();
        if (stack.is(ModTags.MEDICAL_TOOLS_SMALL_BAGS) && !stack.is(ModTags.MEDICAL_USAGE_BAGS)) {
            healthScreen.getMenu().openBag(stack);
        }
        if (stack.is(ModItems.AED)) {
            ClientAccessor.mc().getSoundManager().play(SimpleSoundInstance.forUI(ModSounds.AED.get(), 1.0f));
        }
        return false;
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

    public static boolean onUseMenuItem(int button, Screen screen) {
        if (button != 1) return true;
        if (!(screen instanceof InventoryScreen inventoryScreen)) return true;

        var slot = inventoryScreen.getSlotUnderMouse();
        if (!menuItemUseCheck(slot)) return true;

        int index = slot.getSlotIndex();
        PlatformService.NETWORK.sendToServer(MyKeyPressedData.getInstance(KeyPressedType.KEY_SLOT_USE, index));
        return false;
    }

    private static boolean menuItemUseCheck(Slot slot) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return false;
        if (!mc.level.isClientSide()) return false;
        if (mc.player == null) return false;

        if (slot == null) return false;

        var itemStack = slot.getItem();
        if (itemStack.isEmpty()) return false;
        if (itemStack.is(ModTags.MEDICAL_TOOLS_SMALL_BAGS)) return true;
        if (itemStack.is(ModItems.HEALTH_SCANNER.get())) return true;
        return false;
    }

    public static void openHealthMenu() {
        if (KeyBinding.OPEN_MENU_KEY.consumeClick()){
            PlatformService.NETWORK.sendToServer(MyKeyPressedData.getInstance(KeyPressedType.KEY_HEALTH_MENU, 0));
        }
    }

    public static boolean callForHelp(int action) {
        return ClientAccessor.getPlayer().map(player -> {
            if (HealthCapability.isDown(player)) {
                if (callForHelpTick <= 0 && action == GLFW.GLFW_PRESS) {
                    PlatformService.NETWORK.sendToServer(MyKeyPressedData.getInstance(KeyPressedType.CALL_FOR_HELP, 0));
                    callForHelpTick = 80;
                }
                return ClientAccessor.mc().screen != null;
            }
            return true;
        }).orElse(true);
    }

    public static void playerTick() {
        ClientAccessor.getPlayer().ifPresent(player -> {
            if (!PlatformService.CONFIG.ENABLE_SELF_SUICIDE()) return;

            if (KeyBinding.GIVE_UP.isDown() && HealthCapability.isDown(player)) {
                giveUpTick++;
                if (giveUpTick >= 100) {
                    PlatformService.NETWORK.sendToServer(MyKeyPressedData.getInstance(KeyPressedType.GIVE_UP, 0));
                }
            } else {
                giveUpTick = 0;
            }
        });
        callForHelpTick--;
    }

    public static void onMovementInput(Player player, Input input) {
        if (HealthCapability.isDown(player)) {
            input.jumping = false;
        }
    }

    public static void onInteractWithLiving(Player player, Entity target) {
        if (!player.level().isClientSide()) return;
        if (!target.isAlive() || !(target instanceof LivingEntity livingEntity)) return;
        if (!HealthCapability.has(livingEntity)) return;
        if (!player.getItemInHand(InteractionHand.MAIN_HAND).is(ModItems.AED)) return;

        ClientAccessor.mc().getSoundManager().play(SimpleSoundInstance.forUI(ModSounds.AED.get(), 1.0f));
    }
}