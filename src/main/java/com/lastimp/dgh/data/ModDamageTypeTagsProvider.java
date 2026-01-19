package com.lastimp.dgh.data;

import com.lastimp.dgh.api.tags.ModDamageType;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;

import java.util.concurrent.CompletableFuture;

public class ModDamageTypeTagsProvider extends DamageTypeTagsProvider {
    public ModDamageTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId) {
        super(output, lookupProvider, modId);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(ModDamageType.FINAL_HEALTH_DAMAGE)
                .add(ModDamageType.OPEN_WOUND_DAMAGE)
                .add(ModDamageType.INTERNAL_INJURY_DAMAGE)
                .add(ModDamageType.BURN_DAMAGE)
                .add(ModDamageType.BRAIN_DAMAGE)
                .add(ModDamageType.BLEED_DAMAGE)
                .add(ModDamageType.SURGERY_DAMAGE)
                .add(ModDamageType.CANT_BREATH_DAMAGE);

        this.tag(DamageTypeTags.BYPASSES_ARMOR)
                .add(ModDamageType.OPEN_WOUND_DAMAGE)
                .add(ModDamageType.INTERNAL_INJURY_DAMAGE)
                .add(ModDamageType.BURN_DAMAGE)
                .add(ModDamageType.BRAIN_DAMAGE)
                .add(ModDamageType.BLEED_DAMAGE)
                .add(ModDamageType.SURGERY_DAMAGE)
                .add(ModDamageType.CANT_BREATH_DAMAGE);
    }
}
