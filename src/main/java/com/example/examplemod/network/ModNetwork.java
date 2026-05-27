package com.example.examplemod.network;

import com.example.examplemod.ExampleMod;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetwork {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(ExampleMod.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        int packetId = 0;
        CHANNEL.registerMessage(packetId++, OpenRandomCraftingPacket.class, OpenRandomCraftingPacket::encode, OpenRandomCraftingPacket::new, OpenRandomCraftingPacket::handle);
        CHANNEL.registerMessage(packetId++, MysteryAssistantModePacket.class, MysteryAssistantModePacket::encode, MysteryAssistantModePacket::new, MysteryAssistantModePacket::handle);
        CHANNEL.registerMessage(packetId++, MysteryAssistantStatusRequestPacket.class, MysteryAssistantStatusRequestPacket::encode, MysteryAssistantStatusRequestPacket::new, MysteryAssistantStatusRequestPacket::handle);
        CHANNEL.registerMessage(packetId++, MysteryAssistantStatusPacket.class, MysteryAssistantStatusPacket::encode, MysteryAssistantStatusPacket::new, MysteryAssistantStatusPacket::handle);
    }

    public static void sendMysteryAssistantStatus(ServerPlayer player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), MysteryAssistantStatusPacket.from(player));
    }
}
