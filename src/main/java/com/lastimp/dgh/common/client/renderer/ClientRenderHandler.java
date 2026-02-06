package com.lastimp.dgh.common.client.renderer;

import com.lastimp.dgh.common.capability.HealthCapability;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;

public class ClientRenderHandler {
    public static void onRenderPlayer(LivingEntity livingEntity, LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>> renderer) {
        if (!HealthCapability.has(livingEntity)) return;
        if (!(renderer.getModel() instanceof PlayerModel<?> humanoidModel)) return;

        HealthCapability.getAndApply(livingEntity, health -> {
            // 左臂
            humanoidModel.leftArm.visible = health.leftArmVisible();
            humanoidModel.leftSleeve.visible = humanoidModel.leftArm.visible;
            // 右臂
            humanoidModel.rightArm.visible = health.rightArmVisible();
            humanoidModel.rightSleeve.visible = humanoidModel.rightArm.visible;
            // 左腿
            humanoidModel.leftLeg.visible = health.leftLegVisible();
            humanoidModel.leftPants.visible = humanoidModel.leftLeg.visible;
            // 右腿
            humanoidModel.rightLeg.visible = health.rightLegVisible();
            humanoidModel.rightPants.visible = humanoidModel.rightLeg.visible;
        });
    }

    public static void onRenderPlayerPost(LivingEntity livingEntity, LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>> renderer) {
        if (!HealthCapability.has(livingEntity)) return;
        if (!(renderer.getModel() instanceof PlayerModel<?> humanoidModel)) return;

        humanoidModel.leftArm.visible = true;
        humanoidModel.rightArm.visible = true;
        humanoidModel.leftLeg.visible = true;
        humanoidModel.rightLeg.visible = true;
        humanoidModel.leftSleeve.visible = true;
        humanoidModel.rightSleeve.visible = true;
        humanoidModel.leftPants.visible = true;
        humanoidModel.rightPants.visible = true;
    }
}
