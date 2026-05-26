//package com.example.united.satellite;
//
//import com.example.examplemod.ExampleMod;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraftforge.network.NetworkRegistry;
//import net.minecraftforge.network.simple.SimpleChannel;
//
//public class NetworkHandler {
//    private static final String PROTOCOL_VERSION = "1.0";
//    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
//            new ResourceLocation(ExampleMod.MODID, "network"),
//            () -> PROTOCOL_VERSION, s -> true, s -> true);
//
//    private static int packetId = 0;
//
//    public static void register() {
//        INSTANCE.registerMessage(packetId++, SatelliteScreenshotPacket.class,
//                SatelliteScreenshotPacket::encode,
//                SatelliteScreenshotPacket::decode,
//                SatelliteScreenshotPacket::handle);
//    }
//}