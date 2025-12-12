package com.lastimp.dgh.api.tags;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.neoforge.Common;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

public class ModDamageType {
    public static final TagKey<DamageType> FINAL_HEALTH_DAMAGE = TagKey.create(Registries.DAMAGE_TYPE, Common.ResourceLocation(DontGetHurt.MODID, "final_health_damage"));

    public static final ResourceKey<DamageType> OPEN_WOUND_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE,
            Common.ResourceLocation(DontGetHurt.MODID, "open_wound"));

    public static final ResourceKey<DamageType> INTERNAL_INJURY_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE,
            Common.ResourceLocation(DontGetHurt.MODID, "internal_injury"));

    public static final ResourceKey<DamageType> BURN_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE,
            Common.ResourceLocation(DontGetHurt.MODID, "burn"));

    public static final ResourceKey<DamageType> BRAIN_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE,
            Common.ResourceLocation(DontGetHurt.MODID, "brain_damage"));

    public static final ResourceKey<DamageType> BLEED_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE,
            Common.ResourceLocation(DontGetHurt.MODID, "bleed"));

    public static final ResourceKey<DamageType> SURGERY_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE,
            Common.ResourceLocation(DontGetHurt.MODID, "surgery"));

    public static final ResourceKey<DamageType> CANT_BREATH_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE,
            Common.ResourceLocation(DontGetHurt.MODID, "cant_breath"));
}
