package com.example.united.WASP;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class WaspLauncherItem extends Item {
    private static final int MAX_AMMO = 8;

    public WaspLauncherItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        CompoundTag tag = stack.getOrCreateTag();
        int ammo = tag.getInt("Ammo");

        LockOnTracker tracker = LockOnTracker.get(player);
        LivingEntity target = tracker.getLockedTarget();

        if (target == null) {
            player.displayClientMessage(Component.literal("未锁定目标").withStyle(style -> style.withColor(0xFF5555)), true);
            return InteractionResultHolder.fail(stack);
        }

        if (ammo <= 0) {
            player.displayClientMessage(Component.literal("弹夹已空").withStyle(style -> style.withColor(0xFFAA00)), true);
            return InteractionResultHolder.fail(stack);
        }

        if (!level.isClientSide) {
            // 添加偏移初速度
            Vec3 direction = LockOnUtils.getRandomizedForwardVec(player);
            WaspMissileEntity missile = new WaspMissileEntity(level, player, target);

            // 设置生成位置在玩家眼前
            Vec3 spawnPos = player.getEyePosition().add(direction.normalize().scale(0.6));
            missile.setPos(spawnPos);
            missile.setDeltaMovement(direction.scale(3.2D));

            level.addFreshEntity(missile);
            level.playSound(null, player.blockPosition(), SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.PLAYERS, 1.0F, 1.0F);
        }

        // 扣除弹药
        tag.putInt("Ammo", ammo - 1);

        // 不清除锁定目标，但若玩家转向他物，需要重锁
        tracker.clearIfOtherTarget(target);

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    public static void reload(ItemStack stack) {
        stack.getOrCreateTag().putInt("Ammo", MAX_AMMO);
    }
}
