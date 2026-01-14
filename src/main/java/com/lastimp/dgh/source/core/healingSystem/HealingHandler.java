package com.lastimp.dgh.source.core.healingSystem;

import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.healingItems.AbstractDirectHealItems;
import com.lastimp.dgh.api.healingItems.AbstractHealingItem;
import com.lastimp.dgh.api.healingItems.AbstractPartlyHealItem;
import com.lastimp.dgh.api.tags.ModTags;
import com.lastimp.dgh.source.core.Utils;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.item.medicine.Bandages;
import com.lastimp.dgh.source.item.medicine.Gypsum;
import com.lastimp.dgh.source.item.medicine.Tourniquet;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.List;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.BRAIN_DAMAGE;
import static com.lastimp.dgh.api.enums.BodyComponents.HEAD;
import static com.lastimp.dgh.api.tags.ModTags.MEDICAL_TOOLS_SHEARS;

public class HealingHandler {

    public static void useItemOn(ItemStack itemStack, @NotNull ServerPlayer source, LivingEntity target, BodyComponents component) {
        if (target == null) return;
        if (source.getCooldowns().isOnCooldown(itemStack.getItem())) return;

        boolean success = handleCut(itemStack, target, component);
        success |= handleWrite(itemStack, target, source);

        if (!(itemStack.getItem() instanceof AbstractHealingItem healingItem)) return;
        if (!healingItem.available(target, itemStack)) return;

        if (healingItem instanceof AbstractDirectHealItems item) {
            success = item.heal(source, target);
        } else if (healingItem instanceof AbstractPartlyHealItem item) {
            success = item.heal(source, target, component);
        }

        if (success) {
            source.getCooldowns().addCooldown(healingItem, 10);
            HealthCapability.getAndApply(target, h -> h.setLastHealer(source.getUUID()));
        }

        if (success && itemStack.isDamageableItem()) {
            itemStack.hurtAndBreak(1, source.serverLevel(), source, (i) -> {});
            if (itemStack.getDamageValue() >= itemStack.getMaxDamage())
                itemStack.consume(1, target);
        } else if (success) {
            itemStack.consume(1, target);
        }
    }

    private static boolean handleCut(ItemStack itemStack, LivingEntity target, BodyComponents component) {
        boolean success = false;
        if (ModTags.isShears(itemStack)) {
            success |= Bandages.cut(target, component);
            success |= Gypsum.cut(target, component);
            success |= Tourniquet.cut(target, component);
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
                injury += injuryCheck(body, INTERNAL_INJURY, states) * 2;
                if (component == HEAD)
                    injury += injuryCheck(body, BRAIN_DAMAGE, states) * 3;
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
