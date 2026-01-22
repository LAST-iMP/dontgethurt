package com.lastimp.dgh.source.item.tool;

import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.healingItems.AbstractHealingEquipment;
import com.lastimp.dgh.source.core.bodyPart.Torso;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.BLOOD_PRESSURE;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.OXYGEN;

public class Autopulse extends AbstractHealingEquipment {
    public Autopulse(Properties properties) {
        super(properties);
    }

    @Override
    public boolean heal(@NotNull LivingEntity entity) {
        return HealthCapability.getAndApply(entity, h -> {
            var blood = h.getComponent(BodyComponents.BLOOD);
            Torso torso = (Torso) h.getComponent(BodyComponents.TORSO);
            blood.healing(OXYGEN, -BodyCondition.get(OXYGEN).healingSpeed() * 2);
            blood.healing(BLOOD_PRESSURE, 0.05f);
            torso.addHeartRate(-0.08f);
            return true;
        }, false);
    }

    @Override
    public int getMaxCooldown() {
        return 20;
    }

    @Override
    protected BodyComponents getAvaComponent() {
        return BodyComponents.TORSO;
    }

    @Override
    public boolean available(LivingEntity target, ItemStack stack) {
        if (stack.getDamageValue() >= stack.getMaxDamage() - 1) {
            return false;
        }
        return super.available(target, stack);
    }
}
