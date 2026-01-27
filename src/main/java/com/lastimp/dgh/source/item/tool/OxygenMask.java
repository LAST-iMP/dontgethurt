package com.lastimp.dgh.source.item.tool;

import com.lastimp.dgh.api.healingItems.AbstractHealingEquipment;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OxygenMask extends AbstractHealingEquipment {
    public OxygenMask(Properties properties) {
        super(properties);
    }

    @Override
    public boolean heal(@NotNull LivingEntity entity) {
        return HealthCapability.getAndApply(entity, health -> {
            if (entity.getAirSupply() == entity.getMaxAirSupply()) return false;
            entity.setAirSupply(entity.getMaxAirSupply());
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
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("治疗"));
        tooltipComponents.add(Component.literal("·呼吸停止").withStyle(ChatFormatting.BLUE));
    }
}
