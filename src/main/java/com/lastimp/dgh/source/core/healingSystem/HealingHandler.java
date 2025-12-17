package com.lastimp.dgh.source.core.healingSystem;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.healingItems.AbstractDirectHealItems;
import com.lastimp.dgh.api.healingItems.AbstractPartlyHealItem;
import com.lastimp.dgh.api.tags.ModTags;
import com.lastimp.dgh.network.message.MyHealingItemUseData;
import com.lastimp.dgh.source.client.gui.GuiOpenWrapper;
import com.lastimp.dgh.source.client.gui.component.DynamicSlotItemHandler;
import com.lastimp.dgh.source.client.gui.screen.HealthScreen;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.item.medicine.Bandages;
import com.lastimp.dgh.source.item.medicine.Gypsum;
import com.lastimp.dgh.source.item.medicine.Tourniquet;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.List;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.BRAIN_DAMAGE;
import static com.lastimp.dgh.api.enums.BodyComponents.HEAD;
import static com.lastimp.dgh.api.tags.ModTags.MEDICAL_TOOLS_SHEARS;

@EventBusSubscriber(modid = DontGetHurt.MODID)
public class HealingHandler {
    private static HealthScreen healthScreen = null;

    @SubscribeEvent
    public static void onScannerHealing(ScreenEvent.MouseButtonPressed.Pre event) {
        if (event.getButton() != 1) return;
        if (!screenHealingCheck()) return;

        assert healthScreen.getSlotUnderMouse() != null;
        var slot = healthScreen.getSlotUnderMouse();
        int index = slot.getSlotIndex();
        if (slot instanceof DynamicSlotItemHandler)
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
        if (itemStack.is(ModTags.MEDICINE)) return true;
        if (itemStack.is(ModTags.MEDICAL_TOOLS)) return true;
        if (itemStack.is(ModTags.MEDICAL_TOOLS_BAGS)) return true;

        return false;
    }

    public static void useItemOn(ItemStack itemStack, @NotNull ServerPlayer source, LivingEntity target, BodyComponents component) {
        if (target == null) return;
        if (source.getCooldowns().isOnCooldown(itemStack.getItem())) return;

        boolean success = false;
        if (itemStack.is(MEDICAL_TOOLS_SHEARS)) {
            success |= Bandages.cut(target, component);
            success |= Gypsum.cut(target, component);
            success |= Tourniquet.cut(target, component);
        }
        if (itemStack.getItem() instanceof AbstractDirectHealItems item) {
            success = item.heal(source, target);
        } else if (itemStack.getItem() instanceof AbstractPartlyHealItem item) {
            success = item.heal(source, target, component);
        }

        if (success)
            source.getCooldowns().addCooldown(itemStack.getItem(), 10);

        if (success && itemStack.isDamageableItem()) {
            itemStack.hurtAndBreak(1, source.serverLevel(), source, (i) -> {});
            if (itemStack.getDamageValue() >= itemStack.getMaxDamage())
                itemStack.consume(1, target);
        } else if (success) {
            itemStack.consume(1, target);
        }
    }

    public static void setHealthScreen(HealthScreen healthScreen) {
        HealingHandler.healthScreen = healthScreen;
    }

    public static void handleValindaHealing(ServerPlayer player, float amount) {
        List<Pair<AbstractVisibleBody, ResourceLocation>> states = new ArrayList<>();
        HealthCapability.getAndSet(player, h -> {
            float injury = 0;
            for (var component : BodyComponents.VISIBLE_BODIES) {
                AbstractVisibleBody body = (AbstractVisibleBody) h.getComponent(component);
                injury += injuryCheck(body, BURN, states);
                injury += injuryCheck(body, OPEN_WOUND, states);
                injury += injuryCheck(body, INTERNAL_INJURY, states) * 2;
                if (component == HEAD)
                    injury += injuryCheck(body, BRAIN_DAMAGE, states) * 3;
            }
            if (injury < 0.001) return h;

            float healingDelta = amount / injury;
            for (var bodyAndCondition : states) {
                var body = bodyAndCondition.getA();
                var condition = bodyAndCondition.getB();
                body.healing(condition, -healingDelta * body.getConditionValue(condition));
                body.healingHidden(condition, -healingDelta * body.getConditionHidden(condition));
            }
            return h;
        });
    }

    private static float injuryCheck(AbstractVisibleBody body, ResourceLocation key, List<Pair<AbstractVisibleBody, ResourceLocation>> states) {
        float result = 0;
        if (body.abnormalWithHidden(key)) {
            states.add(new Pair<>(body, key));
            result += body.getConditionHidden(key);
            result += body.getConditionValue(key);
        }
        return result;
    }
}
