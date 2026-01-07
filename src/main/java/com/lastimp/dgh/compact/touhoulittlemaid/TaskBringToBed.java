package com.lastimp.dgh.compact.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidArriveAtBlockTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidMoveToPredicateBlockTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidWalkToLivingEntityTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.neoforge.Common;
import com.lastimp.dgh.source.block.OperatingBedBlock;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.entity.StretcherEntity;
import com.lastimp.dgh.source.item.tool.StretcherItem;
import com.lastimp.dgh.source.register.ModItems;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TaskBringToBed implements IMaidTask {
    public static final ResourceLocation ID = Common.ResourceLocation(DontGetHurt.MODID, "bring_to_bed");
    private StretcherEntity stretcher = null;

    @Override
    public ResourceLocation getUid() {
        return ID;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(ModItems.OPERATING_BED_BLOCK_ITEM.get());
    }

    @Override
    public @Nullable SoundEvent getAmbientSound(EntityMaid entityMaid) {
        return null;
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid entityMaid) {
        MaidWalkToLivingEntityTask maidWalkToLivingEntityTask = this.createWalkToLivingEntityTask();
        MaidMoveToPredicateBlockTask maidMoveToPredicateBlockTask = this.createMoveToBlockTask();
        MaidArriveAtBlockTask maidArriveAtBlockTask = this.createArriveAtBlockTask();
        return Lists.newArrayList(new Pair[]{
                Pair.of(5, maidWalkToLivingEntityTask),
                Pair.of(6, maidMoveToPredicateBlockTask),
                Pair.of(7, maidArriveAtBlockTask)
        });
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createRideBrainTasks(EntityMaid maid) {
        return IMaidTask.super.createRideBrainTasks(maid);
    }

    private MaidWalkToLivingEntityTask createWalkToLivingEntityTask() {
        return new MaidWalkToLivingEntityTask(
                0.6f, 1.5f,
                (maid) -> (HealthCapability.isDying(maid.getOwner())),
                (maid, livingEntity) -> livingEntity.is(maid.getOwner()) && livingEntity.getVehicle() == null,
                this::startCarry
        );
    }

    private MaidMoveToPredicateBlockTask createMoveToBlockTask() {
        return new MaidMoveToPredicateBlockTask(
                1.0f, 50,
                (entity) -> this.stretcher != null,
                (entity, pos) -> entity.level().getBlockEntity(pos) instanceof OperatingBedBlock.Entity
        );
    }

    private MaidArriveAtBlockTask createArriveAtBlockTask() {
        return new MaidArriveAtBlockTask(
                1.5f,
                (maid, blockPos) -> {
                    if (this.stretcher != null) {
                        this.stretcher.setController(null);
                        this.stretcher = null;
                    }
                }
        );
    }

    private void startCarry(EntityMaid maid, LivingEntity livingEntity) {
        var inv = maid.getAvailableInv(true);
        for(int i = 0; i < inv.getSlots(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.is(ModItems.STRETCHER.get())) {
                StretcherItem item = (StretcherItem) stack.getItem();
                var result = item.interactLivingEntity(stack, maid, livingEntity);
                if (result == InteractionResult.SUCCESS) {
                    this.stretcher = (StretcherEntity) livingEntity.getVehicle();
                    this.stretcher.setController(maid);
                }
            }
        }
    }
}
