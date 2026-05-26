package com.example.united.pulseBow;


import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class PulseBow extends BowItem {

    private final String feature="motion";

    @Override
    public void onCraftedBy(@NotNull ItemStack stack, @NotNull Level level, @NotNull Player player) {
        super.onCraftedBy(stack, level, player);
        // 为物品设置默认的 NBT 数据
        CompoundTag tag = stack.getOrCreateTag();
        tag.putString("feature", feature);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, net.minecraft.world.entity.@NotNull Entity entity, int slot, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slot, isSelected);
        // 如果物品没有 NBT 数据，则初始化
        if (!stack.hasTag()) {
            CompoundTag tag = stack.getOrCreateTag();
            tag.putString("feature", feature);
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        if (stack.hasTag() && stack.getTag().contains("feature")) {
            String customValue = stack.getTag().getString("feature");

            // 检测是否按下 Shift 键
            if (Screen.hasShiftDown()) {
                // 显示详细信息
                tooltip.add(Component.literal("根据抛射物速度造成额外伤害").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
            } else {
                // 显示简略信息
                tooltip.add(Component.literal("动能").withStyle(ChatFormatting.GRAY));
            }
        } else {
            tooltip.add(Component.literal("缺失NBT: feature").withStyle(ChatFormatting.RED));
        }
    }
    public PulseBow(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000; // 使用时间
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity player, int timeLeft) {
        if (!level.isClientSide) {
            // 计算蓄力时间
            int charge = this.getUseDuration(stack) - timeLeft;
            float velocity = getArrowVelocity(charge);

            if (velocity >= 0.1F) {
                // 创建自定义箭矢
                PulseArrow arrow = new PulseArrow(level, player);
                arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, velocity * 3.0F, 1.0F);

                // 设置箭矢的属性
                arrow.setBaseDamage(velocity * 2.0);
                stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(player.getUsedItemHand()));

                // 发射箭矢
                level.addFreshEntity(arrow);
            }
        }
    }

    private float getArrowVelocity(int charge) {
        float velocity = charge / 20.0F; // 蓄力时间换算速度
        velocity = (velocity * velocity + velocity * 2.0F) / 3.0F; // 调整比例
        return Math.min(velocity, 1.0F);
    }
}
