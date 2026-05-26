package com.example.examplemod.items.splash_potions.pulse;


import com.example.examplemod.registry.ModEffects;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.SplashPotionItem;
import net.minecraft.world.item.context.UseOnContext;

public class SplashPulsePotionIII extends SplashPotionItem {
    public SplashPulsePotionIII(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player != null) {
            player.addEffect(new MobEffectInstance(ModEffects.EXAMPLE_EFFECT.get(), 30, 3)); // 持续 30 秒，效果等级 2
        }
        return super.useOn(context);
    }
}
