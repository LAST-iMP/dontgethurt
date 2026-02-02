package com.lastimp.dgh.source.item.tool;

import com.lastimp.dgh.api.bodyPart.ConditionAccessor;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.healingItems.AbstractHealingEquipment;
import com.lastimp.dgh.source.core.bodyPart.Torso;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.lastimp.dgh.source.core.bodyPart.base.BodyCondition.RESPIRATORY_ARREST;

public class StasisBag extends AbstractHealingEquipment {
    public StasisBag(Properties properties) {
        super(properties);
    }

    @Override
    public boolean heal(@NotNull LivingEntity entity) {
        return HealthCapability.getAndApply(entity, h -> {
            Torso torso = (Torso) h.getComponent(BodyComponents.TORSO);
            torso.addHeartRate(3);
            torso.injury(RESPIRATORY_ARREST, ConditionAccessor.get(RESPIRATORY_ARREST).maxValue());
            entity.setAirSupply(entity.getMaxAirSupply());
            return true;
        }, false);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        return InteractionResultHolder.pass(player.getItemInHand(usedHand));
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand) {
        return InteractionResult.PASS;
    }

    @Override
    public int getMaxCooldown() {
        return 20;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("治疗"));
        tooltipComponents.add(Component.literal("·症状停滞").withStyle(ChatFormatting.BLUE));
        tooltipComponents.add(Component.literal("造成"));
        tooltipComponents.add(Component.literal("·心跳停止").withStyle(ChatFormatting.RED));
        tooltipComponents.add(Component.literal("·呼吸暂停").withStyle(ChatFormatting.RED));
        tooltipComponents.add(Component.literal("·无法治疗").withStyle(ChatFormatting.RED));
    }
}
