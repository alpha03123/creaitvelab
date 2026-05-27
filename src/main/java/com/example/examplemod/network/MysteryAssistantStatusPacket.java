package com.example.examplemod.network;

import com.example.examplemod.items.mystery_assistant.MysteryAssistantClient;
import com.example.examplemod.items.mystery_assistant.MysteryAssistantModes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MysteryAssistantStatusPacket {
    private final Map<String, Integer> remainingUses;

    public MysteryAssistantStatusPacket(Map<String, Integer> remainingUses) {
        this.remainingUses = remainingUses;
    }

    public MysteryAssistantStatusPacket(FriendlyByteBuf buffer) {
        this.remainingUses = new LinkedHashMap<>();
        for (String key : MysteryAssistantModes.keys()) {
            this.remainingUses.put(key, buffer.readVarInt());
        }
    }

    public static MysteryAssistantStatusPacket from(Player player) {
        Map<String, Integer> remainingUses = new LinkedHashMap<>();
        for (String key : MysteryAssistantModes.keys()) {
            remainingUses.put(key, MysteryAssistantModes.remainingUses(player, key));
        }
        return new MysteryAssistantStatusPacket(remainingUses);
    }

    public void encode(FriendlyByteBuf buffer) {
        for (String key : MysteryAssistantModes.keys()) {
            buffer.writeVarInt(this.remainingUses.getOrDefault(key, 0));
        }
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> MysteryAssistantClient.updateStatus(this.remainingUses)));
        context.setPacketHandled(true);
    }
}
