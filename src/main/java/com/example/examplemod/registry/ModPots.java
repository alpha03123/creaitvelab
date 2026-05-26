package com.example.examplemod.registry;

import com.example.particlecomplex.ExampleMod;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModPots {
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, ExampleMod.MODID);

    // 设置药水效果的时长和等级
    public static final RegistryObject<Potion> MY_POTION = POTIONS.register("my_potion", () -> new Potion(
            new MobEffectInstance(ModEffects.EXAMPLE_EFFECT.get(), 600, 1) // 持续时间 600 ticks（30 秒），效果等级为 2（amplifier = 1）
    ));
    public static final RegistryObject<Potion> BIG_LUCK_1 = POTIONS.register("big_luck_1",
            () -> new Potion(new MobEffectInstance(ModEffects.BIG_LUCK.get(), 40*20, 0)));
    public static final RegistryObject<Potion> BIG_LUCK_3 = POTIONS.register("big_luck_3",
            () -> new Potion(new MobEffectInstance(ModEffects.BIG_LUCK.get(), 40*20, 2)));
    public static final RegistryObject<Potion> BIG_LUCK_5 = POTIONS.register("big_luck_5",
            () -> new Potion(new MobEffectInstance(ModEffects.BIG_LUCK.get(), 40*20, 4)));

    public static void register() {
        POTIONS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
