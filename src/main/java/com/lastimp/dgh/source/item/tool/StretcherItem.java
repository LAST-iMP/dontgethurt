package com.lastimp.dgh.source.item.tool;

import com.lastimp.dgh.api.healingItems.AbstractHealingItem;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.entity.StretcherEntity;
import com.lastimp.dgh.source.register.ModEntities;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class StretcherItem extends AbstractHealingItem {
    public StretcherItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (!player.level().isClientSide && hand == InteractionHand.MAIN_HAND && HealthCapability.isDying(target)) {
            StretcherEntity stretcher = new StretcherEntity(
                    ModEntities.STRETCHER.get(),
                    player.level()
            );
            stretcher.moveTo(
                    target.position().x,
                    target.position().y + 0.1,
                    target.position().z,
                    player.getYRot(),
                    0
            );
            target.startRiding(stretcher, true);
            player.level().addFreshEntity(stretcher);
            player.getItemInHand(hand).shrink(1);
        }
        return InteractionResult.SUCCESS;
    }
}
