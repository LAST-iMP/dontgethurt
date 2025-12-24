
package com.lastimp.dgh.data;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.neoforge.Common;
import com.lastimp.dgh.source.register.ModItems;
import com.lastimp.dgh.source.register.ModPotions;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = DontGetHurt.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BANDAGE.get(), 8)
                .pattern("aaa")
                .define('a', ItemTags.WOOL)
                .unlockedBy("has_wool", has(ItemTags.WOOL))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BLOOD_PACK_EMPTY)
                .pattern(" a ")
                .pattern("b b")
                .pattern(" b ")
                .define('a', ItemTags.WOOL)
                .define('b', Items.LEATHER)
                .unlockedBy("has_leather", has(Items.LEATHER))
                .save(recipeOutput, Common.ResourceLocation(DontGetHurt.MODID, "blood_pack_empty"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.BLOOD_PACK_EMPTY)
                .requires(ModItems.BLOOD_PACK.get(), 1)
                .unlockedBy("has_blood_pack", has(ModItems.BLOOD_PACK))
                .save(recipeOutput, Common.ResourceLocation(DontGetHurt.MODID, "blood_pack_empty_unfill"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SUTURE.get(), 8)
                .pattern("c a")
                .pattern("ca ")
                .pattern("bcc")
                .define('a', Items.IRON_NUGGET)
                .define('b', Items.IRON_INGOT)
                .define('c', Items.STRING)
                .unlockedBy("has_string", has(Items.STRING))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BLOOD_SCANNER.get(), 1)
                .pattern(" d ")
                .pattern("bcb")
                .pattern("aaa")
                .define('a', Items.IRON_INGOT)
                .define('b', Items.REDSTONE)
                .define('c', ModItems.BLOOD_PACK_EMPTY)
                .define('d', Items.GLASS)
                .unlockedBy("has_blood_pack_empty", has(ModItems.BLOOD_PACK_EMPTY))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.HEALTH_SCANNER.get(), 1)
                .pattern(" d ")
                .pattern("bcb")
                .pattern("aaa")
                .define('a', Items.IRON_INGOT)
                .define('b', Items.REDSTONE)
                .define('c', ModItems.BANDAGE)
                .define('d', Items.GLASS)
                .unlockedBy("has_bandage", has(ModItems.BANDAGE))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.GYPSUM.get(), 1)
                .pattern(" a ")
                .pattern("aba")
                .pattern(" a ")
                .define('a', Items.CLAY_BALL)
                .define('b', Items.BONE_MEAL)
                .unlockedBy("has_clay", has(Items.CLAY_BALL))
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.MORPHINE, 2)
                .requires(Items.POPPY, 1)
                .requires(Items.GLASS_BOTTLE, 2)
                .requires(Items.FERMENTED_SPIDER_EYE, 1)
                .unlockedBy("has_glass_bottle", has(Items.GLASS_BOTTLE))
                .save(recipeOutput, Common.ResourceLocation(DontGetHurt.MODID, "morphine"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.HEALTH_CARE_BAG.get(), 1)
                .pattern("bab")
                .pattern("cde")
                .pattern("bfb")
                .define('a', ItemTags.WOOL)
                .define('b', Items.LEATHER)
                .define('c', ModItems.BANDAGE)
                .define('d', ModItems.SUTURE)
                .define('e', ModItems.MORPHINE)
                .define('f', Items.CHEST)
                .unlockedBy("has_chest", has(Items.CHEST))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.WOOD_WRENCH.get(), 1)
                .pattern("a a")
                .pattern(" a ")
                .pattern(" a ")
                .define('a', ItemTags.PLANKS)
                .unlockedBy("has_plank", has(ItemTags.PLANKS))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BONE_IMPLANTS.get(), 1)
                .pattern(" b ")
                .pattern("bab")
                .pattern(" b ")
                .define('a', Items.BONE_BLOCK)
                .define('b', Items.BONE_MEAL)
                .unlockedBy("has_bone", has(Items.BONE))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BONE_IMPLANTS_WOOD.get(), 1)
                .pattern(" b ")
                .pattern("bab")
                .pattern(" b ")
                .define('a', Items.BONE_BLOCK)
                .define('b', ItemTags.LOGS)
                .unlockedBy("has_bone", has(Items.BONE))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BONE_IMPLANTS_STONE.get(), 1)
                .pattern(" b ")
                .pattern("bab")
                .pattern(" b ")
                .define('a', Items.BONE_BLOCK)
                .define('b', Items.STONE)
                .unlockedBy("has_bone", has(Items.BONE))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BONE_IMPLANTS_COPPER.get(), 1)
                .pattern(" b ")
                .pattern("bab")
                .pattern(" b ")
                .define('a', Items.BONE_BLOCK)
                .define('b', Items.COPPER_INGOT)
                .unlockedBy("has_bone", has(Items.BONE))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BONE_IMPLANTS_IRON.get(), 1)
                .pattern(" b ")
                .pattern("bab")
                .pattern(" b ")
                .define('a', Items.BONE_BLOCK)
                .define('b', Items.IRON_INGOT)
                .unlockedBy("has_bone", has(Items.BONE))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BONE_IMPLANTS_GOLD.get(), 1)
                .pattern(" b ")
                .pattern("bab")
                .pattern(" b ")
                .define('a', Items.BONE_BLOCK)
                .define('b', Items.GOLD_INGOT)
                .unlockedBy("has_bone", has(Items.BONE))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BONE_IMPLANTS_DIMOND.get(), 1)
                .pattern(" b ")
                .pattern("bab")
                .pattern(" b ")
                .define('a', Items.BONE_BLOCK)
                .define('b', Items.DIAMOND)
                .unlockedBy("has_bone", has(Items.BONE))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BONE_IMPLANTS_NETHERITE.get(), 1)
                .pattern(" b ")
                .pattern("bab")
                .pattern(" b ")
                .define('a', Items.BONE_BLOCK)
                .define('b', Items.NETHERITE_INGOT)
                .unlockedBy("has_bone", has(Items.BONE))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SCALPEL.get(), 1)
                .pattern(" a ")
                .pattern(" b ")
                .pattern(" b ")
                .define('a', Items.IRON_NUGGET)
                .define('b', Items.IRON_INGOT)
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.HEMOSTAT.get(), 1)
                .pattern("a a")
                .pattern(" b ")
                .pattern(" b ")
                .define('a', Items.IRON_NUGGET)
                .define('b', Items.IRON_INGOT)
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.RETRACTOR.get(), 1)
                .pattern("a a")
                .pattern("bab")
                .pattern("b b")
                .define('a', Items.IRON_NUGGET)
                .define('b', Items.IRON_INGOT)
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SURGICAL_DRILL.get(), 1)
                .pattern(" c ")
                .pattern("abc")
                .pattern("  c")
                .define('a', Items.IRON_NUGGET)
                .define('b', Items.IRON_BLOCK)
                .define('c', Items.IRON_INGOT)
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.TWEEZER.get(), 1)
                .pattern("a a")
                .pattern("a a")
                .pattern(" a ")
                .define('a', Items.IRON_NUGGET)
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SURGERY_TOOL_BAG.get(), 1)
                .pattern("bab")
                .pattern("cde")
                .pattern("bfb")
                .define('a', ItemTags.WOOL)
                .define('b', Items.LEATHER)
                .define('c', ModItems.SCALPEL)
                .define('d', ModItems.HEMOSTAT)
                .define('e', ModItems.RETRACTOR)
                .define('f', Items.CHEST)
                .unlockedBy("has_chest", has(Items.CHEST))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.OPERATING_BED_BLOCK_ITEM.get(), 1)
                .pattern(" c ")
                .pattern("bab")
                .pattern("   ")
                .define('a', ItemTags.BEDS)
                .define('b', Items.IRON_BARS)
                .define('c', ModItems.MORPHINE)
                .unlockedBy("has_bad", has(ItemTags.BEDS))
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.BONE_NATURAL.get(), 1)
                .requires(Items.BONE, 1)
                .unlockedBy("has_bone", has(Items.BONE))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BONE_WOOD.get(), 1)
                .pattern(" b ")
                .pattern("bab")
                .pattern(" b ")
                .define('a', Items.BONE)
                .define('b', ItemTags.LOGS)
                .unlockedBy("has_bone", has(Items.BONE))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BONE_STONE.get(), 1)
                .pattern(" b ")
                .pattern("bab")
                .pattern(" b ")
                .define('a', Items.BONE)
                .define('b', Items.STONE)
                .unlockedBy("has_bone", has(Items.BONE))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BONE_COPPER.get(), 1)
                .pattern(" b ")
                .pattern("bab")
                .pattern(" b ")
                .define('a', Items.BONE)
                .define('b', Items.COPPER_INGOT)
                .unlockedBy("has_bone", has(Items.BONE))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BONE_IRON.get(), 1)
                .pattern(" b ")
                .pattern("bab")
                .pattern(" b ")
                .define('a', Items.BONE)
                .define('b', Items.IRON_INGOT)
                .unlockedBy("has_bone", has(Items.BONE))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BONE_GOLD.get(), 1)
                .pattern(" b ")
                .pattern("bab")
                .pattern(" b ")
                .define('a', Items.BONE)
                .define('b', Items.GOLD_INGOT)
                .unlockedBy("has_bone", has(Items.BONE))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BONE_DIMOND.get(), 1)
                .pattern(" b ")
                .pattern("bab")
                .pattern(" b ")
                .define('a', Items.BONE)
                .define('b', Items.DIAMOND)
                .unlockedBy("has_bone", has(Items.BONE))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BONE_NETHERITE.get(), 1)
                .pattern(" b ")
                .pattern("bab")
                .pattern(" b ")
                .define('a', Items.BONE)
                .define('b', Items.NETHERITE_INGOT)
                .unlockedBy("has_bone", has(Items.BONE))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SURGERY_SAW.get(), 1)
                .pattern("  a")
                .pattern(" ab")
                .pattern("ab ")
                .define('a', Items.IRON_INGOT)
                .define('b', Items.IRON_NUGGET)
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.NALOXONE.get(), 1)
                .requires(ModItems.MORPHINE, 1)
                .requires(Items.MILK_BUCKET, 1)
                .requires(Items.GLOWSTONE_DUST, 2)
                .unlockedBy("has_mophine", has(ModItems.MORPHINE))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.TOURNIQUET.get(), 2)
                .pattern(" a ")
                .pattern("a a")
                .pattern("ba ")
                .define('a', Items.LEATHER)
                .define('b', Items.STRING)
                .unlockedBy("has_leather", has(Items.LEATHER))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.MEDICAL_STENT.get(), 1)
                .pattern(" ba")
                .pattern("bab")
                .pattern("ab ")
                .define('a', Items.STRING)
                .define('b', Items.IRON_NUGGET)
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.NEEDLE.get(), 1)
                .pattern("  b")
                .pattern(" a ")
                .pattern("a  ")
                .define('a', Items.IRON_NUGGET)
                .define('b', Items.IRON_INGOT)
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.DRAINAGE.get(), 2)
                .pattern("cbb")
                .pattern("  b")
                .pattern("abb")
                .define('a', Items.IRON_NUGGET)
                .define('b', Items.STRING)
                .define('c', Items.LEATHER)
                .unlockedBy("has_string", has(Items.STRING))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ADRENALINE.get(), 1)
                .pattern(" c ")
                .pattern("sbs")
                .pattern("rmr")
                .define('b', Items.GLASS_BOTTLE)
                .define('c', Items.GOLDEN_CARROT)
                .define('s', Items.SUGAR)
                .define('r', Items.REDSTONE)
                .define('m', Items.GLISTERING_MELON_SLICE)
                .unlockedBy("has_sugar", has(Items.SUGAR))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.OXYGEN_MASK.get(), 1)
                .pattern(" g ")
                .pattern("wbw")
                .pattern(" i ")
                .define('g', Items.GLASS)
                .define('w', ItemTags.WOOL)
                .define('i', Items.IRON_INGOT)
                .define('b', Items.BAMBOO_BLOCK)
                .unlockedBy("has_bamboo", has(Items.BAMBOO))
                .save(recipeOutput, Common.ResourceLocation(DontGetHurt.MODID, "oxygen_mask"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.OXYGEN_MASK.get(), 1)
                .requires(ModItems.OXYGEN_MASK, 1)
                .requires(Items.BAMBOO_BLOCK, 1)
                .unlockedBy("has_bamboo", has(Items.BAMBOO))
                .save(recipeOutput, Common.ResourceLocation(DontGetHurt.MODID, "oxygen_mask_repair"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.AUTOPULSE.get(), 1)
                .pattern("igi")
                .pattern("rar")
                .pattern("idi")
                .define('i', Items.IRON_INGOT)
                .define('g', Items.GOLD_INGOT)
                .define('r', Items.REDSTONE)
                .define('a', ModItems.ADRENALINE)
                .define('d', Items.DIAMOND)
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .save(recipeOutput, Common.ResourceLocation(DontGetHurt.MODID, "autopulse"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.AUTOPULSE.get(), 1)
                .requires(ModItems.AUTOPULSE, 1)
                .requires(ModItems.ADRENALINE, 1)
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .save(recipeOutput, Common.ResourceLocation(DontGetHurt.MODID, "autopulse_repair"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ANTISEPTIC.get(), 2)
                .requires(Items.GLASS_BOTTLE, 2)
                .requires(Items.WATER_BUCKET, 1)
                .requires(Items.BLAZE_POWDER, 1)
                .unlockedBy("has_blaze", has(Items.BLAZE_POWDER))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ANTISEPTIC_SPRAYER.get(), 1)
                .pattern(" r ")
                .pattern("iai")
                .pattern(" bi")
                .define('i', Items.IRON_INGOT)
                .define('a', ModItems.ANTISEPTIC)
                .define('r', Items.REDSTONE)
                .define('b', ItemTags.BUTTONS)
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .save(recipeOutput, Common.ResourceLocation(DontGetHurt.MODID, "antiseptic_sprayer"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ANTISEPTIC_SPRAYER.get(), 1)
                .requires(ModItems.ANTISEPTIC_SPRAYER, 1)
                .requires(ModItems.ANTISEPTIC, 1)
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .save(recipeOutput, Common.ResourceLocation(DontGetHurt.MODID, "antiseptic_sprayer_repair"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ANTIBIOTICS.get(), 1)
                .requires(ModItems.ANTISEPTIC, 1)
                .requires(Items.SUGAR, 1)
                .requires(Items.BROWN_MUSHROOM, 1)
                .requires(Items.RED_MUSHROOM, 1)
                .unlockedBy("has_sugar", has(Items.SUGAR))
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ANTIBIOTIC_OINTMENT.get(), 1)
                .requires(ModItems.ANTIBIOTICS, 1)
                .requires(Items.SLIME_BALL, 2)
                .unlockedBy("has_slime_ball", has(Items.SLIME_BALL))
                .save(recipeOutput);

        var book = PatchouliAPI.get().getBookStack(Common.ResourceLocation(DontGetHurt.MODID, "medical_guide"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, book)
                .requires(Items.BOOK, 1)
                .requires(ModItems.BANDAGE, 1)
                .unlockedBy("has_book", has(Items.BOOK))
                .save(recipeOutput, Common.ResourceLocation(DontGetHurt.MODID, "medical_guide"));
    }

    @SubscribeEvent // on the game event bus
    public static void registerBrewingRecipes(RegisterBrewingRecipesEvent event) {
        PotionBrewing.Builder builder = event.getBuilder();
        builder.addMix(
                // The initial potion to apply to
                Potions.STRONG_REGENERATION,
                // The brewing ingredient. This is the item at the top of the brewing stand.
                ModItems.MORPHINE.asItem(),
                // The resulting potion
                ModPotions.COMBAT_STIMULANT_POTION
        );
    }
}
