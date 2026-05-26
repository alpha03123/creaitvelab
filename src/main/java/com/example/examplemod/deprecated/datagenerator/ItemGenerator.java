//package com.example.examplemod.deprecated.datagenerator;
//
//import com.example.examplemod.ExampleMod;
//import net.minecraft.data.DataGenerator;
//import net.minecraft.data.PackOutput;
//import net.minecraftforge.common.data.ExistingFileHelper;
//import net.minecraftforge.data.event.GatherDataEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//
//@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
//public class ItemGenerator {
//
//    @SubscribeEvent
//    public static void gatherData(GatherDataEvent event) {
//        DataGenerator generator = event.getGenerator();
//        PackOutput output = generator.getPackOutput();
//        ExistingFileHelper helper = event.getExistingFileHelper();
//
//        // 语言文件生成器
//        generator.addProvider(event.includeClient(), new ModLangProvider(output));
//        // 物品模型文件生成器
//        generator.addProvider(event.includeClient(), new ModItemModelProvider(output, helper));
//    }
//}
//
//
//
//
