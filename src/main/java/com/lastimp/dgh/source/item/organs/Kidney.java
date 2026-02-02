package com.lastimp.dgh.source.item.organs;

import com.lastimp.dgh.source.core.bodyPart.base.AbstractBody;
import com.lastimp.dgh.source.core.bodyPart.base.AbstractOrgan;
import com.lastimp.dgh.api.bodyPart.ConditionAccessor;
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
import net.minecraft.world.level.Level;

import java.util.List;

import static com.lastimp.dgh.DontGetHurt.DELTA;
import static com.lastimp.dgh.source.core.bodyPart.base.BodyCondition.BONE_DAMAGE;
import static com.lastimp.dgh.api.enums.BodyComponents.TORSO;

public class Kidney extends AbstractOrgan {
    public Kidney(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack update(ItemStack stack, HealthCapability health, AbstractBody body, LivingEntity entity) {
        Torso torso = (Torso) health.getComponent(TORSO);
        float num = torso.countOrganMatch(ModTags.KIDNEY);
        float factor = Utils.sqrtFactor(num / 2, 0.5f) / num;

        BodyComponents.VISIBLE_BODIES.stream().map(health::getComponent).forEach(visibleBody ->
                visibleBody.healing(BONE_DAMAGE, -ConditionAccessor.get(BONE_DAMAGE).healingSpeed() * DELTA * factor)
        );
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("提供"));
        tooltipComponents.add(Component.literal("·基础骨损伤恢复").withStyle(ChatFormatting.BLUE));
        tooltipComponents.add(Component.literal("全部失去时"));
        tooltipComponents.add(Component.literal("·持续骨损伤").withStyle(ChatFormatting.RED));
        tooltipComponents.add(Component.literal("·无法恢复脑损伤").withStyle(ChatFormatting.RED));
    }
}
