package com.example.examplemod.registry;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.commands.Cast;

import com.example.examplemod.commands.Test;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID,bus=Mod.EventBusSubscriber.Bus.FORGE)
public class ModCommands {
    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event){
        Cast.register(event.getDispatcher());
        Test.register(event.getDispatcher());
    }

}
