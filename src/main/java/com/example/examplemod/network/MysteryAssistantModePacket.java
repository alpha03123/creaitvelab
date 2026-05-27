package com.example.examplemod.network;

import com.example.examplemod.items.mystery_assistant.MysteryAssistantModes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.Set;
import java.util.function.Supplier;

public class MysteryAssistantModePacket {
    private static final Set<String> ALLOWED_KEYS = Set.of(
            MysteryAssistantModes.RANDOM_CRAFTING,
            MysteryAssistantModes.ENTITY_CHAOS,
            MysteryAssistantModes.BLOCK_CHAOS,
            MysteryAssistantModes.WEAPON_CHAOS
    );

    private final String key;

    public MysteryAssistantModePacket(String key) {
        this.key = key;
    }

    public MysteryAssistantModePacket(FriendlyByteBuf buffer) {
        this.key = buffer.readUtf();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.key);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getSender() != null && ALLOWED_KEYS.contains(this.key)) {
                MysteryAssistantModes.purchase(context.getSender(), this.key);
                ModNetwork.sendMysteryAssistantStatus(context.getSender());
            }
        });
        context.setPacketHandled(true);
    }
}
