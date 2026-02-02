package com.lastimp.dgh.source.item.medicine;

import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.healingItems.AbstractDirectHealItems;
import com.lastimp.dgh.source.core.Utils;
import com.lastimp.dgh.source.core.bodyPart.Blood;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.register.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import net.minecraft.world.level.Level;

import java.util.List;

import static com.lastimp.dgh.source.core.bodyPart.base.BodyCondition.BLOOD_LOSS;
import static com.lastimp.dgh.source.core.bodyPart.base.BodyCondition.BLOOD_PRESSURE;

public class BloodPacks extends AbstractDirectHealItems {

    public BloodPacks(Properties properties) {
        super(properties);
    }

    @Override
    public boolean heal(@NotNull LivingEntity source, @NotNull LivingEntity entity) {
        return HealthCapability.getAndApply(entity, health -> {
            Blood blood = (Blood) health.getComponent(BodyComponents.BLOOD);
            if (!blood.abnormal(BLOOD_LOSS) && !blood.abnormal(BLOOD_PRESSURE)) return false;

            blood.healing(BLOOD_LOSS, -0.25f);
            blood.healing(BLOOD_PRESSURE, 0.25f);

            if (source instanceof Player player) {
                Utils.drop(new ItemStack(ModItems.BLOOD_PACK_EMPTY.get()), player);
            }
            return true;
        }, false);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("治疗"));
        tooltipComponents.add(Component.literal("·失血").withStyle(ChatFormatting.BLUE));
        tooltipComponents.add(Component.literal("造成"));
        tooltipComponents.add(Component.literal("·高血压").withStyle(ChatFormatting.RED));
    }
}
