//package com.example.examplemod.network;
//
//import com.example.examplemod.ExampleMod;
//import com.example.examplemod.network.packet.servertoplayer.EntityRemovePacket;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraftforge.network.NetworkRegistry;
//import net.minecraftforge.network.simple.SimpleChannel;
//
//public class NetworkHandler {
//    private static final String PROTOCOL_VERSION = "1";
//    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
//            new ResourceLocation(ExampleMod.MODID, "main"),
//            () -> PROTOCOL_VERSION,
//            PROTOCOL_VERSION::equals,
//            PROTOCOL_VERSION::equals
//    );
//
//    public static void registerPackets() {
//        int packetId = 0;
//        CHANNEL.registerMessage(packetId++, EntityRemovePacket.class, EntityRemovePacket::encode, EntityRemovePacket::new, EntityRemovePacket::handle);
//    }
//}
