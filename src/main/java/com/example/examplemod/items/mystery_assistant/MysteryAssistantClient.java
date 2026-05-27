package com.example.examplemod.items.mystery_assistant;

import net.minecraft.client.Minecraft;

import java.util.Map;

public final class MysteryAssistantClient {
    private MysteryAssistantClient() {
    }

    public static void openScreen() {
        Minecraft.getInstance().setScreen(new MysteryAssistantScreen());
    }

    public static void updateStatus(Map<String, Integer> remainingUses) {
        if (Minecraft.getInstance().screen instanceof MysteryAssistantScreen screen) {
            screen.updateStatus(remainingUses);
        }
    }
}
