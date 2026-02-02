package com.lastimp.dgh.source.item.medicine;

import com.lastimp.dgh.api.healingItems.AbstractDirectHealItems;
import com.lastimp.dgh.source.core.bodyPart.Head;
import com.lastimp.dgh.source.core.bodyPart.Blood;
import com.lastimp.dgh.source.core.bodyPart.Torso;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.lastimp.dgh.source.core.bodyPart.base.BodyCondition.*;
import static com.lastimp.dgh.api.enums.BodyComponents.*;

public class Naloxone extends AbstractDirectHealItems {

    public Naloxone(Properties properties) {
        super(properties);
    }

    @Override
    public boolean heal(@NotNull LivingEntity source, @NotNull LivingEntity entity) {
        return HealthCapability.getAndApply(entity, h -> {
            Torso torso = (Torso) h.getComponent(TORSO);
            Head head = (Head) h.getComponent(HEAD);
            Blood blood = (Blood) h.getComponent(BLOOD);

            torso.healing(ANALGESIA, -0.6f);
            head.healing(WITHDRAW, -0.6f);
            blood.healing(OPIATE_ADDICTED, -0.6f);
            blood.healing(OPIATE_OVERDOSE, -0.6f);
            return true;
        }, false);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("治疗"));
        tooltipComponents.add(Component.literal("·戒断").withStyle(ChatFormatting.BLUE));
        tooltipComponents.add(Component.literal("·阿片成瘾").withStyle(ChatFormatting.BLUE));
        tooltipComponents.add(Component.literal("·阿片中毒").withStyle(ChatFormatting.BLUE));
        tooltipComponents.add(Component.literal("造成"));
        tooltipComponents.add(Component.literal("·消除麻醉").withStyle(ChatFormatting.RED));
    }
}
