package com.lastimp.dgh.common.item.organs;

import com.lastimp.dgh.common.capability.bodyPart.base.AbstractOrgan;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class Eye extends AbstractOrgan {
    public Eye(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("提供"));
        tooltipComponents.add(Component.literal("·基础视觉").withStyle(ChatFormatting.BLUE));
    }
}
