//package com.example.examplemod.config;
//
//import com.example.examplemod.ExampleMod;
//import net.minecraftforge.common.ForgeConfigSpec;
//import net.minecraftforge.fml.common.Mod;
//
//@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
//public class CommonConfig {
//    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
//    public static final ForgeConfigSpec.IntValue constNumber;
//
//    static {
//        BUILDER.comment("General configuration").push("general");
//        constNumber = BUILDER.comment("Example variable that can be changed in-game")
//                .defineInRange("constNumber", 10, 0, Integer.MAX_VALUE);
//        BUILDER.pop();
//    }
//
//    public static final ForgeConfigSpec SPEC = BUILDER.build();
//}
