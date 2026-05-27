package com.example.examplemod.network;

import com.example.examplemod.items.mystery_assistant.MysteryAssistantCrafting;
import com.example.examplemod.items.mystery_assistant.MysteryAssistantModes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenRandomCraftingPacket {
    public OpenRandomCraftingPacket() {
    }

    public OpenRandomCraftingPacket(FriendlyByteBuf buffer) {
    }

    public void encode(FriendlyByteBuf buffer) {
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getSender() != null) {
                MysteryAssistantModes.purchase(context.getSender(), MysteryAssistantModes.RANDOM_CRAFTING);
                ModNetwork.sendMysteryAssistantStatus(context.getSender());
            }
        });
        context.setPacketHandled(true);
    }
}
