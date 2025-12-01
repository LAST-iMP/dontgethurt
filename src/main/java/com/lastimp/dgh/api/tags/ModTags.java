/*
* MIT License

Copyright (c) 2023 NeoForged project

This license applies to the template files as supplied by github.com/NeoForged/MDK


Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

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
