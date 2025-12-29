package com.lastimp.dgh.api.tags;

import com.lastimp.dgh.neoforge.Common;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class ModTags {
    public static final TagKey<Item> MEDICINE = ItemTags.create(Common.ResourceLocation("c", "medicine"));

    public static final TagKey<Item> MEDICAL_TOOLS = ItemTags.create(Common.ResourceLocation("c", "medical_tools"));
    public static final TagKey<Item> MEDICAL_TOOLS_BASIC = ItemTags.create(Common.ResourceLocation("c", "medical_tools_basic"));
    public static final TagKey<Item> MEDICAL_TOOLS_SURGERY = ItemTags.create(Common.ResourceLocation("c", "medical_tools_surgery"));
    public static final TagKey<Item> MEDICAL_TOOLS_SHEARS = ItemTags.create(Common.ResourceLocation("forge", "shears"));
    public static final TagKey<Item> MEDICAL_TOOLS_BAGS = ItemTags.create(Common.ResourceLocation("c", "medical_tools_bags"));

    public static final TagKey<Item> MEDICAL_LIMBS = ItemTags.create(Common.ResourceLocation("c", "limbs"));

    public static final TagKey<Item> OXYGEN_SUPPLIERS = ItemTags.create(Common.ResourceLocation("c", "oxygen_suppliers"));
    public static final TagKey<Item> AUTOPULSE = ItemTags.create(Common.ResourceLocation("c", "autopulse"));

    private static final Set<TagKey<Item>> shearSet = new HashSet<>();
    private static final Set<TagKey<Item>> healthScreenAvaItem = new HashSet<>();

    static {
        addShearTage(MEDICAL_TOOLS_SHEARS);

        addHealthScreenAvaItem(ModTags.MEDICINE);
        addHealthScreenAvaItem(ModTags.MEDICAL_TOOLS);
        addHealthScreenAvaItem(ModTags.MEDICAL_TOOLS_BAGS);
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
