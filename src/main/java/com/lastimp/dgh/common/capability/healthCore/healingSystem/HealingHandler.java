package com.lastimp.dgh.common.capability.healthCore.healingSystem;

import com.lastimp.dgh.common.capability.bodyPart.base.AbstractVisibleBody;
import com.lastimp.dgh.common.enums.BodyComponents;
import com.lastimp.dgh.common.item.bases.AbstractDirectHealItems;
import com.lastimp.dgh.common.item.bases.AbstractHealingItem;
import com.lastimp.dgh.common.item.bases.AbstractPartlyHealItem;
import com.lastimp.dgh.common.tags.ModTags;
import com.lastimp.dgh.common.network.message.MyHealingItemUseData;
import com.lastimp.dgh.common.utils.Utils;
import com.lastimp.dgh.common.capability.HealthCapability;
import com.lastimp.dgh.common.menu.BagMenu;
import com.lastimp.dgh.common.menu.HealthMenu;
import com.lastimp.dgh.common.item.medicine.Bandages;
import com.lastimp.dgh.common.item.medicine.Clamp;
import com.lastimp.dgh.common.item.medicine.Gypsum;
import com.lastimp.dgh.common.item.medicine.Tourniquet;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import org.jetbrains.annotations.NotNull;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.List;

import static com.lastimp.dgh.common.enums.BodyComponents.HEAD;
import static com.lastimp.dgh.common.capability.bodyPart.base.BodyCondition.*;

public class HealingHandler {
    public static HealingResult handleHealthMenuItemUse(HealthMenu healthMenu, final MyHealingItemUseData data, ServerPlayer sourcePlayer, LivingEntity target) {
        ItemStack stack = healthMenu.getStackBySlotNum(data.slotNum());
        HealingResult result = HealingResult.SUCCESS;
        if (stack.is(ModTags.MEDICAL_TOOLS_SMALL_BAGS) && !stack.is(ModTags.MEDICAL_USAGE_BAGS)) {
            healthMenu.openBag(stack);
        } else {
            BodyComponents component = data.component().equals("NONE") ? null : BodyComponents.valueOf(data.component());
            if (stack.is(ModTags.MEDICAL_USAGE_BAGS)) {
                result = BagHealerHandler.handleBagHealing(stack, sourcePlayer, target, component);
            } else {
                result = HealingHandler.useItemOn(stack, sourcePlayer, target, component);
            }
            healthMenu.getSlot(data.slotNum()).setByPlayer(stack);
        }
        return result;
    }

    public static HealingResult handleMedicineBagMenuItemUse(BagMenu.MedicineSmallBag bagMenu, final MyHealingItemUseData data, ServerPlayer sourcePlayer, LivingEntity target) {
        ItemStack stack = bagMenu.getStackBySlotNum(data.slotNum());
        HealingResult result = HealingResult.SUCCESS;
        if (stack.getItem() instanceof PotionItem potionItem) {
            Utils.drop(potionItem.finishUsingItem(stack, target.level(), target), sourcePlayer);
        } else if (stack.getItem() instanceof AbstractDirectHealItems){
            result = HealingHandler.useItemOn(stack, sourcePlayer, target, null);
        }
        bagMenu.getSlot(data.slotNum()).setByPlayer(stack);
        return result;
    }

    public static HealingResult useItemOn(ItemStack itemStack, @NotNull ServerPlayer source, LivingEntity target, BodyComponents component) {
        if (target == null) return HealingResult.NO_TARGET;
        if (source.getCooldowns().isOnCooldown(itemStack.getItem())) return HealingResult.IN_COOLDOWN;

        if (handleCut(itemStack, target, component) || handleWrite(itemStack, target, source)) {
            return HealingResult.SUCCESS;
        }

        if (!(itemStack.getItem() instanceof AbstractHealingItem healingItem)) return HealingResult.NOT_HEALING_ITEM;
        if (!healingItem.available(target, itemStack)) return HealingResult.ITEM_NOT_AVAILABLE;

        boolean success = false;
        if (healingItem instanceof AbstractDirectHealItems item) {
            success = item.heal(source, target);
        } else if (healingItem instanceof AbstractPartlyHealItem item) {
            success = item.heal(source, target, component);
        }

        if (success) {
            source.getCooldowns().addCooldown(healingItem, 10);
            HealthCapability.getAndApply(target, h -> h.setLastHealer(source.getUUID()));
            if (itemStack.isDamageableItem()) {
                itemStack.hurtAndBreak(1, source.serverLevel(), source, (i) -> {});
            } else {
                itemStack.consume(1, target);
            }
            return HealingResult.SUCCESS;
        }
        return HealingResult.ITEM_USING_FAILED;
    }

    private static boolean handleCut(ItemStack itemStack, LivingEntity target, BodyComponents component) {
        boolean success = false;
        if (ModTags.isShears(itemStack)) {
            success |= Bandages.cut(target, component);
            success |= Gypsum.cut(target, component);
            success |= Tourniquet.cut(target, component);
            success |= Clamp.cut(target, component);
        }
        return success;
    }

    private static boolean handleWrite(ItemStack itemStack, LivingEntity target, ServerPlayer source) {
        return HealthCapability.getAndApply(target, h -> {
            if (!itemStack.is(Items.WRITABLE_BOOK)) return false;
            ItemStack stack = new ItemStack(Items.WRITTEN_BOOK, 1);
            if (h.write(stack, Component.translatable(target.getName().getString()), Component.nullToEmpty(source.getScoreboardName()))) {
                Utils.drop(stack, source);
                itemStack.shrink(1);
                return true;
            }
            return false;
        }, false);
    }

    public static void handleValindaHealing(LivingEntity entity, float amount) {
        List<Pair<AbstractVisibleBody, ResourceLocation>> states = new ArrayList<>();
        HealthCapability.getAndApply(entity, h -> {
            float injury = 0;
            for (var component : BodyComponents.VISIBLE_BODIES) {
                AbstractVisibleBody body = (AbstractVisibleBody) h.getComponent(component);
                injury += injuryCheck(body, BURN, states);
                injury += injuryCheck(body, OPEN_WOUND, states);
                injury += injuryCheck(body, PASS_THROUGH, states);
                injury += injuryCheck(body, INTERNAL_INJURY, states);
                if (component == HEAD)
                    injury += injuryCheck(body, BRAIN_DAMAGE, states);
            }
            if (injury < 0.001) return;

            float healingDelta = amount / injury;
            for (var bodyAndCondition : states) {
                var body = bodyAndCondition.getA();
                var condition = bodyAndCondition.getB();
                body.healing(condition, -healingDelta * body.getConditionValue(condition));
                body.healingHidden(condition, -healingDelta * body.getConditionHidden(condition));
            }
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
