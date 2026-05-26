//package com.example.examplemod.deprecated.datagenerator;
//
//import com.example.examplemod.ExampleMod;
//import com.example.examplemod.registry.Moditems;
//import net.minecraft.data.PackOutput;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.item.Item;
//import net.minecraftforge.client.model.generators.ItemModelProvider;
//import net.minecraftforge.common.data.ExistingFileHelper;
//import net.minecraftforge.registries.ForgeRegistries;
//
//public class ModItemModelProvider extends ItemModelProvider {
//    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
//        super(output, ExampleMod.MODID, existingFileHelper);
//    }
//
//
//    protected void registerModels() {
//        Item item = Moditems.MeteoriteItem.get();
//        simpleItem(item);
//
//    }
//
//
//        private void simpleItem(Item item) {
//            ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);
//            getBuilder(id.getPath())
//                    .parent(getExistingFile(mcLoc("item/generated")))
//                    .texture("layer0", modLoc("item/" + id.getPath()));
//        }
//}
