
package com.lastimp.dgh.source.item.medicine;

import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.healingItems.AbstractDirectHealItems;
import com.lastimp.dgh.source.core.Utils;
import com.lastimp.dgh.source.core.bodyPart.Blood;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.register.ModItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.BLOOD_LOSS;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.BLOOD_PRESSURE;

public class BloodPacksEmpty extends AbstractDirectHealItems {
    public BloodPacksEmpty(Properties properties) {
        super(properties);
    }

    @Override
    public boolean heal(@NotNull LivingEntity source, @NotNull LivingEntity entity) {
        return HealthCapability.getAndApply(entity, health -> {
            Blood blood = (Blood) health.getComponent(BodyComponents.BLOOD);
            float currCondition = blood.getConditionValue(BLOOD_LOSS);
            if (currCondition > BodyCondition.get(BLOOD_LOSS).maxValue() - 0.3f) return false;

            blood.injury(BLOOD_LOSS, 0.25f);
            blood.healing(BLOOD_PRESSURE, -0.25f);

            if (source instanceof Player player) {
                if (!player.getStringUUID().equals(entity.getStringUUID()))
                    player.attack(entity);
                Utils.drop(new ItemStack(ModItems.BLOOD_PACK.get()), player);
            }
            return true;
        }, false);
    }

    @Override
    protected BodyComponents getAvaComponent() {
        return BodyComponents.BLOOD;
    }
}
