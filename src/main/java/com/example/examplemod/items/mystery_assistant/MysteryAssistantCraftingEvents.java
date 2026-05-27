package com.example.examplemod.items.mystery_assistant;

import com.example.examplemod.ExampleMod;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID)
public class MysteryAssistantCraftingEvents {
    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        if (!event.getCrafting().isEmpty() && MysteryAssistantModes.ensureReady(event.getEntity(), MysteryAssistantModes.RANDOM_CRAFTING)) {
            MysteryAssistantModes.recordTrigger(event.getEntity(), MysteryAssistantModes.RANDOM_CRAFTING, MysteryAssistantCrafting.consumePendingMessage(event.getEntity(), event.getCrafting()));
        }
    }
}
