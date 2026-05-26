package com.example.examplemod.registry;

import com.example.examplemod.ExampleMod;

import com.example.examplemod.entities.renderer.NoRenderEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntityRenderer {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {

        EntityRenderers.register(ModEntities.AMMO.get(), NoRenderEntityRenderer::new);
        EntityRenderers.register(ModEntities.AREA.get(), NoRenderEntityRenderer::new);
        EntityRenderers.register(ModEntities.CUSTOM_ARROW.get(), NoRenderEntityRenderer::new);
        EntityRenderers.register(ModEntities.BASE_COMPLEX_ENTITY.get(), NoRenderEntityRenderer::new);
        EntityRenderers.register(ModEntities.WASP_MISSILE.get(), NoRenderEntityRenderer::new);


    }
}