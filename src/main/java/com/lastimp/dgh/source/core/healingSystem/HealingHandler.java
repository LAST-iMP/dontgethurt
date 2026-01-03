
package com.lastimp.dgh.source.core.healingSystem;

import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import com.lastimp.dgh.api.healingItems.AbstractDirectHealItems;
import com.lastimp.dgh.api.healingItems.AbstractHealingItem;
import com.lastimp.dgh.api.healingItems.AbstractPartlyHealItem;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.tags.ModTags;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.item.medicine.Bandages;
import com.lastimp.dgh.source.item.medicine.Gypsum;
import com.lastimp.dgh.source.item.medicine.Tourniquet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.BRAIN_DAMAGE;
import static com.lastimp.dgh.api.enums.BodyComponents.HEAD;

public class HealingHandler {

    public static void useItemOn(ItemStack itemStack, @NotNull ServerPlayer source, LivingEntity target, BodyComponents component) {
        if (target == null) return;
        if (source.getCooldowns().isOnCooldown(itemStack.getItem())) return;

        boolean success = false;
        if (ModTags.isShears(itemStack)) {
            success |= Bandages.cut(target, component);
            success |= Gypsum.cut(target, component);
            success |= Tourniquet.cut(target, component);
        }

        if (!(itemStack.getItem() instanceof AbstractHealingItem healingItem)) return;
        if (!healingItem.available(target, itemStack)) return;

        if (healingItem instanceof AbstractDirectHealItems item) {
            success = item.heal(source, target);
        } else if (healingItem instanceof AbstractPartlyHealItem item) {
            success = item.heal(source, target, component);
        }

        if (success) {
            source.getCooldowns().addCooldown(healingItem, 10);
            HealthCapability.getAndSet(target, h -> {
                h.setLastHealer(source.getUUID());
                return true;
            });
        }

        if (success && itemStack.isDamageableItem()) {
            itemStack.hurtAndBreak(1, source, (player) -> {});
            if (itemStack.getDamageValue() >= itemStack.getMaxDamage())
                itemStack.shrink(1);
        } else if (success) {
            itemStack.shrink(1);
        }
    }

    public static void handleValindaHealing(LivingEntity entity, float amount) {
        List<Pair<AbstractVisibleBody, ResourceLocation>> states = new ArrayList<>();
        HealthCapability.getAndSet(entity, h -> {
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
