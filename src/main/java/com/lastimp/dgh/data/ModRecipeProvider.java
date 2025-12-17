
package com.lastimp.dgh.data;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.register.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BANDAGE.get(), 4)
                .pattern("aaa")
                .define('a', ItemTags.WOOL)
                .unlockedBy("has_wool", has(ItemTags.WOOL))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BLOOD_PACK_EMPTY.get())
                .pattern(" a ")
                .pattern("b b")
                .pattern(" b ")
                .define('a', Items.IRON_NUGGET)
                .define('b', Items.LEATHER)
                .unlockedBy("has_leather", has(Items.LEATHER))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(DontGetHurt.MODID, "blood_pack_empty"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.BLOOD_PACK_EMPTY.get())
                .requires(ModItems.BLOOD_PACK.get(), 1)
                .unlockedBy("has_blood_pack", has(ModItems.BLOOD_PACK.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(DontGetHurt.MODID, "blood_pack_empty_unfill"));
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
                .pattern(" a ")
                .pattern("bcb")
                .pattern("aaa")
                .define('a', Items.IRON_INGOT)
                .define('b', Items.REDSTONE)
                .define('c', ModItems.BLOOD_PACK_EMPTY.get())
                .unlockedBy("has_blood_pack_empty", has(ModItems.BLOOD_PACK_EMPTY.get()))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.HEALTH_SCANNER.get(), 1)
                .pattern(" a ")
                .pattern("bcb")
                .pattern("aaa")
                .define('a', Items.IRON_INGOT)
                .define('b', Items.REDSTONE)
                .define('c', ModItems.BANDAGE.get())
                .unlockedBy("has_bandage", has(ModItems.BANDAGE.get()))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.GYPSUM.get(), 1)
                .pattern(" a ")
                .pattern("aba")
                .pattern(" a ")
                .define('a', Items.CLAY_BALL)
                .define('b', Items.BONE_MEAL)
                .unlockedBy("has_clay", has(Items.CLAY_BALL))
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.MORPHINE.get(), 2)
                .requires(Items.POPPY, 1)
                .requires(Items.GLASS_BOTTLE, 2)
                .requires(Items.FERMENTED_SPIDER_EYE, 1)
                .unlockedBy("has_glass_bottle", has(Items.GLASS_BOTTLE))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.HEALTH_CARE_BAG.get(), 1)
                .pattern("bab")
                .pattern("cde")
                .pattern("bfb")
                .define('a', ItemTags.WOOL)
                .define('b', Items.LEATHER)
                .define('c', ModItems.BANDAGE.get())
                .define('d', ModItems.SUTURE.get())
                .define('e', ModItems.MORPHINE.get())
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
                .define('c', ModItems.SCALPEL.get())
                .define('d', ModItems.HEMOSTAT.get())
                .define('e', ModItems.RETRACTOR.get())
                .define('f', Items.CHEST)
                .unlockedBy("has_chest", has(Items.CHEST))
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.OPERATING_BED_BLOCK_ITEM.get(), 1)
                .requires(ItemTags.BEDS)
                .requires(ModItems.MORPHINE.get(), 1)
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
                .requires(ModItems.MORPHINE.get(), 1)
                .requires(Items.GOLDEN_APPLE, 1)
                .unlockedBy("has_mophine", has(ModItems.MORPHINE.get()))
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

        var book = PatchouliAPI.get().getBookStack(ResourceLocation.fromNamespaceAndPath(DontGetHurt.MODID, "medical_guide"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, book.getItem())
                .requires(Items.BOOK, 1)
                .requires(ModItems.BANDAGE.get(), 1)
                .unlockedBy("has_book", has(Items.BOOK))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(DontGetHurt.MODID, "medical_guide"));
    }
}
