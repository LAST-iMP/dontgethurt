package com.lastimp.dgh.common.item.organs;

import com.lastimp.dgh.common.capability.bodyPart.ConditionAccessor;
import com.lastimp.dgh.common.capability.bodyPart.base.AbstractBody;
import com.lastimp.dgh.common.capability.bodyPart.base.AbstractOrgan;
import com.lastimp.dgh.common.capability.bodyPart.base.AbstractVisibleBody;
import com.lastimp.dgh.common.enums.BodyComponents;
import com.lastimp.dgh.common.tags.ModTags;
import com.lastimp.dgh.common.utils.Utils;
import com.lastimp.dgh.common.capability.bodyPart.bodies.Blood;
import com.lastimp.dgh.common.capability.bodyPart.bodies.Torso;
import com.lastimp.dgh.common.capability.HealthCapability;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

import static com.lastimp.dgh.common.enums.BodyComponents.*;
import static com.lastimp.dgh.common.capability.bodyPart.base.BodyCondition.*;

public class Liver extends AbstractOrgan {
    public Liver(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack update(ItemStack stack, HealthCapability health, AbstractBody body, LivingEntity entity) {
        Torso torso = (Torso) health.getComponent(TORSO);
        Blood blood = (Blood) health.getComponent(BLOOD);
        int num = torso.countOrganMatch(ModTags.LIVER);
        float factor = Utils.sqrtFactor(num, 0.5f) / num;

        if (blood.sepsis() <= Utils.EPS) {
            blood.healing(SEPSIS, -ConditionAccessor.get(SEPSIS).healingSpeed() * Utils.DELTA * factor);
        }

        BodyComponents.VISIBLE_BODIES.stream().map(health::getComponent).forEach(visibleBody ->
                ((AbstractVisibleBody) visibleBody).cureInfection(factor)
        );
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("提供"));
        tooltipComponents.add(Component.literal("·基础败血症恢复").withStyle(ChatFormatting.BLUE));
        tooltipComponents.add(Component.literal("·基础感染恢复").withStyle(ChatFormatting.BLUE));
    }
}
