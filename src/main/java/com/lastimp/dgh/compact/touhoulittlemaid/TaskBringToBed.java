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
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class TaskBringToBed implements IMaidTask {
    public static final ResourceLocation ID = Common.ResourceLocation(DontGetHurt.MODID, "bring_to_bed");
    private StretcherEntity stretcher = null;

    @Override
    public ResourceLocation getUid() {
        return ID;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(ModItems.STRETCHER.get());
    }

    @Override
    public @Nullable SoundEvent getAmbientSound(EntityMaid entityMaid) {
        return null;
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid entityMaid) {
        MaidWalkToLivingEntityTask maidWalkToLivingEntityTask = this.createWalkToLivingEntityTask();
        return Lists.newArrayList(new Pair[]{
                Pair.of(5, maidWalkToLivingEntityTask),
        });
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createRideBrainTasks(EntityMaid maid) {
        return IMaidTask.super.createRideBrainTasks(maid);
    }

    private MaidWalkToLivingEntityTask createWalkToLivingEntityTask() {
        return new MaidWalkToLivingEntityTask(
                0.6f, 1.5f,
                (maid) -> (maid.getOwner() != null && HealthCapability.isDying(maid.getOwner()) && maid.getOwner().getVehicle() == null),
                (maid, livingEntity) -> livingEntity.is(maid.getOwner()),
                this::startCarry
        );
    }

    private void startCarry(EntityMaid maid, LivingEntity livingEntity) {
        if (!(livingEntity instanceof ServerPlayer player)) return;
        var inv = maid.getAvailableInv(true);
        for(int i = 0; i < inv.getSlots(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.is(ModItems.STRETCHER.get())) {
                StretcherItem item = (StretcherItem) stack.getItem();
                var result = item.interactLivingEntity(stack, maid, player);
                if (result == InteractionResult.SUCCESS) {
                    this.stretcher = (StretcherEntity) player.getVehicle();

                    ServerLevel targetLevel = player.server.getLevel(player.getRespawnDimension());
                    var location = Player.findRespawnPositionAndUseSpawnBlock(targetLevel, player.getRespawnPosition(), player.getRespawnAngle(), player.isRespawnForced(), true);
                    location.ifPresent((loc) -> {
                        EntityMaid newMaid = (EntityMaid) maid.changeDimension(targetLevel);
                        LivingEntity newOwner = (LivingEntity) maid.getOwner().changeDimension(targetLevel);
                        StretcherEntity newStretcher = (StretcherEntity) this.stretcher.changeDimension(targetLevel);
                        assert newMaid != null && newOwner != null && newStretcher != null;

                        newMaid.teleportTo(loc.x, loc.y, loc.z);
                        newStretcher.teleportTo(loc.x, loc.y, loc.z);
                        newOwner.startRiding(newStretcher, true);
                    });
                }
            }
        }
    }
}
