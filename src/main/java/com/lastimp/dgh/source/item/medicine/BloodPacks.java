package com.lastimp.dgh.source.item.medicine;

import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.healingItems.AbstractDirectHealItems;
import com.lastimp.dgh.source.core.bodyPart.PlayerBlood;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.register.ModItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.BLOOD_LOSS;

public class BloodPacks extends AbstractDirectHealItems {

    public BloodPacks(Properties properties) {
        super(properties);
    }

    @Override
    public boolean heal(@NotNull ServerPlayer source, @NotNull ServerPlayer target) {
        return HealthCapability.getAndSet(target, health -> {
            PlayerBlood blood = (PlayerBlood) health.getComponent(BodyComponents.BLOOD);
            if (!blood.abnormal(BLOOD_LOSS)) return false;

            blood.healing(BLOOD_LOSS, -0.25f);

            ItemStack stack = new ItemStack(ModItems.BLOOD_PACK_EMPTY.get());
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
