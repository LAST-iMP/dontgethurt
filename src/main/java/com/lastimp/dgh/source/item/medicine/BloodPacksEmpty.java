
package com.lastimp.dgh.source.item.medicine;

import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.healingItems.AbstractDirectHealItems;
import com.lastimp.dgh.source.register.ModItems;
import com.lastimp.dgh.source.core.bodyPart.PlayerBlood;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import static com.lastimp.dgh.api.enums.BodyCondition.BLOOD_LOSS;

public class BloodPacksEmpty extends AbstractDirectHealItems {
    public BloodPacksEmpty(Properties properties) {
        super(properties);
    }

    @Override
    public boolean heal(@NotNull ServerPlayer source, @NotNull ServerPlayer target) {
        return PlayerHealthCapability.getAndSet(target, health -> {
            PlayerBlood blood = (PlayerBlood) health.getComponent(BodyComponents.BLOOD);
            float currCondition = blood.getConditionValue(BLOOD_LOSS);
            if (currCondition > BLOOD_LOSS.maxValue - 0.3f) return false;

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
