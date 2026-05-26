package com.example.examplemod.items;

import com.example.examplemod.registry.ModEffects;
import com.example.examplemod.registry.ModSounds;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class Ha extends Item {
    private static final double D = 10.0; // 常数范围

    public Ha(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            List<LivingEntity> targets = level.getEntitiesOfClass(
                    LivingEntity.class,
                    new AABB(player.position().subtract(D, D, D), player.position().add(D, D, D)),
                    entity -> entity != player && entity.isAlive()
            );

            for (LivingEntity target : targets) {
                target.addEffect(new MobEffectInstance(ModEffects.FRIGHTENED.get(), 400, 0)); // 持续10秒，等级0
            }

        }
        player.level().playSound(
                null, // null 表示所有玩家都能听到
                player.blockPosition(), // 播放位置
                ModSounds.HA.get(),
                net.minecraft.sounds.SoundSource.PLAYERS,
                1.0f, // 音量
                1.0f  // 音高
        );

        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide);
    }
}
