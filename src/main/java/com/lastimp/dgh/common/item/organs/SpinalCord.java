package com.lastimp.dgh.common.item.organs;

import com.lastimp.dgh.common.capability.bodyPart.base.AbstractBody;
import com.lastimp.dgh.common.capability.bodyPart.base.AbstractOrgan;
import com.lastimp.dgh.common.capability.bodyPart.bodies.Head;
import com.lastimp.dgh.common.capability.bodyPart.bodies.Torso;
import com.lastimp.dgh.common.capability.HealthCapability;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

import static com.lastimp.dgh.common.enums.BodyComponents.HEAD;
import static com.lastimp.dgh.common.enums.BodyComponents.TORSO;

public class SpinalCord extends AbstractOrgan {
    public SpinalCord(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack update(ItemStack stack, HealthCapability health, AbstractBody body, LivingEntity entity) {
        Torso torso = (Torso) health.getComponent(TORSO);
        Head head = (Head) health.getComponent(HEAD);

        torso.setOrgan1AdditionLevel(torso.organ1AdditionLevel() + 2);
        head.setOrgan1AdditionLevel(head.organ1AdditionLevel() + 2);
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("提供"));
        tooltipComponents.add(Component.literal("·头部胸部槽位+2").withStyle(ChatFormatting.BLUE));
    }
}
