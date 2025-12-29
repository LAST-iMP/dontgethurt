package com.lastimp.dgh.source.client;

import com.lastimp.dgh.source.client.gui.screen.HealthScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.UUID;

@OnlyIn(value = Dist.CLIENT)
public abstract class ClientAccessor {
    private static HealthScreen healthScreen = null;

    public static RegistryAccess registryAccess() {
        return Minecraft.getInstance().player.registryAccess();
    }

    public static boolean canRenderGui() {
        Minecraft mc = Minecraft.getInstance();
        return !(mc.level == null || mc.player == null || mc.options.hideGui);
    }

    public static ClientLevel getLevel() {
        return Minecraft.getInstance().level;
    }

    public static AbstractClientPlayer getPlayer() {
        return Minecraft.getInstance().player;
    }

    public static LivingEntity getLiving(ClientLevel level, UUID uuid, Vec3 center, int range) {
        var result = level.getEntitiesOfClass(
                LivingEntity.class, AABB.ofSize(center, range, range, range),
                (entity) -> entity.getUUID().equals(uuid)
        );
        if (!result.isEmpty())
            return result.getFirst();
        return null;
    }

    public static LivingEntity getLiving(int id) {
        var result = getLevel().getEntity(id);
        if (result instanceof LivingEntity livingEntity) {
            return livingEntity;
        }
        return null;
    }

    public static HealthScreen healthScreen() {
        return healthScreen;
    }

    public static void setHealthScreen(HealthScreen healthScreen) {
        ClientAccessor.healthScreen = healthScreen;
    }
}
