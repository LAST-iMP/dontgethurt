package com.lastimp.dgh.source.client;

import com.lastimp.dgh.source.client.gui.screen.HealthScreen;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
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
    private static HealthCapability health = null;
    private static boolean showingScreen = false;

    public static RegistryAccess registryAccess() {
        return Minecraft.getInstance().player.registryAccess();
    }

    public static boolean canRenderGui() {
        Minecraft mc = Minecraft.getInstance();
        return !(mc.level == null || mc.player == null || mc.options.hideGui);
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

    public static HealthCapability health() {
        return health;
    }

    public static void setHealth(HealthCapability health) {
        ClientAccessor.health = health;
    }

    public static HealthScreen healthScreen() {
        return healthScreen;
    }

    public static void setHealthScreen(HealthScreen healthScreen) {
        ClientAccessor.healthScreen = healthScreen;
    }

    public static boolean showingScreen() {
        return showingScreen;
    }

    public static void setShowingScreen(boolean showingScreen) {
        ClientAccessor.showingScreen = showingScreen;
    }
}
