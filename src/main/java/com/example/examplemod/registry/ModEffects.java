package com.example.examplemod.registry;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.effects.BigLuck;
import com.example.examplemod.effects.Frightened;
import com.example.examplemod.effects.Inspiration;
import com.example.examplemod.effects.Pulse;


import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModEffects {
    // 创建一个 DeferredRegister 用于 MobEffect 注册
    public static final DeferredRegister<MobEffect> MOD_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, ExampleMod.MODID);

    // 注册一个自定义的药水效果
    public static final Supplier<MobEffect> EXAMPLE_EFFECT = register("example_effect", () -> new Pulse(MobEffectCategory.HARMFUL, 0xFF00FF));
    public static final Supplier<MobEffect> INSPIRATION = register("inspiration", () -> new Inspiration(MobEffectCategory.BENEFICIAL));
    public static final Supplier<MobEffect> BIG_LUCK = register("big_luck", () -> new BigLuck(MobEffectCategory.BENEFICIAL,0xBF01EF));
    public static final Supplier<MobEffect> FRIGHTENED = register("frightened", Frightened::new);
    // 注册方法
    public static <T extends MobEffect> RegistryObject<T> register(String name, Supplier<T> effect) {
        return MOD_EFFECTS.register(name, effect);
    }

    // 注册所有的药水效果
    public static void register(IEventBus eventBus) {
        MOD_EFFECTS.register(eventBus);
    }
}
