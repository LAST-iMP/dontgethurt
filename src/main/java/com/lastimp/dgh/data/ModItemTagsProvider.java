
package com.lastimp.dgh.data;

import com.lastimp.dgh.api.tags.ModTags;
import com.lastimp.dgh.neoforge.Common;
import com.lastimp.dgh.source.register.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends TagsProvider<Item> {
    public static final ResourceKey<Item> SHEARS = ResourceKey.create(Registries.ITEM, Common.ResourceLocation("minecraft", "shears"));
    public static final ResourceKey<Item> WRITABLE_BOOK = ResourceKey.create(Registries.ITEM, Common.ResourceLocation("minecraft", "writable_book"));

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
                .add(ModItems.SUTURE.getKey())
                .add(ModItems.NALOXONE.getKey())
                .add(ModItems.TOURNIQUET.getKey())
                .add(ModItems.NEEDLE.getKey())
                .add(ModItems.ADRENALINE.getKey())
                .add(ModItems.ANTISEPTIC.getKey())
                .add(ModItems.ANTIBIOTICS.getKey())
                .add(ModItems.ANTIBIOTIC_OINTMENT.getKey())
                .add(ModItems.PLASTIC_SKIN.getKey())
                .add(ModItems.ANTIBIOTIC_GLUE.getKey())
                .add(ModItems.MANNITOL.getKey());

        this.tag(ModTags.MEDICAL_TOOLS)
                .add(SHEARS)
                .add(WRITABLE_BOOK)
                .add(ModItems.HEALTH_SCANNER.getKey())
                .add(ModItems.BLOOD_SCANNER.getKey())
                .add(ModItems.WOOD_WRENCH.getKey())
                .add(ModItems.HEALTH_CARE_BAG.getKey())
                .add(ModItems.SURGERY_TOOL_BAG.getKey())
                .add(ModItems.LIMB_REF_BEG.getKey())
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
                .add(ModItems.BONE_NETHERITE.getKey())
                .add(ModItems.MEDICAL_STENT.getKey())
                .add(ModItems.DRAINAGE.getKey())
                .add(ModItems.OXYGEN_MASK.getKey())
                .add(ModItems.ANTISEPTIC_SPRAYER.getKey())
                .add(ModItems.AUTOPULSE.getKey())
                .add(ModItems.HUMAN_HAND.getKey())
                .add(ModItems.HUMAN_LEG.getKey())
                .add(ModItems.STASIS_BAG.getKey());

        this.tag(ModTags.MEDICAL_TOOLS_BASIC)
                .add(SHEARS)
                .add(ModItems.HEALTH_SCANNER.getKey())
                .add(ModItems.BLOOD_SCANNER.getKey())
                .add(ModItems.WOOD_WRENCH.getKey())
                .add(ModItems.OXYGEN_MASK.getKey())
                .add(ModItems.ANTISEPTIC_SPRAYER.getKey())
                .add(ModItems.AUTOPULSE.getKey())
                .add(ModItems.STASIS_BAG.getKey());

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
                .add(ModItems.BONE_NETHERITE.getKey())
                .add(ModItems.MEDICAL_STENT.getKey())
                .add(ModItems.DRAINAGE.getKey())
                .add(ModItems.OXYGEN_MASK.getKey())
                .add(ModItems.HUMAN_HAND.getKey())
                .add(ModItems.HUMAN_LEG.getKey())
                .add(ModItems.SUTURE.getKey());

        this.tag(ModTags.MEDICAL_TOOLS_SHEARS)
                .add(SHEARS)
                .add(ModItems.SCALPEL.getKey());

        this.tag(ModTags.MEDICAL_LIMBS)
                .add(ModItems.HUMAN_HAND.getKey())
                .add(ModItems.HUMAN_LEG.getKey());

        this.tag(ModTags.MEDICAL_TOOLS_BAGS)
                .add(ModItems.HEALTH_CARE_BAG.getKey())
                .add(ModItems.SURGERY_TOOL_BAG.getKey())
                .add(ModItems.LIMB_REF_BEG.getKey());

        this.tag(ItemTags.BEDS)
                .add(ModItems.OPERATING_BED_BLOCK_ITEM.getKey());

        this.tag(ModTags.OXYGEN_SUPPLIERS)
                .add(ModItems.OXYGEN_MASK.getKey());
        this.tag(ModTags.AUTOPULSE)
                .add(ModItems.AUTOPULSE.getKey())
                .add(ModItems.STASIS_BAG.getKey());

        //tfc
        this.tag(ModTags.MEDICAL_TOOLS)
                .addOptional(Common.ResourceLocation("tfc", "metal/shears/bismuth_bronze"))
                .addOptional(Common.ResourceLocation("tfc", "metal/shears/black_bronze"))
                .addOptional(Common.ResourceLocation("tfc", "metal/shears/bronze"))
                .addOptional(Common.ResourceLocation("tfc", "metal/shears/copper"))
                .addOptional(Common.ResourceLocation("tfc", "metal/shears/wrought_iron"))
                .addOptional(Common.ResourceLocation("tfc", "metal/shears/steel"))
                .addOptional(Common.ResourceLocation("tfc", "metal/shears/black_steel"))
                .addOptional(Common.ResourceLocation("tfc", "metal/shears/blue_steel"))
                .addOptional(Common.ResourceLocation("tfc", "metal/shears/red_steel"));

        this.tag(ModTags.MEDICAL_TOOLS_BASIC)
                .addOptional(Common.ResourceLocation("tfc", "metal/shears/bismuth_bronze"))
                .addOptional(Common.ResourceLocation("tfc", "metal/shears/black_bronze"))
                .addOptional(Common.ResourceLocation("tfc", "metal/shears/bronze"))
                .addOptional(Common.ResourceLocation("tfc", "metal/shears/copper"))
                .addOptional(Common.ResourceLocation("tfc", "metal/shears/wrought_iron"))
                .addOptional(Common.ResourceLocation("tfc", "metal/shears/steel"))
                .addOptional(Common.ResourceLocation("tfc", "metal/shears/black_steel"))
                .addOptional(Common.ResourceLocation("tfc", "metal/shears/blue_steel"))
                .addOptional(Common.ResourceLocation("tfc", "metal/shears/red_steel"));

        this.tag(ModTags.MEDICAL_TOOLS_SHEARS)
                .addOptional(Common.ResourceLocation("tfc", "metal/shears/bismuth_bronze"))
                .addOptional(Common.ResourceLocation("tfc", "metal/shears/black_bronze"))
                .addOptional(Common.ResourceLocation("tfc", "metal/shears/bronze"))
                .addOptional(Common.ResourceLocation("tfc", "metal/shears/copper"))
                .addOptional(Common.ResourceLocation("tfc", "metal/shears/wrought_iron"))
                .addOptional(Common.ResourceLocation("tfc", "metal/shears/steel"))
                .addOptional(Common.ResourceLocation("tfc", "metal/shears/black_steel"))
                .addOptional(Common.ResourceLocation("tfc", "metal/shears/blue_steel"))
                .addOptional(Common.ResourceLocation("tfc", "metal/shears/red_steel"));
    }
}
