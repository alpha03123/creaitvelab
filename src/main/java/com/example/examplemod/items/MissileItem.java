package com.example.examplemod.items;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.advanced_functions.EntityUtils;
import com.example.examplemod.entities.Missile;
import com.example.examplemod.registry.ModEntities;
import com.example.examplemod.registry.Moditems;
import com.example.particlecomplex.particles.base.ParticleAreaSpawnerVanilla;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


import static com.example.examplemod.advanced_functions.EntityUtils.findNearestTargetEntity;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID)
public class MissileItem extends Item {
    // 在玩家NBT中存储数据的键名
    private static final String TARGET_ENTITY = "targetEntity";
    private static final String CHARGE_ACCOUNT = "chargeAccount";

    public MissileItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        pPlayer.startUsingItem(pHand);
        return InteractionResultHolder.consume(itemstack);
    }
    @Override
    public int getUseDuration(ItemStack pStack) {
        return 36000;
    }


    // 每tick更新
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Player player = event.player;
        CompoundTag playerData = player.getPersistentData();

        // 更新玩家目标和充能状态
        updateTargetAndCharge(player, playerData);

        // 处理不同的阶段
        handleChargeAndStage(player, playerData);
    }

    // 更新玩家目标和充能状态
    private static void updateTargetAndCharge(Player player, CompoundTag playerData) {
        Entity nearEntity = findNearestTargetEntity(player, 18, 50);
        int charge = playerData.getInt(CHARGE_ACCOUNT);
        Entity lastTarget = player.level().getEntity(playerData.getInt(TARGET_ENTITY));
        // 更新目标和充能
        if (player.isUsingItem() && player.getMainHandItem().is(Moditems.MISSILE_TARGET.get()) && nearEntity != null) {
            playerData.putInt(TARGET_ENTITY, nearEntity.getId());
            playerData.putInt(CHARGE_ACCOUNT, charge + 1);
        } else {
            playerData.putInt(CHARGE_ACCOUNT, 0);
        }

        Entity currentTarget = player.level().getEntity(playerData.getInt(TARGET_ENTITY));
        if (currentTarget == null) {
            playerData.remove(TARGET_ENTITY); // 如果目标无效，移除目标ID
        }

        if (lastTarget != currentTarget) {
            playerData.putInt(CHARGE_ACCOUNT, 1);
        }

        if (playerData.getInt(CHARGE_ACCOUNT) - 1 < 0) {
            playerData.remove(CHARGE_ACCOUNT);
            playerData.remove(TARGET_ENTITY);
        }
    }

    // 处理充能和阶段事件
    private static void handleChargeAndStage(Player player, CompoundTag playerData) {
        if (!(player.level().getEntity(playerData.getInt(TARGET_ENTITY)) instanceof LivingEntity target)) return;
        int chargeAccount = playerData.getInt(CHARGE_ACCOUNT);

        // 播放充能阶段音效
        playChargingSound(player, chargeAccount);

        if (chargeAccount == 80) {
            // 当充能达到 80 时，处理特殊的导弹发射音效
            handleStageA(player, target);
            playLaunchSound(player);
        } else {
            if (chargeAccount == 1 || chargeAccount == 0) return;
            releaseParticle(player.level(), target.position(), chargeAccount);
        }
    }

    private static void releaseParticle(Level level, Vec3 pos,int chargeAccount) {
        System.out.println(pos);

        ParticleAreaSpawnerVanilla spawner=new ParticleAreaSpawnerVanilla(level, ParticleTypes.FALLING_LAVA,-5,5,0.5,5);

        spawner.createByPositionEquation(pos.x,pos.y,pos.z,"x^2+y^2+z^2-"+(50-chargeAccount/2),0.4);

    }

    private static void handleStageA(Player player, LivingEntity target) {
        Vec3 forward = player.getLookAngle().normalize(); // 玩家朝向向量 (a, b, c)
        Vec3 up = new Vec3(0, 1, 0); // 世界的上方向
        Vec3 right = forward.cross(up).normalize(); // 计算玩家右侧方向向量

        // 计算扇形角度范围（从 -angle/2 到 +angle/2）
        double angleRange = Math.toRadians(180); // 假设扇形的角度范围为 180°

        for (int i = 0; i < 45; i++) {
            // 计算当前导弹的偏移角度，根据 i 来从左到右变化
            double angle = -angleRange + (2.0 * i / 24) * angleRange; // 从 -90-90

            // 计算旋转矩阵，来旋转玩家的朝向，使导弹有扇形发射效果
            Vec3 offsetDirection = rotateVectorAroundY(forward, angle);

            // 计算最终偏移向量 (从最左侧到最右侧)，控制最大宽度
            Vec3 offset = right.scale(0.5); // 可以调整这个值来控制导弹左右的分布范围

            // 计算最终导弹的初始位置，确保它们均匀分布
            Vec3 missilePosition = EntityUtils.getForwardPosition(player, 1).add(new Vec3(0,1.5,0)).add(offset);

            // 生成导弹
            Missile missile = new Missile(
                    ModEntities.BASE_COMPLEX_ENTITY.get(),
                    player.level(),
                    target,
                    2.5,
                    offsetDirection.scale(4.5),  // 初速度方向朝玩家朝向+偏移角度
                    0.12 + 0.05 * i,
                    0.1 + i * 0.2,
                    0.15,
                    missilePosition
            );

            missile.lifetime = 320;
            player.level().addFreshEntity(missile);
        }
    }

    // 旋转一个向量（forward）绕 Y 轴，角度是 angle（单位：弧度）
    private static Vec3 rotateVectorAroundY(Vec3 vector, double angle) {
        double cosAngle = Math.cos(angle);
        double sinAngle = Math.sin(angle);

        // 旋转矩阵
        double x = vector.x * cosAngle - vector.z * sinAngle;
        double z = vector.x * sinAngle + vector.z * cosAngle;

        return new Vec3(x, vector.y, z); // 返回旋转后的新向量
    }
    // 处理不同充能阶段的音效
    private static void playChargingSound(Player player, int chargeAccount) {
        // 根据充能阶段播放不同的音效
        if (chargeAccount == 20) {

            // 播放低音调音效
            player.level().playSound(null, player.blockPosition(), SoundEvents.NOTE_BLOCK_PLING.get(), SoundSource.PLAYERS, 1.0f, 0.75f);
        } else if (chargeAccount == 40) {
            // 播放中音调音效
            player.level().playSound(null, player.blockPosition(), SoundEvents.NOTE_BLOCK_PLING.get(), SoundSource.PLAYERS, 1.0f, 1f);
        } else if (chargeAccount == 60) {
            // 播放高音调音效
            player.level().playSound(null, player.blockPosition(), SoundEvents.NOTE_BLOCK_PLING.get(), SoundSource.PLAYERS, 1.0f, 1.25f);
        }
    }

    // 处理特殊的导弹发射音效
    private static void playLaunchSound(Player player) {
        // 播放导弹发射音效
        player.level().playSound(null, player.blockPosition(), SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.PLAYERS, 3.0f, 1f);
    }



}
