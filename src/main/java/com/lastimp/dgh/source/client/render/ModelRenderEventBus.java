package com.lastimp.dgh.source.client.render;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.client.ClientAccessor;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = DontGetHurt.MODID, value = Dist.CLIENT)
public class ModelRenderEventBus {
    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre<@NotNull AbstractClientPlayer> event) {
        var id = event.getRenderer().createRenderState().id;
        Player player = (Player) ClientAccessor.getLiving(id);
        if (player == null || !HealthCapability.has(player)) return;

        var humanoidModel = event.getRenderer().getModel();
        HealthCapability.getAndApply(player, health -> {
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
    public static void onRenderPlayerPost(RenderPlayerEvent.Pre<@NotNull AbstractClientPlayer> event) {
        var id = event.getRenderer().createRenderState().id;
        Player player = (Player) ClientAccessor.getLiving(id);
        if (player == null || !HealthCapability.has(player)) return;

        var humanoidModel = event.getRenderer().getModel();
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
