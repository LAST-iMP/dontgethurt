package com.lastimp.dgh.source.item.tool;

import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.healingItems.AbstractDirectHealItems;
import com.lastimp.dgh.source.core.Utils;
import com.lastimp.dgh.source.core.bodyPart.Torso;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.BURN;

public class AED extends AbstractDirectHealItems {
    public AED(Properties properties) {
        super(properties);
    }

    @Override
    public boolean heal(@NotNull LivingEntity source, @NotNull LivingEntity entity) {
        return HealthCapability.getAndApply(entity, h -> {
            Torso torso = (Torso) h.getComponent(BodyComponents.TORSO);
            torso.injury(BURN, 0.1f);
            torso.addHeartRate(0.3f);
            float currentHeartRate = torso.getHeartRateLevel();
            torso.addHeartRate(Utils.randomBetween(-0.75f, 0.2f) * currentHeartRate);
            return true;
        },false);
    }

    @Override
    public boolean available(LivingEntity target, ItemStack stack) {
        if (stack.getDamageValue() >= stack.getMaxDamage() - 1) {
            return false;
        }
        return super.available(target, stack);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        return InteractionResultHolder.pass(player.getItemInHand(usedHand));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("治疗"));
        tooltipComponents.add(Component.literal("·严重心脏骤停").withStyle(ChatFormatting.BLUE));
        tooltipComponents.add(Component.literal("造成"));
        tooltipComponents.add(Component.literal("·心率上升").withStyle(ChatFormatting.RED));
        tooltipComponents.add(Component.literal("·胸部烧伤").withStyle(ChatFormatting.RED));
    }
}
