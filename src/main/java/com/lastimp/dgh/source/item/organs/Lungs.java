package com.lastimp.dgh.source.item.organs;

import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.bodyPart.AbstractOrgan;
import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.api.tags.ModTags;
import com.lastimp.dgh.source.core.Utils;
import com.lastimp.dgh.source.core.bodyPart.Blood;
import com.lastimp.dgh.source.core.bodyPart.Torso;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

import static com.lastimp.dgh.DontGetHurt.DELTA;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.OXYGEN;
import static com.lastimp.dgh.api.enums.BodyComponents.BLOOD;
import static com.lastimp.dgh.api.enums.BodyComponents.TORSO;

public class Lungs extends AbstractOrgan {
    public Lungs(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack update(ItemStack stack, HealthCapability health, AbstractBody body, LivingEntity entity) {
        Torso torso = (Torso) health.getComponent(TORSO);
        Blood blood = (Blood) health.getComponent(BLOOD);

        torso.setAdditionAir(torso.additionAir() + 150);

        int num = torso.countOrganMatch(ModTags.LUNGS);
        float factor = Utils.sqrtFactor(num, 1f) / num;

        if (blood.abnormal(OXYGEN) && !blood.oxygenLost() && !torso.abnormal(RESPIRATORY_ARREST) && torso.getConditionValue(PNEUMOTHORAX) < 0.1 && entity.getAirSupply() >= 2) {
            blood.healing(OXYGEN, -BodyCondition.get(OXYGEN).healingSpeed() * DELTA * factor * health.bloodOxygenFactor());
            entity.setAirSupply(entity.getAirSupply() - 1);
        }
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("提供"));
        tooltipComponents.add(Component.literal("·基础血氧恢复").withStyle(ChatFormatting.BLUE));
        tooltipComponents.add(Component.literal("·氧气上限").withStyle(ChatFormatting.BLUE));
        tooltipComponents.add(Component.literal("全部失去时"));
        tooltipComponents.add(Component.literal("·呼吸停止").withStyle(ChatFormatting.RED));
    }
}
