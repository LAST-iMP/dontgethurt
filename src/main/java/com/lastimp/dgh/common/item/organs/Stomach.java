package com.lastimp.dgh.common.item.organs;

import com.lastimp.dgh.common.PlatformService;
import com.lastimp.dgh.common.capability.bodyPart.base.AbstractBody;
import com.lastimp.dgh.common.capability.bodyPart.base.AbstractOrgan;
import com.lastimp.dgh.common.capability.bodyPart.base.AbstractVisibleBody;
import com.lastimp.dgh.common.enums.BodyComponents;
import com.lastimp.dgh.common.tags.ModTags;
import com.lastimp.dgh.common.utils.Utils;
import com.lastimp.dgh.common.capability.bodyPart.bodies.Torso;
import com.lastimp.dgh.common.capability.HealthCapability;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

import static com.lastimp.dgh.common.capability.bodyPart.base.BodyCondition.*;
import static com.lastimp.dgh.common.enums.BodyComponents.*;

public class Stomach extends AbstractOrgan {
    public Stomach(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack update(ItemStack stack, HealthCapability health, AbstractBody body, LivingEntity entity) {
        Torso torso = (Torso) health.getComponent(TORSO);
        int num = torso.countOrganMatch(ModTags.STOMACH);
        float factor = Utils.sqrtFactor(num, 1f) / num * PlatformService.CONFIG.INTERNAL_FOOD_HEALING();

        BodyComponents.VISIBLE_BODIES.stream().map(health::getComponent).forEach(visibleBody -> {
            if (visibleBody.abnormalWithHidden(BURN))
                ((AbstractVisibleBody) visibleBody).handleFoodAcc(entity, BURN, factor, 1.5f * Utils.DELTA);
            if (visibleBody.abnormalWithHidden(OPEN_WOUND))
                ((AbstractVisibleBody) visibleBody).handleFoodAcc(entity, OPEN_WOUND, factor, 1.5f * Utils.DELTA);
            if (visibleBody.abnormalWithHidden(PASS_THROUGH))
                ((AbstractVisibleBody) visibleBody).handleFoodAcc(entity, PASS_THROUGH, factor, 3 * Utils.DELTA);
            if (visibleBody.abnormalWithHidden(INTERNAL_INJURY))
                ((AbstractVisibleBody) visibleBody).handleFoodAcc(entity, INTERNAL_INJURY, factor, 1 * Utils.DELTA);
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
