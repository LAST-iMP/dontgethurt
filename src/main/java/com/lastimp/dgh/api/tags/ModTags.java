package com.lastimp.dgh.api.tags;

import com.lastimp.dgh.neoforge.Common;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {
    public static final TagKey<Item> MEDICINE = ItemTags.create(Common.ResourceLocation("c", "medicine"));

    public static final TagKey<Item> MEDICAL_TOOLS_SHEARS = ItemTags.create(Common.ResourceLocation("c", "medical_tools_shears"));

    public static final TagKey<Item> MEDICAL_TOOLS = ItemTags.create(Common.ResourceLocation("c", "medical_tools"));
    public static final TagKey<Item> MEDICAL_TOOLS_BASIC = ItemTags.create(Common.ResourceLocation("c", "medical_tools_basic"));
    public static final TagKey<Item> MEDICAL_TOOLS_SURGERY = ItemTags.create(Common.ResourceLocation("c", "medical_tools_surgery"));
}
