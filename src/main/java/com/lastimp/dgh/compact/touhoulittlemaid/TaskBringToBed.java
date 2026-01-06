package com.lastimp.dgh.compact.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidWalkToLivingEntityTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.entity.StretcherEntity;
import com.lastimp.dgh.source.item.tool.StretcherItem;
import com.lastimp.dgh.source.register.ModItems;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class TaskBringToBed implements IMaidTask {
    @Override
    public ResourceLocation getUid() {
        return null;
    }

    @Override
    public ItemStack getIcon() {
        return null;
    }

    @Override
    public @Nullable SoundEvent getAmbientSound(EntityMaid entityMaid) {
        return null;
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid entityMaid) {
        MaidWalkToLivingEntityTask maidWalkToLivingEntityTask = this.createWalkTask();
        return Lists.newArrayList(new Pair[]{Pair.of(5, maidWalkToLivingEntityTask)});
    }
//
//    @Override
//    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createRideBrainTasks(EntityMaid maid) {
//        return IMaidTask.super.createRideBrainTasks(maid);
//    }
//
//    @Override
//    public List<Pair<String, Predicate<EntityMaid>>> getEnableConditionDesc(EntityMaid maid) {
//        return IMaidTask.super.getEnableConditionDesc(maid);
//    }
//
//    @Override
//    public MutableComponent getName() {
//        return IMaidTask.super.getName();
//    }
//
//    @Override
//    public List<Pair<String, Predicate<EntityMaid>>> getConditionDescription(EntityMaid maid) {
//        return IMaidTask.super.getConditionDescription(maid);
//    }
//
//    @Override
//    public List<String> getDescription(EntityMaid maid) {
//        return IMaidTask.super.getDescription(maid);
//    }
//
    private MaidWalkToLivingEntityTask createWalkTask() {
        return new MaidWalkToLivingEntityTask(
                0.6f, 1.5f,
                (maid) -> (HealthCapability.isDying(maid.getOwner())),
                (maid, livingEntity) -> livingEntity.is(maid.getOwner()),
                this::startCarry
        );
    }

    private void startCarry(EntityMaid maid, LivingEntity livingEntity) {
        var inv = maid.getAvailableInv(true);
        for(int i = 0; i < inv.getSlots(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.is(ModItems.STRETCHER)) {
                StretcherItem item = (StretcherItem) stack.getItem();
                var result = item.interactLivingEntity(stack, maid, livingEntity);
                if (result == InteractionResult.SUCCESS) {
                    StretcherEntity stretcher = (StretcherEntity) livingEntity.getVehicle();
                    stretcher.setController(maid);
                }
            }
        }
    }
}
