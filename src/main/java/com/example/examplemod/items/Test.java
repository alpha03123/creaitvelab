package com.example.examplemod.items;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.advanced_functions.EntityUtils;
import com.example.examplemod.registry.ModEffects;
import com.example.examplemod.registry.Moditems;
import com.example.particlecomplex.particles.base.BaseParticleType;
import com.example.particlecomplex.particles.base.ParticleAreaSpawner;
import com.example.particlecomplex.particles.custom.END_ROD;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Vector4i;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID)
public class Test {
    @SubscribeEvent
    public static void onPlayerRightClickItem(PlayerInteractEvent event) {
//        event.getEntity().addEffect(new MobEffectInstance(ModEffects.INSPIRATION.get(),18*20,2));
    }
}
