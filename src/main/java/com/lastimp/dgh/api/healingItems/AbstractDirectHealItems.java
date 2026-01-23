package com.lastimp.dgh.api.healingItems;

import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.core.healingSystem.HealingHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractDirectHealItems extends AbstractHealingItem{
    public AbstractDirectHealItems(Properties properties) {
        super(properties);
    }


    public boolean heal(@NotNull LivingEntity source, @NotNull LivingEntity entity, @Nullable BodyComponents component) {
        return this.heal(source, entity);
    }

    public abstract boolean heal(@NotNull LivingEntity source, @NotNull LivingEntity entity);

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (usedHand == InteractionHand.OFF_HAND)
            return InteractionResultHolder.pass(player.getItemInHand(usedHand));
        if (!level.isClientSide) {
            HealingHandler.useItemOn(player.getItemInHand(usedHand), (ServerPlayer) player, player, null);
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(usedHand), level.isClientSide);
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand) {
        if (usedHand == InteractionHand.OFF_HAND)
            return InteractionResult.PASS;
        if (!player.level().isClientSide && HealthCapability.has(interactionTarget)) {
            HealingHandler.useItemOn(player.getItemInHand(usedHand), (ServerPlayer) player, interactionTarget, null);
        }
        return InteractionResult.SUCCESS;
    }

}
