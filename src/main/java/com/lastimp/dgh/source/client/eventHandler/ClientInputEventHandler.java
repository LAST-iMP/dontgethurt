
package com.lastimp.dgh.source.client.eventHandler;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.api.enums.KeyPressedType;
import com.lastimp.dgh.api.tags.ModTags;
import com.lastimp.dgh.config.Config;
import com.lastimp.dgh.neoforge.Common;
import com.lastimp.dgh.network.message.MyHealingItemUseData;
import com.lastimp.dgh.source.client.ClientAccessor;
import com.lastimp.dgh.source.client.gui.GuiOpenWrapper;
import com.lastimp.dgh.source.client.gui.screen.HealthScreen;
import com.lastimp.dgh.source.core.menu.component.DynamicSlot;
import com.lastimp.dgh.source.client.hotkey.KeyBinding;
import com.lastimp.dgh.network.message.MyKeyPressedData;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.register.ModItems;
import com.lastimp.dgh.source.register.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
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
        Common.sendToServer(MyHealingItemUseData.getInstance(
                healthScreen.getMenu().targetEntity, index, healthScreen.getSelectedComponent()
        ));

        ItemStack stack = slot.getItem();
        if (stack.is(ModTags.MEDICAL_TOOLS_SMALL_BAGS) && !stack.is(ModTags.MEDICAL_USAGE_BAGS)) {
            healthScreen.getMenu().openBag(stack);
        }
        if (stack.is(ModItems.AED.get())) {
            ClientAccessor.mc().getSoundManager().play(SimpleSoundInstance.forUI(ModSounds.AED.get(), 1.0f));
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
        Common.sendToServer(MyKeyPressedData.getInstance(KeyPressedType.KEY_SLOT_USE, index));
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
        if (itemStack.is(ModTags.MEDICAL_TOOLS_SMALL_BAGS)) return true;
        if (itemStack.is(ModItems.HEALTH_SCANNER.get())) return true;
        return false;
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (KeyBinding.OPEN_MENU_KEY.consumeClick()){
            Common.sendToServer(MyKeyPressedData.getInstance(KeyPressedType.KEY_HEALTH_MENU, 0));
        }
    }

    @SubscribeEvent
    public static void onMouseInput(InputEvent.MouseButton.Pre event) {
        ClientAccessor.getPlayer().ifPresent(player -> {
            if (HealthCapability.isDown(player)) {
                if (callForHelpTick <= 0 && event.getAction() == GLFW.GLFW_PRESS) {
                    Common.sendToServer(MyKeyPressedData.getInstance(KeyPressedType.CALL_FOR_HELP, 0));
                    callForHelpTick = 80;
                }
                if (ClientAccessor.mc().screen == null)
                    event.setCanceled(true);
            }
        });
    }

    @SubscribeEvent
    public static void onInputTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        ClientAccessor.getPlayer().ifPresent(player -> {
            if (!Config.enable_self_suicide) return;

            if (KeyBinding.GIVE_UP.isDown() && HealthCapability.isDown(player)) {
                giveUpTick++;
                if (giveUpTick >= 100) {
                    Common.sendToServer(MyKeyPressedData.getInstance(KeyPressedType.GIVE_UP, 0));
                }
            } else {
                giveUpTick = 0;
            }
        });
        callForHelpTick--;
    }

    @SubscribeEvent
    public static void onMovementInput(MovementInputUpdateEvent event) {
        if (HealthCapability.isDown(event.getEntity())) {
            event.getInput().jumping = false;
        }
    }

    @SubscribeEvent
    public static void onInteractWithLiving(PlayerInteractEvent.EntityInteractSpecific event) {
        var player = event.getEntity();
        var target = event.getTarget();
        if (!player.level().isClientSide()) return;
        if (!target.isAlive() || !(target instanceof LivingEntity livingEntity)) return;
        if (!HealthCapability.has(livingEntity)) return;
        if (!player.getItemInHand(InteractionHand.MAIN_HAND).is(ModItems.AED.get())) return;

        ClientAccessor.mc().getSoundManager().play(SimpleSoundInstance.forUI(ModSounds.AED.get(), 1.0f));
    }
}