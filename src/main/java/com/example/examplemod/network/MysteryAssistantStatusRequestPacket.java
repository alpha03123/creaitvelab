package com.example.examplemod.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MysteryAssistantStatusRequestPacket {
    public MysteryAssistantStatusRequestPacket() {
    }

    public MysteryAssistantStatusRequestPacket(FriendlyByteBuf buffer) {
    }

    public void encode(FriendlyByteBuf buffer) {
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getSender() != null) {
                ModNetwork.sendMysteryAssistantStatus(context.getSender());
            }
        });
        context.setPacketHandled(true);
    }
}
