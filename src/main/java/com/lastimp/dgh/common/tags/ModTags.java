package com.lastimp.dgh.common.tags;

import com.lastimp.dgh.common.utils.ResourceHelper;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class ModTags {
    public static final TagKey<Item> MEDICINE = ItemTags.create(ResourceHelper.ResourceLocation("c", "medicine"));
    public static final TagKey<Item> MEDICINE_DIRECT = ItemTags.create(ResourceHelper.ResourceLocation("c", "medicine_direct"));

    public static final TagKey<Item> MEDICAL_TOOLS = ItemTags.create(ResourceHelper.ResourceLocation("c", "medical_tools"));
    public static final TagKey<Item> MEDICAL_TOOLS_BASIC = ItemTags.create(ResourceHelper.ResourceLocation("c", "medical_tools_basic"));
    public static final TagKey<Item> MEDICAL_TOOLS_SURGERY = ItemTags.create(ResourceHelper.ResourceLocation("c", "medical_tools_surgery"));
    public static final TagKey<Item> MEDICAL_TOOLS_SHEARS = ItemTags.create(ResourceHelper.ResourceLocation("forge", "shears"));
    public static final TagKey<Item> MEDICAL_TOOLS_SMALL_BAGS = ItemTags.create(ResourceHelper.ResourceLocation("c", "medical_tools_small_bags"));
    public static final TagKey<Item> MEDICAL_USAGE_BAGS = ItemTags.create(ResourceHelper.ResourceLocation("c", "medical_usage_bags"));

    public static final TagKey<Item> MEDICAL_LIMBS = ItemTags.create(ResourceHelper.ResourceLocation("c", "limbs"));

    public static final TagKey<Item> OXYGEN_SUPPLIERS = ItemTags.create(ResourceHelper.ResourceLocation("c", "oxygen_suppliers"));
    public static final TagKey<Item> AUTOPULSE = ItemTags.create(ResourceHelper.ResourceLocation("c", "autopulse"));

    public static final TagKey<Item> ORGAN = ItemTags.create(ResourceHelper.ResourceLocation("c", "organ"));
    public static final TagKey<Item> ORGAN_HEAD = ItemTags.create(ResourceHelper.ResourceLocation("c", "organ_head"));
    public static final TagKey<Item> ORGAN_TORSO = ItemTags.create(ResourceHelper.ResourceLocation("c", "organ_torso"));
    public static final TagKey<Item> ORGAN_LEG = ItemTags.create(ResourceHelper.ResourceLocation("c", "organ_leg"));
    public static final TagKey<Item> ORGAN_ARM = ItemTags.create(ResourceHelper.ResourceLocation("c", "organ_arm"));

    public static final TagKey<Item> BRAIN = ItemTags.create(ResourceHelper.ResourceLocation("c", "brain"));
    public static final TagKey<Item> EYE = ItemTags.create(ResourceHelper.ResourceLocation("c", "eye"));
    public static final TagKey<Item> HEART = ItemTags.create(ResourceHelper.ResourceLocation("c", "heart"));
    public static final TagKey<Item> KIDNEY = ItemTags.create(ResourceHelper.ResourceLocation("c", "kidney"));
    public static final TagKey<Item> LIVER = ItemTags.create(ResourceHelper.ResourceLocation("c", "liver"));
    public static final TagKey<Item> LUNGS = ItemTags.create(ResourceHelper.ResourceLocation("c", "lungs"));
    public static final TagKey<Item> MUSCLE = ItemTags.create(ResourceHelper.ResourceLocation("c", "muscle"));
    public static final TagKey<Item> NEURO = ItemTags.create(ResourceHelper.ResourceLocation("c", "neuro"));
    public static final TagKey<Item> SKIN = ItemTags.create(ResourceHelper.ResourceLocation("c", "skin"));
    public static final TagKey<Item> STOMACH = ItemTags.create(ResourceHelper.ResourceLocation("c", "stomach"));
    public static final TagKey<Item> SPINAL_CORD = ItemTags.create(ResourceHelper.ResourceLocation("c", "spinal_cord"));

    private static final Set<TagKey<Item>> shearSet = new HashSet<>();
    private static final Set<TagKey<Item>> healthScreenAvaItem = new HashSet<>();

    static {
        addShearTage(MEDICAL_TOOLS_SHEARS);

        addHealthScreenAvaItem(ModTags.MEDICINE);
        addHealthScreenAvaItem(ModTags.MEDICINE_DIRECT);
        addHealthScreenAvaItem(ModTags.MEDICAL_TOOLS);
        addHealthScreenAvaItem(ModTags.MEDICAL_TOOLS_SMALL_BAGS);
    }

    public static void addShearTage(TagKey<Item> key) {
        shearSet.add(key);
    }

    public static void addHealthScreenAvaItem(TagKey<Item> key) {
        healthScreenAvaItem.add(key);
    }

    public static boolean isShears(ItemStack stack) {
        for (var key : shearSet) {
            if (stack.is(key))
                return true;
        }
        return false;
    }

    public static boolean isHealthScreenAvaItem(ItemStack stack) {
        for (var key : healthScreenAvaItem) {
            if (stack.is(key))
                return true;
        }
        return false;
    }
}
