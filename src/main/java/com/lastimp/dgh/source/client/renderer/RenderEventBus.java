package com.lastimp.dgh.source.client.renderer;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLivingEvent;

@EventBusSubscriber(modid = DontGetHurt.MODID, value = Dist.CLIENT)
public class RenderEventBus {
    @SubscribeEvent
    public static void onRenderPlayer(RenderLivingEvent.Pre<LivingEntity, EntityModel<LivingEntity>> event) {
        LivingEntity entity = event.getEntity();
        if (!HealthCapability.has(entity)) return;
        if (!(event.getRenderer().getModel() instanceof PlayerModel<?> humanoidModel)) return;

        HealthCapability.getAndApply(entity, health -> {
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

    @SubscribeEvent
    public static void onRenderPlayerPost(RenderLivingEvent.Post<LivingEntity, EntityModel<LivingEntity>> event) {
        LivingEntity entity = event.getEntity();
        if (!HealthCapability.has(entity)) return;
        if (!(event.getRenderer().getModel() instanceof PlayerModel<?> humanoidModel)) return;

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
