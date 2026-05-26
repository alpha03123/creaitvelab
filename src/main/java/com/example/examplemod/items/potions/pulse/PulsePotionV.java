package com.example.examplemod.items.potions.pulse;

import com.example.examplemod.registry.ModEffects;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.context.UseOnContext;

public class PulsePotionV extends PotionItem {
    public PulsePotionV(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player != null) {
            player.addEffect(new MobEffectInstance(ModEffects.EXAMPLE_EFFECT.get(), 10, 5)); // 持续 30 秒，效果等级 2
        }
        return super.useOn(context);
    }
}
