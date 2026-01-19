
package com.lastimp.dgh.data;

import com.lastimp.dgh.api.tags.ModTags;
import com.lastimp.dgh.source.register.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {
    protected ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId) {
        super(output, lookupProvider, modId);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(ModTags.MEDICINE)
                .add(ModItems.BANDAGE.get())
                .add(ModItems.BLOOD_PACK.get())
                .add(ModItems.BLOOD_PACK_EMPTY.get())
                .add(ModItems.GYPSUM.get())
                .add(ModItems.MORPHINE.get())
                .add(ModItems.SUTURE.get())
                .add(ModItems.NALOXONE.get())
                .add(ModItems.TOURNIQUET.get())
                .add(ModItems.NEEDLE.get())
                .add(ModItems.ADRENALINE.get())
                .add(ModItems.ANTISEPTIC.get())
                .add(ModItems.ANTIBIOTICS.get())
                .add(ModItems.ANTIBIOTIC_OINTMENT.get())
                .add(ModItems.PLASTIC_SKIN.get())
                .add(ModItems.ANTIBIOTIC_GLUE.get())
                .add(ModItems.MANNITOL.get());

        this.tag(ModTags.MEDICAL_TOOLS)
                .add(Items.SHEARS)
                .add(Items.WRITABLE_BOOK)
                .add(ModItems.HEALTH_SCANNER.get())
                .add(ModItems.BLOOD_SCANNER.get())
                .add(ModItems.WOOD_WRENCH.get())
                .add(ModItems.HEALTH_CARE_BAG.get())
                .add(ModItems.SURGERY_TOOL_BAG.get())
                .add(ModItems.LIMB_REF_BEG.get())
                .add(ModItems.SCALPEL.get())
                .add(ModItems.HEMOSTAT.get())
                .add(ModItems.RETRACTOR.get())
                .add(ModItems.SURGICAL_DRILL.get())
                .add(ModItems.TWEEZER.get())
                .add(ModItems.BONE_IMPLANTS.get())
                .add(ModItems.BONE_IMPLANTS_WOOD.get())
                .add(ModItems.BONE_IMPLANTS_STONE.get())
                .add(ModItems.BONE_IMPLANTS_COPPER.get())
                .add(ModItems.BONE_IMPLANTS_IRON.get())
                .add(ModItems.BONE_IMPLANTS_GOLD.get())
                .add(ModItems.BONE_IMPLANTS_DIMOND.get())
                .add(ModItems.BONE_IMPLANTS_NETHERITE.get())
                .add(ModItems.SURGERY_SAW.get())
                .add(ModItems.BONE_NATURAL.get())
                .add(ModItems.BONE_WOOD.get())
                .add(ModItems.BONE_STONE.get())
                .add(ModItems.BONE_COPPER.get())
                .add(ModItems.BONE_IRON.get())
                .add(ModItems.BONE_GOLD.get())
                .add(ModItems.BONE_DIMOND.get())
                .add(ModItems.BONE_NETHERITE.get())
                .add(ModItems.MEDICAL_STENT.get())
                .add(ModItems.DRAINAGE.get())
                .add(ModItems.OXYGEN_MASK.get())
                .add(ModItems.ANTISEPTIC_SPRAYER.get())
                .add(ModItems.AUTOPULSE.get())
                .add(ModItems.HUMAN_HAND.get())
                .add(ModItems.HUMAN_LEG.get())
                .add(ModItems.STASIS_BAG.get());

        this.tag(ModTags.MEDICAL_TOOLS_BASIC)
                .add(Items.SHEARS)
                .add(ModItems.HEALTH_SCANNER.get())
                .add(ModItems.BLOOD_SCANNER.get())
                .add(ModItems.WOOD_WRENCH.get())
                .add(ModItems.OXYGEN_MASK.get())
                .add(ModItems.ANTISEPTIC_SPRAYER.get())
                .add(ModItems.AUTOPULSE.get())
                .add(ModItems.STASIS_BAG.get());

        this.tag(ModTags.MEDICAL_TOOLS_SURGERY)
                .add(ModItems.SCALPEL.get())
                .add(ModItems.HEMOSTAT.get())
                .add(ModItems.RETRACTOR.get())
                .add(ModItems.SURGICAL_DRILL.get())
                .add(ModItems.TWEEZER.get())
                .add(ModItems.BONE_IMPLANTS.get())
                .add(ModItems.BONE_IMPLANTS_WOOD.get())
                .add(ModItems.BONE_IMPLANTS_STONE.get())
                .add(ModItems.BONE_IMPLANTS_COPPER.get())
                .add(ModItems.BONE_IMPLANTS_IRON.get())
                .add(ModItems.BONE_IMPLANTS_GOLD.get())
                .add(ModItems.BONE_IMPLANTS_DIMOND.get())
                .add(ModItems.BONE_IMPLANTS_NETHERITE.get())
                .add(ModItems.SURGERY_SAW.get())
                .add(ModItems.BONE_NATURAL.get())
                .add(ModItems.BONE_WOOD.get())
                .add(ModItems.BONE_STONE.get())
                .add(ModItems.BONE_COPPER.get())
                .add(ModItems.BONE_IRON.get())
                .add(ModItems.BONE_GOLD.get())
                .add(ModItems.BONE_DIMOND.get())
                .add(ModItems.BONE_NETHERITE.get())
                .add(ModItems.MEDICAL_STENT.get())
                .add(ModItems.DRAINAGE.get())
                .add(ModItems.OXYGEN_MASK.get())
                .add(ModItems.HUMAN_HAND.get())
                .add(ModItems.HUMAN_LEG.get())
                .add(ModItems.SUTURE.get());

        this.tag(ModTags.MEDICAL_TOOLS_SHEARS)
                .add(Items.SHEARS)
                .add(ModItems.SCALPEL.get());

        this.tag(ModTags.MEDICAL_LIMBS)
                .add(ModItems.HUMAN_HAND.get())
                .add(ModItems.HUMAN_LEG.get());

        this.tag(ModTags.MEDICAL_TOOLS_BAGS)
                .add(ModItems.HEALTH_CARE_BAG.get())
                .add(ModItems.SURGERY_TOOL_BAG.get())
                .add(ModItems.LIMB_REF_BEG.get());

        this.tag(ItemTags.BEDS)
                .add(ModItems.OPERATING_BED_BLOCK_ITEM.get());

        this.tag(ModTags.OXYGEN_SUPPLIERS)
                .add(ModItems.OXYGEN_MASK.get());
        this.tag(ModTags.AUTOPULSE)
                .add(ModItems.AUTOPULSE.get())
                .add(ModItems.STASIS_BAG.get());
    }
}
