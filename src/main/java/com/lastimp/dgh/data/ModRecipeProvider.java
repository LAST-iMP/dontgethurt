package com.lastimp.dgh.data;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.Register.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BANDAGE.get(), 4)
                .pattern("aaa")
                .define('a', ItemTags.WOOL)
                .unlockedBy("has_wool", has(ItemTags.WOOL))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BLOOD_PACK_EMPTY)
                .pattern(" a ")
                .pattern("b b")
                .pattern(" b ")
                .define('a', Items.IRON_NUGGET)
                .define('b', Items.LEATHER)
                .unlockedBy("has_leather", has(Items.LEATHER))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(DontGetHurt.MODID, "blood_pack_empty"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.BLOOD_PACK_EMPTY)
                .requires(ModItems.BLOOD_PACK.get(), 1)
                .unlockedBy("has_blood_pack", has(ModItems.BLOOD_PACK))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(DontGetHurt.MODID, "blood_pack_empty_unfill"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SUTURE.get(), 8)
                .pattern("c a")
                .pattern("ca ")
                .pattern("bcc")
                .define('a', Items.IRON_NUGGET)
                .define('b', Items.IRON_INGOT)
                .define('c', Items.STRING)
                .unlockedBy("has_iron", has(Items.STRING))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BLOOD_SCANNER.get(), 1)
                .pattern(" a ")
                .pattern("bcb")
                .pattern("aaa")
                .define('a', Items.IRON_INGOT)
                .define('b', Items.REDSTONE)
                .define('c', ModItems.BLOOD_PACK_EMPTY)
                .unlockedBy("has_blood_pack_empty", has(ModItems.BLOOD_PACK_EMPTY))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.HEALTH_SCANNER.get(), 1)
                .pattern(" a ")
                .pattern("bcb")
                .pattern("aaa")
                .define('a', Items.IRON_INGOT)
                .define('b', Items.REDSTONE)
                .define('c', ModItems.BANDAGE)
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
                .save(recipeOutput);
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

        var book = PatchouliAPI.get().getBookStack(ResourceLocation.fromNamespaceAndPath(DontGetHurt.MODID, "medical_guide"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, book)
                .requires(Items.BOOK, 1)
                .requires(ModItems.BANDAGE, 1)
                .unlockedBy("has_book", has(Items.BOOK))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(DontGetHurt.MODID, "medical_guide"));
    }
}
