package com.lastimp.dgh.source.item.organs;

import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.bodyPart.AbstractOrgan;
import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.api.tags.ModTags;
import com.lastimp.dgh.source.core.Utils;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

import static com.lastimp.dgh.DontGetHurt.DELTA;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;

public class Skin extends AbstractOrgan {
    public Skin(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack update(ItemStack stack, HealthCapability health, AbstractBody body, LivingEntity entity) {
        AbstractVisibleBody visibleBody = (AbstractVisibleBody) body;
        int num = visibleBody.countOrganMatch(ModTags.SKIN);
        float factor = Utils.sqrtFactor(num, 1f) / num;

        handleWoundCondition(BURN, visibleBody, factor, num);
        handleWoundCondition(OPEN_WOUND, visibleBody, factor, num);
        handleWoundCondition(PASS_THROUGH, visibleBody, factor, num);
        return stack;
    }

    private void handleWoundCondition(ResourceLocation key, AbstractVisibleBody body, float factor, int num) {
        var condition = BodyCondition.get(key);
        if (body.abnormal(key) && body.getConditionValue(key) < condition.healingTS() * num * factor) {
            body.healing(key, -condition.healingSpeed() * DELTA * factor);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("提供"));
        tooltipComponents.add(Component.literal("·基础外伤恢复").withStyle(ChatFormatting.BLUE));
        tooltipComponents.add(Component.literal("全部失去时"));
        tooltipComponents.add(Component.literal("·加速感染").withStyle(ChatFormatting.RED));
    }
}
