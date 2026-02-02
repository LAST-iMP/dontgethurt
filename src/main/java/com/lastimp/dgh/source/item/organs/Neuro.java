package com.lastimp.dgh.source.item.organs;

import com.lastimp.dgh.source.core.bodyPart.base.AbstractBody;
import com.lastimp.dgh.source.core.bodyPart.base.AbstractOrgan;
import com.lastimp.dgh.api.tags.ModTags;
import com.lastimp.dgh.source.core.bodyPart.Head;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

import static com.lastimp.dgh.api.enums.BodyComponents.HEAD;

public class Neuro extends AbstractOrgan {
    public Neuro(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack update(ItemStack stack, HealthCapability health, AbstractBody body, LivingEntity entity) {
        Head head = (Head) health.getComponent(HEAD);
        if (head.countOrganMatch(ModTags.SPINAL_CORD) > 0)
            body.setOrgan1AdditionLevel(body.organ1AdditionLevel() + 2);
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("提供"));
        tooltipComponents.add(Component.literal("·四肢槽位+2").withStyle(ChatFormatting.BLUE));
    }
}
