package com.lastimp.dgh.data;

import com.lastimp.dgh.api.tags.ModDamageType;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModDamageTypeTagsProvider extends DamageTypeTagsProvider {
    public ModDamageTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        ResourceKey<DamageType>[] damages = new ResourceKey[ModDamageType.DGH_FINAL_DAMAGE.size()];
        ModDamageType.DGH_FINAL_DAMAGE.toArray(damages);

        this.tag(ModDamageType.FINAL_HEALTH_DAMAGE).add(damages);
        this.tag(DamageTypeTags.BYPASSES_ARMOR).add(damages);
        this.tag(DamageTypeTags.BYPASSES_SHIELD).add(damages);
        this.tag(DamageTypeTags.BYPASSES_INVULNERABILITY).add(damages);
        this.tag(DamageTypeTags.BYPASSES_COOLDOWN).add(damages);
        this.tag(DamageTypeTags.BYPASSES_EFFECTS).add(damages);
        this.tag(DamageTypeTags.BYPASSES_RESISTANCE).add(damages);
        this.tag(DamageTypeTags.BYPASSES_ENCHANTMENTS).add(damages);
    }
}
