
package com.lastimp.dgh.api.tags;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.neoforge.Common;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public class DamageTag {
    public static final ResourceKey<Registry<DamageType>> BLUNT_TRAUMA_DAMAGE =
            ResourceKey.create(Registries.DAMAGE_TYPE.registryKey(), Common.ResourceLocation(DontGetHurt.MODID, "blunt_trauma"));
//    public static final ResourceKey<DamageType> BLUNT_TRAUMA_DAMAGE =
//            ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(DontGetHurt.MODID, "blunt_trauma"));
}
