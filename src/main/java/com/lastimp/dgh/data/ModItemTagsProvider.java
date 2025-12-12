
package com.lastimp.dgh.data;

import com.lastimp.dgh.api.tags.ModTags;
import com.lastimp.dgh.source.register.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends TagsProvider<Item> {
    public static final ResourceKey<Item> SHEARS = ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("minecraft", "shears"));

    protected ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, Registries.ITEM, lookupProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        this.tag(ModTags.MEDICINE)
                .add(ModItems.BANDAGE.getKey())
                .add(ModItems.BLOOD_PACK.getKey())
                .add(ModItems.BLOOD_PACK_EMPTY.getKey())
                .add(ModItems.GYPSUM.getKey())
                .add(ModItems.MORPHINE.getKey())
                .add(ModItems.SUTURE.getKey());

        this.tag(ModTags.MEDICAL_TOOLS)
                .add(SHEARS)
                .add(ModItems.HEALTH_SCANNER.getKey())
                .add(ModItems.BLOOD_SCANNER.getKey())
                .add(ModItems.WOOD_WRENCH.getKey())
                .add(ModItems.HEALTH_CARE_BAG.getKey())
                .add(ModItems.SURGERY_TOOL_BAG.getKey())
                .add(ModItems.SCALPEL.getKey())
                .add(ModItems.HEMOSTAT.getKey())
                .add(ModItems.RETRACTOR.getKey())
                .add(ModItems.SURGICAL_DRILL.getKey())
                .add(ModItems.TWEEZER.getKey())
                .add(ModItems.BONE_IMPLANTS.getKey())
                .add(ModItems.BONE_IMPLANTS_WOOD.getKey())
                .add(ModItems.BONE_IMPLANTS_STONE.getKey())
                .add(ModItems.BONE_IMPLANTS_COPPER.getKey())
                .add(ModItems.BONE_IMPLANTS_IRON.getKey())
                .add(ModItems.BONE_IMPLANTS_GOLD.getKey())
                .add(ModItems.BONE_IMPLANTS_DIMOND.getKey())
                .add(ModItems.BONE_IMPLANTS_NETHERITE.getKey())
                .add(ModItems.SURGERY_SAW.getKey())
                .add(ModItems.BONE_NATURAL.getKey())
                .add(ModItems.BONE_WOOD.getKey())
                .add(ModItems.BONE_STONE.getKey())
                .add(ModItems.BONE_COPPER.getKey())
                .add(ModItems.BONE_IRON.getKey())
                .add(ModItems.BONE_GOLD.getKey())
                .add(ModItems.BONE_DIMOND.getKey())
                .add(ModItems.BONE_NETHERITE.getKey());

        this.tag(ModTags.MEDICAL_TOOLS_BASIC)
                .add(SHEARS)
                .add(ModItems.HEALTH_SCANNER.getKey())
                .add(ModItems.BLOOD_SCANNER.getKey())
                .add(ModItems.WOOD_WRENCH.getKey());

        this.tag(ModTags.MEDICAL_TOOLS_SURGERY)
                .add(ModItems.SCALPEL.getKey())
                .add(ModItems.HEMOSTAT.getKey())
                .add(ModItems.RETRACTOR.getKey())
                .add(ModItems.SURGICAL_DRILL.getKey())
                .add(ModItems.TWEEZER.getKey())
                .add(ModItems.BONE_IMPLANTS.getKey())
                .add(ModItems.BONE_IMPLANTS_WOOD.getKey())
                .add(ModItems.BONE_IMPLANTS_STONE.getKey())
                .add(ModItems.BONE_IMPLANTS_COPPER.getKey())
                .add(ModItems.BONE_IMPLANTS_IRON.getKey())
                .add(ModItems.BONE_IMPLANTS_GOLD.getKey())
                .add(ModItems.BONE_IMPLANTS_DIMOND.getKey())
                .add(ModItems.BONE_IMPLANTS_NETHERITE.getKey())
                .add(ModItems.SURGERY_SAW.getKey())
                .add(ModItems.BONE_NATURAL.getKey())
                .add(ModItems.BONE_WOOD.getKey())
                .add(ModItems.BONE_STONE.getKey())
                .add(ModItems.BONE_COPPER.getKey())
                .add(ModItems.BONE_IRON.getKey())
                .add(ModItems.BONE_GOLD.getKey())
                .add(ModItems.BONE_DIMOND.getKey())
                .add(ModItems.BONE_NETHERITE.getKey());

        this.tag(ModTags.MEDICAL_TOOLS_SHEARS)
                .add(SHEARS)
                .add(ModItems.SCALPEL.getKey());

        this.tag(ModTags.MEDICAL_TOOLS_BAGS)
                .add(ModItems.HEALTH_CARE_BAG.getKey())
                .add(ModItems.SURGERY_TOOL_BAG.getKey());

        this.tag(ItemTags.BEDS)
                .add(ModItems.OPERATING_BED_BLOCK_ITEM.getKey());
    }
}
