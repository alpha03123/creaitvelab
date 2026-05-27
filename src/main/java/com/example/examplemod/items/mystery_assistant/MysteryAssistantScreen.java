package com.example.examplemod.items.mystery_assistant;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.client.Minecraft;
import com.example.examplemod.network.ModNetwork;
import com.example.examplemod.network.MysteryAssistantModePacket;
import com.example.examplemod.network.MysteryAssistantStatusRequestPacket;

import java.util.HashMap;
import java.util.Map;

public class MysteryAssistantScreen extends Screen {
    private static final Component TITLE = Component.literal("你想问我什么问题");
    private final Map<String, Button> buttonsByMode = new HashMap<>();

    protected MysteryAssistantScreen() {
        super(TITLE);
    }

    @Override
    protected void init() {
        int buttonWidth = 180;
        int buttonHeight = 20;
        int x = (this.width - buttonWidth) / 2;
        int y = this.height / 2 - 28;
        addButton(x, y, buttonWidth, buttonHeight, "帮我看看物品怎么做?", MysteryAssistantModes.RANDOM_CRAFTING);
        addButton(x, y + 24, buttonWidth, buttonHeight, "告诉我前面的怪物是什么?", MysteryAssistantModes.ENTITY_CHAOS);
        addButton(x, y + 48, buttonWidth, buttonHeight, "告诉我前面的方块是什么?", MysteryAssistantModes.BLOCK_CHAOS);
        addButton(x, y + 72, buttonWidth, buttonHeight, "我手上的物品适合打怪物吗?", MysteryAssistantModes.WEAPON_CHAOS);
        ModNetwork.CHANNEL.sendToServer(new MysteryAssistantStatusRequestPacket());
    }

    private void addButton(int x, int y, int width, int height, String label, String key) {
        Button button = Button.builder(Component.literal(label), clickedButton -> enableMode(key, clickedButton)).bounds(x, y, width, height).build();
        button.active = false;
        this.buttonsByMode.put(key, button);
        this.addRenderableWidget(button);
    }

    private void enableMode(String key, Button button) {
        button.active = false;
        ModNetwork.CHANNEL.sendToServer(new MysteryAssistantModePacket(key));
        Minecraft.getInstance().setScreen(null);
    }

    public void updateStatus(Map<String, Integer> remainingUses) {
        for (Map.Entry<String, Button> entry : this.buttonsByMode.entrySet()) {
            entry.getValue().active = remainingUses.getOrDefault(entry.getKey(), 0) == 0;
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        graphics.drawCenteredString(this.font, TITLE, this.width / 2, this.height / 2 - 56, 0xFFFFFF);
        super.render(graphics, mouseX, mouseY, partialTick);
    }
}
