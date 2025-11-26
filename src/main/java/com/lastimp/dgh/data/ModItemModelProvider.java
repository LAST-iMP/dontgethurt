
package com.lastimp.dgh.data;

import com.lastimp.dgh.source.register.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Objects;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.basicItem(ModItems.HEALTH_SCANNER.get());
        this.basicItem(ModItems.BLOOD_PACK.get());
        this.basicItem(ModItems.BLOOD_PACK_EMPTY.get());
        this.basicItem(ModItems.BLOOD_SCANNER.get());
        this.basicItem(ModItems.BANDAGE.get());
        this.basicItem(ModItems.GYPSUM.get());
        this.basicItem(ModItems.MORPHINE.get());
        this.basicItem(ModItems.SUTURE.get());
        this.basicItem(ModItems.HEALTH_CARE_BAG.get());
        this.basicItem(ModItems.WOOD_WRENCH.get());
    }

    public ResourceLocation getResourceLocation(Item item){
        return Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item));
    }

//    public void magicIngotModel(ResourceLocation item){
//        this.getBuilder(item.toString())
//                .parent(new ModelFile.UncheckedModelFile("item/generated"))
//                .texture("layer0",new ResourceLocation("item/iron_ingot"))
//                .override().predicate(new ResourceLocation(ExampleMod.MODID,"size"),16).model(new ModelFile.UncheckedModelFile("item/gold_ingot"))
//                .end();
//    }
}
