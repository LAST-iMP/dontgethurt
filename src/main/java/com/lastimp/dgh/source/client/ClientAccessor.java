package com.lastimp.dgh.source.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@OnlyIn(value = Dist.CLIENT)
public abstract class ClientAccessor {
    private static final Lazy<Minecraft> MINECRAFT = Lazy.of(Minecraft::getInstance);

    public static Minecraft mc() {
        return MINECRAFT.get();
    }

    public static boolean canRenderGui() {
        Minecraft mc = Minecraft.getInstance();
        return !(mc.level == null || mc.player == null || mc.options.hideGui);
    }

    public static ClientLevel getLevel() {
        return Minecraft.getInstance().level;
    }

    public static long getGameTime() {
        return mc().level.getGameTime();
    }

    public static Optional<LocalPlayer> getPlayer() {
        return Optional.ofNullable(Minecraft.getInstance().player);
    }

    public static @NotNull LocalPlayer getPlayerOrThrow() {
        return Objects.requireNonNull(Minecraft.getInstance().player);
    }

    public static LivingEntity getLiving(ClientLevel level, UUID uuid, Vec3 center, int range) {
        var result = level.getEntitiesOfClass(
                LivingEntity.class, AABB.ofSize(center, range, range, range),
                (entity) -> entity.getUUID().equals(uuid)
        );
        if (!result.isEmpty())
            return result.get(0);
        return null;
    }

    public static LivingEntity getLiving(int id) {
        var result = getLevel().getEntity(id);
        if (result instanceof LivingEntity livingEntity) {
            return livingEntity;
        }
        return null;
    }
}
