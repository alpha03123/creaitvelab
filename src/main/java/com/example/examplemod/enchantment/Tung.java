package com.example.examplemod.enchantment;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entities.BlackHole;
import com.example.examplemod.registry.Enchantments;
import com.example.examplemod.registry.ModEntities;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

// tung tung tung sahur
@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Tung {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
    }
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        Entity hurtEntity=event.getEntity();
        if(!(hurtEntity instanceof LivingEntity livingHurtEntity))return;
        if(!(event.getSource().getEntity() instanceof LivingEntity attacker))return;
        int level=EnchantmentHelper.getEnchantmentLevel(Enchantments.Tung.get(), attacker);
        if(level<=0)return;
        if(level==5){
            BlackHole blackHole=new BlackHole(ModEntities.BASE_COMPLEX_ENTITY.get(),hurtEntity.level(),hurtEntity.position());
            hurtEntity.level().addFreshEntity(blackHole);
            EnchantmentHelper.setEnchantments(Collections.singletonMap
                    (Enchantments.Tung.get(), 1),attacker.getMainHandItem());
        }
        if(level==2){
            livingHurtEntity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING,60,1));
            livingHurtEntity.addEffect(new MobEffectInstance(MobEffects.LEVITATION,20,6));
        }
        if(level==1){
            Vec3 source=event.getSource().getSourcePosition();
            if(source!=null){
                Vec3 knock=source.subtract(hurtEntity.position());
                livingHurtEntity.knockback(13f,knock.x,knock.z);
            }
        }
        EnchantmentHelper.setEnchantments(Collections.singletonMap
                (Enchantments.Tung.get(), level-1),attacker.getMainHandItem());
    }
}
