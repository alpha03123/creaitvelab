package com.example.examplemod.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GuessDisease extends Item {
    public GuessDisease(Properties pProperties) {
        super(pProperties);
    }
    private static final String[] ROMAN_NUMERALS = {
            "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"
    };

    public static String getRomanNumeral(int number) {
        if (number >= 1 && number <= ROMAN_NUMERALS.length) {
            return ROMAN_NUMERALS[number - 1];
        }
        return String.valueOf(number); // 超出范围时返回数字
    }
    public static String formatEffect(MobEffectInstance effect) {
        MobEffect mobEffect = effect.getEffect();

        // 名称（本地化）
        Component nameComponent = mobEffect.getDisplayName();
        String name = nameComponent.getString();

        // 等级（Amplifier 从 0 开始，对应 I）
        int level = effect.getAmplifier() + 1;
        String levelRoman = getRomanNumeral(level);

        // 时间（单位为 tick，20 tick = 1 秒）
        int durationSeconds = effect.getDuration() / 20;

        return name + " " + levelRoman + "级 持续时间 " + durationSeconds + " 秒";
    }


    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack stack, Player player, @NotNull LivingEntity target, @NotNull InteractionHand hand) {
        if (player.level().isClientSide) return InteractionResult.SUCCESS;

        ItemStack offhand = player.getOffhandItem();
        if (!(offhand.getItem() instanceof PotionItem || offhand.getItem() instanceof SplashPotionItem)) {
            return InteractionResult.PASS;
        }

        List<MobEffectInstance> effects = PotionUtils.getMobEffects(offhand);
        if (effects.isEmpty()) return InteractionResult.PASS;

        int color = PotionUtils.getColor(offhand);

        if (offhand.getItem() instanceof SplashPotionItem) {
            // 对目标及附近非玩家实体加效果、播放粒子、发送文本
            List<LivingEntity> targets = target.level().getEntitiesOfClass(
                    LivingEntity.class,
                    target.getBoundingBox().inflate(6),
                    e -> e.isAlive() && !(e instanceof Player) // 不对玩家加效果
            );

            for (LivingEntity entity : targets) {
                for (MobEffectInstance effect : effects) {
                    entity.addEffect(new MobEffectInstance(effect));
                    spawnEffectParticles(entity, color);

                    // 发送消息（每个目标都发）
                    Component message1 = Component.literal("<")
                            .append(player.getDisplayName())
                            .append("> 我猜你的病是")
                            .append(Component.literal("[").withStyle(ChatFormatting.RED))
                            .append(Component.literal(formatEffect(effect)).withStyle(ChatFormatting.RED))
                            .append(Component.literal("]").withStyle(ChatFormatting.RED));
                    player.sendSystemMessage(message1);

                    Component message2 = Component.literal("<")
                            .append(entity.getDisplayName())
                            .append("> ")
                            .append(Component.literal("猜对了").withStyle(ChatFormatting.GREEN));
                    player.sendSystemMessage(message2);
                }
            }

        } else { // 普通药水
            for (MobEffectInstance effect : effects) {
                target.addEffect(new MobEffectInstance(effect));
                spawnEffectParticles(target, color);

                Component message1 = Component.literal("<")
                        .append(player.getDisplayName())
                        .append("> 我猜你的病是")
                        .append(Component.literal("[").withStyle(ChatFormatting.RED))
                        .append(Component.literal(formatEffect(effect)).withStyle(ChatFormatting.RED))
                        .append(Component.literal("]").withStyle(ChatFormatting.RED));
                player.sendSystemMessage(message1);

                Component message2 = Component.literal("<")
                        .append(target.getDisplayName())
                        .append("> ")
                        .append(Component.literal("猜对了").withStyle(ChatFormatting.GREEN));
                player.sendSystemMessage(message2);
            }
        }

        // 消耗副手药水
        offhand.shrink(1);
        player.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(Items.GLASS_BOTTLE));

        return InteractionResult.CONSUME;
    }
    private void spawnEffectParticles(LivingEntity entity, int color) {
        double x = entity.getX();
        double y = entity.getY() + entity.getBbHeight() / 2.0;
        double z = entity.getZ();

        float r = (color >> 16 & 255) / 255.0f;
        float g = (color >> 8 & 255) / 255.0f;
        float b = (color & 255) / 255.0f;

        for (int i = 0; i < 10; i++) {
            entity.level().addParticle(
                    net.minecraft.core.particles.DustParticleOptions.REDSTONE,
                    x + (entity.getRandom().nextDouble() - 0.5),
                    y + entity.getRandom().nextDouble(),
                    z + (entity.getRandom().nextDouble() - 0.5),
                    r, g, b
            );
        }
    }


}
