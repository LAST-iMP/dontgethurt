package com.lastimp.dgh.source.entity;

import com.lastimp.dgh.source.core.Utils;
import com.lastimp.dgh.source.register.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class StretcherEntity extends Entity {
    public static final EntityType<StretcherEntity> TYPE;
    public static final float SIZE_HEIGHT = 0.25f;
    public static final float SIZE_LENGTH = 1.5f;
    private LivingEntity controller;


    public StretcherEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    /* ---------------- 基础 ---------------- */
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    /* ---------------- 尺寸 & 碰撞 ---------------- */

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.fixed(SIZE_LENGTH, SIZE_HEIGHT);
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return true;
    }

    /* ---------------- 乘客 ---------------- */

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return passenger instanceof LivingEntity && super.canAddPassenger(passenger);
    }

    @Nullable
    public LivingEntity getPatient() {
        if (this.getPassengers().isEmpty()) return null;
        if (this.getPassengers().get(0) instanceof LivingEntity le) return le;
        return null;
    }

    @Override
    public void tick() {
        super.tick();
        if (controller != null) {
            if (controller.isDeadOrDying()) {
                controller = null;
            } else {
                this.driveByPlayer();
            }
        }
        if (!this.onGround()) {
            this.addDeltaMovement(new Vec3(0, -0.08, 0));
        }
        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().scale(0.85));
    }

    private void driveByPlayer() {
        Vec3 playerPos = controller.getPosition(1);
        Vec3 entityPos = this.getPosition(1);
        double distance = playerPos.distanceTo(entityPos);
        Vec3 direction = entityPos.subtract(playerPos).normalize();

        double targetDistance = 2;
        float power = (float) Math.min(0.3, distance * 0.1);
        if (distance > targetDistance) {
            this.addDeltaMovement(direction.reverse().multiply(power, 0, power));
            if (playerPos.y() > entityPos.y() && distance > targetDistance * 1.5) {
                this.setPos(entityPos.x(), playerPos.y() + 0.1, entityPos.z());
            }
        }

        float yRot = -(float)(Math.atan2(-direction.x, direction.z) * 180 / Math.PI);
        this.setYRot(yRot);
        this.yRotO = this.getYRot();
        if (this.getPatient() != null) {
            LivingEntity patient = getPatient();
            patient.setYRot(-yRot);
            patient.yRotO = patient.getYRot();
            patient.setYBodyRot(-yRot);
            patient.yBodyRotO = patient.yBodyRot;
        }
    }

    public void setController(@Nullable LivingEntity livingEntity) {
        this.controller = livingEntity;
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (this.level().isClientSide) return InteractionResult.SUCCESS;

        if (player.isShiftKeyDown()) {
            this.setController(this.controller == null ? player : null);
        } else {
            LivingEntity patient = getPatient();
            if (patient != null) {
                patient.stopRiding();
                patient.moveTo(this.position().add(0, 0.5, 0));
            }
            Utils.drop(ModItems.STRETCHER.get(), player, 1);
            this.discard();
        }
        return InteractionResult.CONSUME;
    }

    static {
        TYPE = EntityType.Builder.of(StretcherEntity::new, MobCategory.MISC).noSummon().fireImmune().sized(SIZE_LENGTH, SIZE_HEIGHT).clientTrackingRange(10).updateInterval(1).setShouldReceiveVelocityUpdates(true).build("stretcher");
    }
}
