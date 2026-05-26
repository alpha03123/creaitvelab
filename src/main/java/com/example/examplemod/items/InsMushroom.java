package com.example.examplemod.items;

import com.example.examplemod.registry.ModEffects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class InsMushroom extends Item {

    public InsMushroom() {
        super(new Item.Properties().food(
                new FoodProperties.Builder()
                        .nutrition(2)  // 食物恢复4点饥饿
                        .saturationMod(0.3F)  // 设置饱和度
                        .alwaysEat()
                        .build()
        ));  // 这里可以修改物品的分类
    }


    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level pLevel, @NotNull LivingEntity pLivingEntity) {
        if (!(pLivingEntity instanceof Player player)) {
            return stack;
        }

        // 让食物物品恢复饥饿度
        super.finishUsingItem(stack, pLevel, player); // 确保食物效果生效

        // 获取 NBT 中的增长值
        CompoundTag nbt = stack.getOrCreateTag();
        int growthValue = nbt.getInt("growth_value");
        nbt.putInt("growth_value", nbt.getInt("growth_value")+1);
        // 根据增长值触发不同的效果
        if (growthValue >= 2) {
            player.addEffect(new MobEffectInstance(ModEffects.INSPIRATION.get(),200,growthValue));
//            player.displayClientMessage(Component.literal("增长值还不够高，无法触发效果！"), true);
        }
        if (growthValue >= 5) {
            BlockState state = pLevel.getBlockState(player.getOnPos());

            // 更新相邻方块的状态，这会让红石信号扩展
            pLevel.updateNeighborsAt(player.getOnPos(), Blocks.REDSTONE_BLOCK);  // 红石块是一个始终发出红石信号的方块

            // 你也可以尝试更新其他方块的位置来间接触发红石信号
            pLevel.updateNeighborsAt(player.getOnPos(), state.getBlock());
        }
        if(growthValue>=8){
            Explosion explosion = new Explosion(pLevel, null, null, null, player.getX(),  player.getY(),  player.getZ(), 5, false, Explosion.BlockInteraction.KEEP);

            // 触发爆炸伤害
            explosion.explode();
            nbt.putInt("growth_value", 0);
            // 可以选择显示爆炸粒子效果
            for (int i = 0; i < 10; i++) {
                pLevel.addParticle(ParticleTypes.EXPLOSION_EMITTER, player.getX(),  player.getY(),  player.getZ(),new Random().nextInt(-2,2), new Random().nextInt(-2,2), new Random().nextInt(-2,2));
            }

        }

        // 吃掉物品后减少数量
//        if (!player.getAbilities().instabuild) {
//            stack.shrink(1); // 物品数量减少
//        }
        Component message = Component.literal("灵感值:" + growthValue);
        player.displayClientMessage(message, true);


        return stack;
    }
}
