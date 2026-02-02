package com.lastimp.dgh.source.item.organs;

import com.lastimp.dgh.api.bodyPart.ConditionAccessor;
import com.lastimp.dgh.source.core.bodyPart.base.AbstractBody;
import com.lastimp.dgh.source.core.bodyPart.base.AbstractOrgan;
import com.lastimp.dgh.source.core.bodyPart.base.AbstractVisibleBody;
import com.lastimp.dgh.api.enums.BodyComponents;
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
import static com.lastimp.dgh.DontGetHurt.EPS;
import static com.lastimp.dgh.source.core.bodyPart.base.BodyCondition.*;
import static com.lastimp.dgh.api.enums.BodyComponents.BLOOD;
import static com.lastimp.dgh.api.enums.BodyComponents.TORSO;

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

        if (blood.sepsis() <= EPS) {
            blood.healing(SEPSIS, -ConditionAccessor.get(SEPSIS).healingSpeed() * DELTA * factor);
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
