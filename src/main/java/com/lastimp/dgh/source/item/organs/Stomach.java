package com.lastimp.dgh.source.item.organs;

import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.bodyPart.AbstractOrgan;
import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.tags.ModTags;
import com.lastimp.dgh.source.core.Utils;
import com.lastimp.dgh.source.core.bodyPart.Torso;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;
import static com.lastimp.dgh.api.enums.BodyComponents.TORSO;

public class Stomach extends AbstractOrgan {
    public Stomach(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack update(ItemStack stack, HealthCapability health, AbstractBody body, LivingEntity entity) {
        Torso torso = (Torso) health.getComponent(TORSO);
        int num = torso.countOrganMatch(ModTags.STOMACH);
        float factor = Utils.sqrtFactor(num, 1f) / num;

        BodyComponents.VISIBLE_BODIES.stream().map(health::getComponent).forEach(visibleBody -> {
            if (visibleBody.abnormalWithHidden(BURN))
                ((AbstractVisibleBody) visibleBody).handleFoodAcc(entity, BURN, factor, 1.5f);
            if (visibleBody.abnormalWithHidden(OPEN_WOUND))
                ((AbstractVisibleBody) visibleBody).handleFoodAcc(entity, OPEN_WOUND, factor, 1.5f);
            if (visibleBody.abnormalWithHidden(PASS_THROUGH))
                ((AbstractVisibleBody) visibleBody).handleFoodAcc(entity, PASS_THROUGH, factor, 3);
            if (visibleBody.abnormalWithHidden(INTERNAL_INJURY))
                ((AbstractVisibleBody) visibleBody).handleFoodAcc(entity, INTERNAL_INJURY, factor, 1);
        });
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("提供"));
        tooltipComponents.add(Component.literal("·基础饱食伤口恢复").withStyle(ChatFormatting.BLUE));
        tooltipComponents.add(Component.literal("全部失去时"));
        tooltipComponents.add(Component.literal("·无法进食").withStyle(ChatFormatting.RED));
    }
}
