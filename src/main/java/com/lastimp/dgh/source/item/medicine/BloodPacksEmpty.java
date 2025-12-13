
package com.lastimp.dgh.source.item.medicine;

import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.healingItems.AbstractDirectHealItems;
import com.lastimp.dgh.source.register.ModItems;
import com.lastimp.dgh.source.core.bodyPart.Blood;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;

public class BloodPacksEmpty extends AbstractDirectHealItems {
    public BloodPacksEmpty(Properties properties) {
        super(properties);
    }

    @Override
    public boolean heal(@NotNull ServerPlayer source, @NotNull ServerPlayer target) {
        return HealthCapability.getAndSet(target, health -> {
            Blood blood = (Blood) health.getComponent(BodyComponents.BLOOD);
            float currCondition = blood.getConditionValue(BLOOD_LOSS);
            if (currCondition > BodyCondition.get(BLOOD_LOSS).maxValue() - 0.3f) return false;

            blood.injury(BLOOD_LOSS, 0.25f);
            if (!source.getStringUUID().equals(target.getStringUUID()))
                source.attack(target);

            ItemStack stack = new ItemStack(ModItems.BLOOD_PACK.get());
            if (!source.addItem(stack)) {
                source.drop(stack, true, true);
            }
            return true;
        });
    }

    @Override
    protected BodyComponents getAvaComponent() {
        return BodyComponents.BLOOD;
    }
}
