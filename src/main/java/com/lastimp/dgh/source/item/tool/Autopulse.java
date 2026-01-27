package com.lastimp.dgh.source.item.tool;

import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.healingItems.AbstractHealingEquipment;
import com.lastimp.dgh.source.core.bodyPart.Torso;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import net.minecraft.world.level.Level;

import java.util.List;

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
    public boolean available(LivingEntity target, ItemStack stack) {
        if (stack.getDamageValue() >= stack.getMaxDamage() - 1) {
            return false;
        }
        return super.available(target, stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("治疗"));
        tooltipComponents.add(Component.literal("·心率上升").withStyle(ChatFormatting.BLUE));
        tooltipComponents.add(Component.literal("·低血氧").withStyle(ChatFormatting.BLUE));
        tooltipComponents.add(Component.literal("造成"));
        tooltipComponents.add(Component.literal("·血压上升").withStyle(ChatFormatting.RED));
    }
}
